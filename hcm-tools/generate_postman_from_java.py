import os
import re
import json
import sys
from collections import defaultdict

# Configuration
SERVICE_DIRS = [
    'hcm-core'
]
OUTPUT_FILE = 'hcm-core-service-pm-collection.json'
COLLECTION_NAME = 'eZHire Core API (hcm-core-service)'
FIXED_TENANT_ID = 'a15104c0-44b7-4512-b9b1-6122e7af7d41'
FIXED_ORG_ID = '08b06d14-4e03-11f0-bc56-325096b39f47'

# Helper regexes
RE_CONTROLLER = re.compile(r'@RestController|@Controller')
RE_REQUEST_MAPPING = re.compile(r'@RequestMapping\(([^)]*)\)')
# Updated to match both with and without parentheses for method-specific mappings
RE_METHOD_MAPPING = re.compile(r'@(GetMapping|PostMapping|PutMapping|DeleteMapping|PatchMapping)(\(([^)]*)\))?')
RE_METHOD_LEVEL_REQUEST_MAPPING = re.compile(r'@RequestMapping(\(([^)]*)\))?')
RE_MAPPING_PATH = re.compile(r'path\s*=\s*"([^"]+)"|value\s*=\s*"([^"]+)"|"([^"]+)"')
RE_MAPPING_METHOD = re.compile(r'method\s*=\s*(\{[^}]+\}|RequestMethod\.[A-Z]+)')
RE_METHOD = {
    'GetMapping': 'GET',
    'PostMapping': 'POST',
    'PutMapping': 'PUT',
    'DeleteMapping': 'DELETE',
    'PatchMapping': 'PATCH',
}
RE_PARAM = re.compile(r'@RequestBody|@RequestParam|@PathVariable')
RE_CLASS = re.compile(r'class\s+(\w+)')
RE_FIELD = re.compile(r'(private|public|protected)\s+([\w<>\[\]]+)\s+(\w+);')
RE_BODY_PARAM = re.compile(r'@RequestBody\s*([\w<>\.]+)?\s*(\w+)?')

# Helper to extract HTTP methods from method param
METHOD_MAP = {
    'RequestMethod.GET': 'GET',
    'RequestMethod.POST': 'POST',
    'RequestMethod.PUT': 'PUT',
    'RequestMethod.DELETE': 'DELETE',
    'RequestMethod.PATCH': 'PATCH',
}
def extract_methods_from_param(param):
    methods = []
    m = RE_MAPPING_METHOD.search(param)
    if m:
        method_val = m.group(1)
        if method_val.startswith('{'):
            # Multiple methods: {RequestMethod.GET, RequestMethod.POST}
            for meth in method_val.strip('{}').split(','):
                meth = meth.strip()
                if meth in METHOD_MAP:
                    methods.append(METHOD_MAP[meth])
        else:
            # Single method: RequestMethod.GET
            if method_val in METHOD_MAP:
                methods.append(METHOD_MAP[method_val])
    return methods

# Functional grouping mapping
FUNCTIONAL_GROUPS = {
    'CandidateController': 'Candidates',
    'VendorController': 'Vendors',
    'ApplicationController': 'Applications',
    'PositionController': 'Positions',
    'OrganizationController': 'Organizations',
    'TenantController': 'Tenants',
    'JobRequisitionController': 'Job Requisitions',
    'MasterDataController': 'Master Data',
    'CoreController': 'Core',
    'TransactionController': 'Transactions'
}

# Utility functions
def find_java_files(root):
    """Find all Java files in the given directory tree"""
    for dirpath, _, filenames in os.walk(root):
        for f in filenames:
            if f.endswith('.java'):
                yield os.path.join(dirpath, f)

def parse_class_fields(java_file):
    """Returns a dict of field: type for a DTO"""
    fields = {}
    try:
        with open(java_file, encoding='utf-8') as f:
            content = f.read()
        
        # Improved regex to handle various field declarations
        field_pattern = re.compile(r'(private|public|protected)\s+([\w<>\.\[\]]+)\s+(\w+)\s*;')
        
        for match in field_pattern.finditer(content):
            visibility, field_type, field_name = match.groups()
            # Clean up the field type (remove generics, etc.)
            clean_type = re.sub(r'<[^>]+>', '', field_type)
            clean_type = clean_type.replace('java.math.BigDecimal', 'BigDecimal')
            clean_type = clean_type.replace('java.time.', '')
            clean_type = clean_type.replace('java.util.', '')
            fields[field_name] = clean_type.strip()
            
    except Exception as e:
        print(f"Error parsing {java_file}: {e}")
    return fields

def extract_path_from_mapping_params(params):
    """Extract path from mapping parameters"""
    path_matches = RE_MAPPING_PATH.findall(params)
    for tup in path_matches:
        for p in tup:
            if p:
                return p
    return ''

def parse_controller(java_file):
    """Parse a controller file and extract endpoints"""
    endpoints = []
    try:
        with open(java_file, encoding='utf-8') as f:
            lines = f.readlines()
        
        class_level_path = ''
        in_controller_class = False
        
        for i, line in enumerate(lines):
            # Check if we're in a controller class
            if RE_CONTROLLER.search(line):
                in_controller_class = True
                # Look for class-level @RequestMapping
                for j in range(i, min(i+10, len(lines))):
                    m = RE_REQUEST_MAPPING.search(lines[j])
                    if m:
                        class_level_path = extract_path_from_mapping_params(m.group(1))
                        break
                continue
            
            # Check for method mappings (GetMapping, PostMapping, etc.)
            m = RE_METHOD_MAPPING.search(line)
            if m and in_controller_class:
                method = m.group(1)
                params = m.group(3) if m.group(3) is not None else ""
                http_method = RE_METHOD[method]
                # If params is empty, treat as collection-level
                if params.strip() == "":
                    path = ""
                else:
                    path = extract_path_from_mapping_params(params)
                
                # Find method signature and @RequestBody parameter
                body_type = None
                if http_method in ('POST', 'PUT', 'PATCH'):
                    # Look ahead for method signature and @RequestBody
                    for k in range(i, min(i+10, len(lines))):
                        if '(' in lines[k] and ')' in lines[k]:
                            # Check for @RequestBody in the method signature
                            body_match = RE_BODY_PARAM.search(lines[k])
                            if body_match:
                                body_type = body_match.group(1)
                                if not body_type:
                                    # Try to extract from parameter list
                                    sig_match = re.search(r'\(([^)]*)\)', lines[k])
                                    if sig_match:
                                        params_str = sig_match.group(1)
                                        for param in params_str.split(','):
                                            if '@RequestBody' in param:
                                                parts = param.strip().split()
                                                if len(parts) >= 2:
                                                    body_type = parts[-2].replace('<','').replace('>','')
                            break
                # Build full path
                if path in ("", "/"):
                    full_path = class_level_path
                else:
                    # Avoid duplicate resource names (e.g., candidates/candidates)
                    if path == class_level_path:
                        full_path = class_level_path
                    else:
                        full_path = (class_level_path + '/' + path).replace('//','/')
                if full_path.startswith('/'):
                    full_path = full_path[1:]
                if full_path.endswith('/') and len(full_path) > 1:
                    full_path = full_path[:-1]
                print(f"[DIAG] Parsed endpoint: {http_method} {full_path} (from {os.path.basename(java_file)})")
                endpoints.append({
                    'http_method': http_method,
                    'path': full_path,
                    'body_type': body_type,
                    'controller': os.path.basename(java_file),
                })
                continue
            # Check for method-level @RequestMapping
            m = RE_METHOD_LEVEL_REQUEST_MAPPING.search(line)
            if m and in_controller_class:
                params = m.group(2) if m.group(2) is not None else ""
                # Extract HTTP methods
                methods = extract_methods_from_param(params)
                if not methods:
                    # Default to GET if not specified
                    methods = ['GET']
                # Extract path
                path = extract_path_from_mapping_params(params)
                # Find method signature and @RequestBody parameter
                body_type = None
                if any(meth in ('POST', 'PUT', 'PATCH') for meth in methods):
                    for k in range(i, min(i+10, len(lines))):
                        if '(' in lines[k] and ')' in lines[k]:
                            body_match = RE_BODY_PARAM.search(lines[k])
                            if body_match:
                                body_type = body_match.group(1)
                                if not body_type:
                                    sig_match = re.search(r'\(([^)]*)\)', lines[k])
                                    if sig_match:
                                        params_str = sig_match.group(1)
                                        for param in params_str.split(','):
                                            if '@RequestBody' in param:
                                                parts = param.strip().split()
                                                if len(parts) >= 2:
                                                    body_type = parts[-2].replace('<','').replace('>','')
                            break
                # Build full path
                if path in ("", "/"):
                    full_path = class_level_path
                else:
                    # Avoid duplicate resource names (e.g., candidates/candidates)
                    if path == class_level_path:
                        full_path = class_level_path
                    else:
                        full_path = (class_level_path + '/' + path).replace('//','/')
                if full_path.startswith('/'):
                    full_path = full_path[1:]
                if full_path.endswith('/') and len(full_path) > 1:
                    full_path = full_path[:-1]
                for http_method in methods:
                    print(f"[DIAG] Parsed endpoint: {http_method} {full_path} (from {os.path.basename(java_file)})")
                    endpoints.append({
                        'http_method': http_method,
                        'path': full_path,
                        'body_type': body_type,
                        'controller': os.path.basename(java_file),
                    })
    except Exception as e:
        print(f"Error parsing controller {java_file}: {e}")
    
    return endpoints

def group_endpoints_by_path(endpoints):
    """Group endpoints by their path structure to create hierarchical folders"""
    groups = defaultdict(lambda: defaultdict(list))
    
    for endpoint in endpoints:
        path_parts = endpoint['path'].strip('/').split('/')
        
        # Handle different path structures
        if len(path_parts) >= 3 and path_parts[0] == 'api' and path_parts[1] == 'v1':
            # Paths like: api/v1/candidates/response-status/{candidateId}
            main_resource = path_parts[2]  # candidates
            resource_path = path_parts[2:]  # [candidates, response-status, {candidateId}]
        elif len(path_parts) >= 1:
            # Paths like: applications, positions, etc.
            main_resource = path_parts[0]  # applications
            resource_path = path_parts  # [applications, candidate, {candidateId}]
        else:
            # Fallback for empty paths
            main_resource = 'unknown'
            resource_path = path_parts
        
        if len(resource_path) >= 1:
            # Handle different types of endpoints
            if len(resource_path) == 1:
                # Top-level resource endpoints (e.g., /applications, /candidates)
                print(f"[GROUPING] Adding to _collection for {main_resource}: {endpoint['http_method']} {endpoint['path']}")
                groups[main_resource]['_collection'].append(endpoint)
            elif len(resource_path) >= 2 and resource_path[1].startswith('{'):
                # This is an ID-based endpoint (e.g., /resource/{id})
                id_variable = resource_path[1].strip('{}')
                # Check for sub-resources with nested IDs (e.g., /resource/{id}/sub/{subId})
                if len(resource_path) >= 4 and resource_path[3].startswith('{'):
                    sub_resource = resource_path[2]
                    nested_id = resource_path[3].strip('{}')
                    groups[main_resource][f"{id_variable}_{sub_resource}_{nested_id}"].append(endpoint)
                elif len(resource_path) >= 3:
                    sub_resource = resource_path[2]
                    groups[main_resource][f"{id_variable}_{sub_resource}"].append(endpoint)
                else:
                    groups[main_resource][f"{id_variable}_direct"].append(endpoint)
            else:
                # This is a sub-resource endpoint (e.g., /resource/sub-resource)
                # Add to a special group for sub-resources without IDs
                if len(resource_path) >= 2:
                    sub_resource = resource_path[1]
                    groups[main_resource][f"sub_{sub_resource}"].append(endpoint)
                else:
                    # Fallback: add to collection if we can't categorize
                    groups[main_resource]['_collection'].append(endpoint)
    
    return groups

def create_hierarchical_structure(groups, dto_fields):
    """Create the hierarchical folder structure for the collection"""
    collection_items = []
    added_endpoints = set()  # Track (method, path) pairs

    for main_resource, resource_groups in groups.items():
        functional_group_name = main_resource.title()
        functional_group_items = []

        # 1. Top-level collection endpoints (GET/POST on /resource)
        collection_requests = []
        for endpoint in resource_groups.get('_collection', []):
            key = (endpoint['http_method'], endpoint['path'])
            if key not in added_endpoints:
                collection_requests.append(build_request(endpoint, dto_fields))
                added_endpoints.add(key)
        if collection_requests:
            functional_group_items.extend(collection_requests)

        # 2. Sub-resource endpoints without IDs (e.g., /resource/sub-resource)
        sub_resource_requests = []
        for group_key, endpoints in resource_groups.items():
            if group_key.startswith('sub_'):
                sub_resource = group_key[4:]  # Remove 'sub_' prefix
                for endpoint in endpoints:
                    sub_resource_requests.append(build_request(endpoint, dto_fields))
        if sub_resource_requests:
            functional_group_items.extend(sub_resource_requests)

        # 3. ID-based folders
        id_groups = defaultdict(list)
        for group_key, endpoints in resource_groups.items():
            if group_key == '_collection' or group_key.startswith('sub_'):
                continue
            # group_key is like "candidateId_direct" or "candidateId_skills" or "candidateId_skills_skillId"
            id_variable = group_key.split('_')[0]
            id_groups[id_variable].append((group_key, endpoints))

        for id_variable, group_entries in id_groups.items():
            id_folder_items = []
            # Direct endpoints and sub-resources
            sub_resource_groups = defaultdict(list)
            for group_key, endpoints in group_entries:
                parts = group_key.split('_')
                if len(parts) == 2 and parts[1] == 'direct':
                    # Direct endpoints (GET/PUT/DELETE on /resource/{id})
                    for endpoint in endpoints:
                        key = (endpoint['http_method'], endpoint['path'])
                        if key not in added_endpoints:
                            id_folder_items.append(build_request(endpoint, dto_fields))
                            added_endpoints.add(key)
                else:
                    # Sub-resources or nested IDs
                    sub_resource = parts[1]
                    if len(parts) == 2:
                        # e.g., candidateId_skills
                        for endpoint in endpoints:
                            key = (endpoint['http_method'], endpoint['path'])
                            if key not in added_endpoints:
                                sub_resource_groups[sub_resource].append(build_request(endpoint, dto_fields))
                                added_endpoints.add(key)
                    elif len(parts) == 3:
                        # e.g., candidateId_skills_skillId
                        for endpoint in endpoints:
                            key = (endpoint['http_method'], endpoint['path'])
                            if key not in added_endpoints:
                                sub_resource_groups[f"{sub_resource}_{{{parts[2]}}}"].append(build_request(endpoint, dto_fields))
                                added_endpoints.add(key)
            # Add sub-resource folders
            for sub_resource, requests in sub_resource_groups.items():
                id_folder_items.append({
                    'name': sub_resource,
                    'item': requests  # Already built requests
                })
            if id_folder_items:
                functional_group_items.append({
                    'name': f"{{{id_variable}}}",
                    'item': id_folder_items
                })

        if functional_group_items:
            collection_items.append({
                'name': functional_group_name,
                'item': functional_group_items
            })

    return collection_items

def build_request(endpoint, dto_fields):
    """Build a Postman request object"""
    url = endpoint['path']
    
    # Debug: Check if dto_fields is being passed correctly
    print(f"[BUILD_REQUEST] dto_fields keys: {list(dto_fields.keys()) if dto_fields else 'EMPTY'}")
    
    # Replace path variables with Postman variables
    url = re.sub(r'\{(\w+)\}', r'{{\1}}', url)
    
    # Add API version prefix if not present
    if not url.startswith('api/v1/') and not url.startswith('/api/v1/'):
        url = '/api/v1/' + url
    
    # Ensure proper URL format
    if not url.startswith('/'):
        url = '/' + url
    
    # Create request name based on method and path
    path_parts = url.strip('/').split('/')
    if len(path_parts) >= 2:
        resource = path_parts[-2] if path_parts[-1].startswith('{') else path_parts[-1]
        action = endpoint['http_method']
        if action == 'GET' and path_parts[-1].startswith('{'):
            request_name = f"Get {resource.title()} by ID"
        elif action == 'GET':
            request_name = f"Get All {resource.title()}"
        elif action == 'POST':
            request_name = f"Create {resource.title()}"
        elif action == 'PUT':
            request_name = f"Update {resource.title()}"
        elif action == 'DELETE':
            request_name = f"Delete {resource.title()}"
        else:
            request_name = f"{action} {url}"
    else:
        request_name = f"{endpoint['http_method']} {endpoint['path']}"
    
    req = {
        'name': request_name,
        'request': {
            'method': endpoint['http_method'],
            'header': [
                {'key': 'Content-Type', 'value': 'application/json'}
            ],
            'url': {
                'raw': '{{baseUrl}}' + url,
                'host': ['{{baseUrl}}'],
                'path': [p for p in url.strip('/').split('/') if p]
            }
        },
        'event': [
            {
                'listen': 'test',
                'script': {
                    'type': 'text/javascript',
                    'exec': [
                        f"// Test script for {request_name}",
                        "pm.test('Response time is less than 5000ms', function () {",
                        "  pm.expect(pm.response.responseTime).to.be.below(5000);",
                        "});",
                        "",
                        "pm.test('Response has valid status code', function () {",
                        "  pm.expect(pm.response.code).to.be.oneOf([200, 201, 204, 400, 401, 403, 404, 500]);",
                        "});",
                        "",
                        "// Log response for debugging",
                        "if (pm.response.code >= 400) {",
                        "  console.log('Error Response:', pm.response.text());",
                        "}",
                        "",
                        "// Validate JSON response structure",
                        "if (pm.response.code < 400 && pm.response.headers.get('Content-Type') && pm.response.headers.get('Content-Type').includes('application/json')) {",
                        "  try {",
                        "    const jsonResponse = pm.response.json();",
                        "    pm.test('Response is valid JSON', function () {",
                        "      pm.expect(jsonResponse).to.be.an('object');",
                        "    });",
                        "  } catch (e) {",
                        "    pm.test('Response is valid JSON', function () {",
                        "      pm.expect.fail('Response is not valid JSON');",
                        "    });",
                        "  }",
                        "}"
                    ]
                }
            }
        ]
    }
    
    # Add request body for POST/PUT/PATCH methods
    if endpoint['http_method'] in ('POST', 'PUT', 'PATCH'):
        body = {}
        
        # Try to find the actual DTO for this endpoint
        resource_type = path_parts[-2] if path_parts[-1].startswith('{') else path_parts[-1]
        dto_class_name = None
        
        # Map resource types to DTO class names
        resource_to_dto = {
            'candidate': 'CandidateDTO',
            'candidates': 'CandidateDTO',
            'vendor': 'VendorDTO',
            'vendors': 'VendorDTO',
            'organization': 'OrganizationDTO',
            'organizations': 'OrganizationDTO',
            'position': 'PositionDTO',
            'positions': 'PositionDTO',
            'application': 'ApplicationDTO',
            'applications': 'ApplicationDTO',
            'application-status': 'ApplicationStatusDTO',
            'application-statuses': 'ApplicationStatusDTO',
            'tenant': 'TenantDTO',
            'tenants': 'TenantDTO',
            'job-requisition': 'JobRequisitionDTO',
            'job-requisitions': 'JobRequisitionDTO',
            'department': 'DepartmentDTO',
            'departments': 'DepartmentDTO',
            'skill': 'SkillDTO',
            'skills': 'SkillDTO',
            'country': 'CountryDTO',
            'countries': 'CountryDTO',
            'state': 'StateDTO',
            'states': 'StateDTO',
            # Add mappings for sub-resources that have their own DTOs
            'education': 'CandidateEducationDTO',
            'educations': 'CandidateEducationDTO',
            'work-history': 'CandidateWorkHistoryDTO',
            'work-histories': 'CandidateWorkHistoryDTO',
            'certification': 'CandidateCertificationDTO',
            'certifications': 'CandidateCertificationDTO',
            'reference': 'CandidateReferenceDTO',
            'references': 'CandidateReferenceDTO',
            'identity': 'CandidateIdentityDTO',
            'identities': 'CandidateIdentityDTO',
            'document': 'CandidateDocumentDTO',
            'documents': 'CandidateDocumentDTO',
            'skill': 'CandidateSkillDTO',
            'skills': 'CandidateSkillDTO',
            'status': 'ApplicationStatusDTO',
            'statuses': 'ApplicationStatusDTO',
            'position-status': 'PositionStatusDTO',
            'position-statuses': 'PositionStatusDTO',
            'organization-status': 'OrganizationStatusDTO',
            'organization-statuses': 'OrganizationStatusDTO',
            'vendor-status': 'VendorStatusDTO',
            'vendor-statuses': 'VendorStatusDTO',
            'user-role': 'UserRoleDTO',
            'user-roles': 'UserRoleDTO',
            'user-type': 'UserTypeDTO',
            'user-types': 'UserTypeDTO',
            'user-status': 'UserStatusDTO',
            'user-statuses': 'UserStatusDTO',
            'department-status': 'DepartmentStatusDTO',
            'department-statuses': 'DepartmentStatusDTO',
            'job-requisition-status': 'JobRequisitionStatusDTO',
            'job-requisition-statuses': 'JobRequisitionStatusDTO',
            'offer-status': 'OfferStatusDTO',
            'offer-statuses': 'OfferStatusDTO',
            'onboarding-status': 'OnboardingStatusDTO',
            'onboarding-statuses': 'OnboardingStatusDTO',
            'interview-status': 'InterviewStatusDTO',
            'interview-statuses': 'InterviewStatusDTO',
            'interview-feedback': 'InterviewFeedbackDTO',
            'interview-feedbacks': 'InterviewFeedbackDTO',
            'approval-status': 'ApprovalStatusDTO',
            'approval-statuses': 'ApprovalStatusDTO',
            'pipeline-stage': 'PipelineStageDTO',
            'pipeline-stages': 'PipelineStageDTO',
            'approval': 'ApprovalDTO',
            'approvals': 'ApprovalDTO',
            'interview': 'InterviewDTO',
            'interviews': 'InterviewDTO',
            'offer': 'OfferDTO',
            'offers': 'OfferDTO',
            'onboarding': 'OnboardingDTO',
            'onboardings': 'OnboardingDTO',
            'user': 'UserDTO',
            'users': 'UserDTO',
            # Also try CreateDTO variants for POST operations
            'CandidateCreateDTO': 'CandidateCreateDTO',
            'ApplicationCreateDTO': 'ApplicationCreateDTO',
            'ApplicationStatusCreateDTO': 'ApplicationStatusCreateDTO',
            'ApprovalCreateDTO': 'ApprovalCreateDTO',
            'ApprovalStatusCreateDTO': 'ApprovalStatusCreateDTO',
            'CandidateCertificationCreateDTO': 'CandidateCertificationCreateDTO',
            'CandidateDocumentCreateDTO': 'CandidateDocumentCreateDTO',
            'CandidateEducationCreateDTO': 'CandidateEducationCreateDTO',
            'CandidateIdentityCreateDTO': 'CandidateIdentityCreateDTO',
            'CandidateReferenceCreateDTO': 'CandidateReferenceCreateDTO',
            'CandidateSkillCreateDTO': 'CandidateSkillCreateDTO',
            'CandidateWorkHistoryCreateDTO': 'CandidateWorkHistoryCreateDTO',
            'DepartmentCreateDTO': 'DepartmentCreateDTO',
            'DepartmentStatusCreateDTO': 'DepartmentStatusCreateDTO',
            'InterviewCreateDTO': 'InterviewCreateDTO',
            'InterviewFeedbackCreateDTO': 'InterviewFeedbackCreateDTO',
            'InterviewStatusCreateDTO': 'InterviewStatusCreateDTO',
            'JobRequisitionCreateDTO': 'JobRequisitionCreateDTO',
            'JobRequisitionStatusCreateDTO': 'JobRequisitionStatusCreateDTO',
            'OfferCreateDTO': 'OfferCreateDTO',
            'OfferStatusCreateDTO': 'OfferStatusCreateDTO',
            'OnboardingCreateDTO': 'OnboardingCreateDTO',
            'OnboardingStatusCreateDTO': 'OnboardingStatusCreateDTO',
            'OrganizationCreateDTO': 'OrganizationCreateDTO',
            'OrganizationStatusCreateDTO': 'OrganizationStatusCreateDTO',
            'PipelineStageCreateDTO': 'PipelineStageCreateDTO',
            'PositionCreateDTO': 'PositionCreateDTO',
            'PositionStatusCreateDTO': 'PositionStatusCreateDTO',
            'SkillCreateDTO': 'SkillCreateDTO',
            'TenantCreateDTO': 'TenantCreateDTO',
            'UserCreateDTO': 'UserCreateDTO',
            'UserRoleCreateDTO': 'UserRoleCreateDTO',
            'UserStatusCreateDTO': 'UserStatusCreateDTO',
            'UserTypeCreateDTO': 'UserTypeCreateDTO',
            'VendorCreateDTO': 'VendorCreateDTO',
            'VendorStatusCreateDTO': 'VendorStatusCreateDTO'
        }
        
        dto_class_name = resource_to_dto.get(resource_type.lower())
        
        print(f"[DEBUG] Resource type: {resource_type}, mapped to DTO: {dto_class_name}")
        print(f"[DEBUG] Available DTOs: {list(dto_fields.keys())}")
        
        if dto_class_name and dto_class_name in dto_fields:
            # Use actual DTO fields
            fields = dto_fields[dto_class_name]
            print(f"[BODY] Using DTO {dto_class_name} for {resource_type} with {len(fields)} fields")
            for field_name, field_type in fields.items():
                # Skip audit fields for create operations
                if endpoint['http_method'] == 'POST' and field_name in ['createdAt', 'updatedAt', 'createdBy', 'updatedBy', 'deletedAt', 'deletedBy']:
                    continue
                
                # Generate appropriate sample values based on field type
                if 'UUID' in field_type:
                    if 'Id' in field_name:
                        body[field_name] = f"{{{{{field_name}}}}}"
                    else:
                        body[field_name] = "{{uuid}}"
                elif 'String' in field_type:
                    if 'email' in field_name.lower():
                        body[field_name] = f"{{{{{field_name}}}}}"
                    elif 'name' in field_name.lower():
                        body[field_name] = f"{{{{{field_name}}}}}"
                    elif 'phone' in field_name.lower():
                        body[field_name] = f"{{{{{field_name}}}}}"
                    else:
                        body[field_name] = f"{{{{{field_name}}}}}"
                elif 'Integer' in field_type or 'int' in field_type:
                    if 'Id' in field_name:
                        body[field_name] = f"{{{{{field_name}}}}}"
                    else:
                        body[field_name] = 1
                elif 'BigDecimal' in field_type:
                    body[field_name] = 50000.00
                elif 'LocalDate' in field_type:
                    body[field_name] = "2024-01-15"
                elif 'Instant' in field_type:
                    body[field_name] = "2024-01-15T10:00:00Z"
                elif 'Boolean' in field_type:
                    body[field_name] = True
                else:
                    body[field_name] = f"{{{{{field_name}}}}}"
        else:
            # Try alternative DTO names
            alternative_dto_names = []
            if dto_class_name:
                # Try CreateDTO variant for POST operations
                if endpoint['http_method'] == 'POST':
                    create_dto_name = dto_class_name.replace('DTO', 'CreateDTO')
                    alternative_dto_names.append(create_dto_name)
                
                # Try without 'DTO' suffix
                simple_name = dto_class_name.replace('DTO', '')
                alternative_dto_names.append(simple_name)
                
                # Try with different casing
                alternative_dto_names.append(dto_class_name.lower())
                alternative_dto_names.append(dto_class_name.upper())
            
            # Try to find any matching DTO
            found_dto = None
            for alt_name in alternative_dto_names:
                if alt_name in dto_fields:
                    found_dto = alt_name
                    break
            
            if found_dto:
                print(f"[BODY] Found alternative DTO {found_dto} for {resource_type}")
                fields = dto_fields[found_dto]
                for field_name, field_type in fields.items():
                    # Skip audit fields for create operations
                    if endpoint['http_method'] == 'POST' and field_name in ['createdAt', 'updatedAt', 'createdBy', 'updatedBy', 'deletedAt', 'deletedBy']:
                        continue
                    
                    # Generate appropriate sample values based on field type
                    if 'UUID' in field_type:
                        if 'Id' in field_name:
                            body[field_name] = f"{{{{{field_name}}}}}"
                        else:
                            body[field_name] = "{{uuid}}"
                    elif 'String' in field_type:
                        if 'email' in field_name.lower():
                            body[field_name] = f"{{{{{field_name}}}}}"
                        elif 'name' in field_name.lower():
                            body[field_name] = f"{{{{{field_name}}}}}"
                        elif 'phone' in field_name.lower():
                            body[field_name] = f"{{{{{field_name}}}}}"
                        else:
                            body[field_name] = f"{{{{{field_name}}}}}"
                    elif 'Integer' in field_type or 'int' in field_type:
                        if 'Id' in field_name:
                            body[field_name] = f"{{{{{field_name}}}}}"
                        else:
                            body[field_name] = 1
                    elif 'BigDecimal' in field_type:
                        body[field_name] = 50000.00
                    elif 'LocalDate' in field_type:
                        body[field_name] = "2024-01-15"
                    elif 'Instant' in field_type:
                        body[field_name] = "2024-01-15T10:00:00Z"
                    elif 'Boolean' in field_type:
                        body[field_name] = True
                    else:
                        body[field_name] = f"{{{{{field_name}}}}}"
            else:
                print(f"[BODY] No DTO found for {resource_type}, using fallback")
                if dto_class_name:
                    print(f"[BODY] Expected DTO: {dto_class_name}, available DTOs: {list(dto_fields.keys())}")
                # Fallback to basic structure if DTO not found
                body = {
                    "id": "{{id}}",
                    "name": "{{name}}",
                    "description": "{{description}}",
                    "status": "Active"
                }
        
        req['request']['body'] = {
            'mode': 'raw',
            'raw': json.dumps(body, indent=2)
        }
    
    return req

def get_comprehensive_prerequest_script():
    """Get the comprehensive pre-request script with random data generation"""
    return [
        "// Set static tenantId and organizationId for all create requests",
        f"pm.collectionVariables.set('tenantId', '{FIXED_TENANT_ID}');",
        f"pm.collectionVariables.set('organizationId', '{FIXED_ORG_ID}');",
        "",
        "// Generate random data functions",
        "function uuidv4() {",
        "  return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {",
        "    var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);",
        "    return v.toString(16);",
        "  });",
        "}",
        "",
        "function randomEmail() {",
        "  const domains = ['gmail.com', 'yahoo.com', 'hotmail.com', 'outlook.com', 'example.com'];",
        "  const names = ['john', 'jane', 'mike', 'sarah', 'david', 'emma', 'alex', 'lisa', 'tom', 'anna'];",
        "  const randomName = names[Math.floor(Math.random() * names.length)];",
        "  const randomNumber = Math.floor(Math.random() * 10000);",
        "  const randomDomain = domains[Math.floor(Math.random() * domains.length)];",
        "  return `${randomName}${randomNumber}@${randomDomain}`;",
        "}",
        "",
        "function randomPhone() {",
        "  const areaCodes = ['212', '415', '310', '312', '404', '305', '702', '602', '713', '214'];",
        "  const randomAreaCode = areaCodes[Math.floor(Math.random() * areaCodes.length)];",
        "  const randomNumber = Math.floor(1000000 + Math.random() * 9000000);",
        "  return `+1-${randomAreaCode}-${randomNumber}`;",
        "}",
        "",
        "function randomName() {",
        "  const firstNames = ['John', 'Jane', 'Michael', 'Sarah', 'David', 'Emma', 'Alex', 'Lisa', 'Tom', 'Anna'];",
        "  const lastNames = ['Smith', 'Johnson', 'Williams', 'Brown', 'Jones', 'Garcia', 'Miller', 'Davis'];",
        "  return {",
        "    firstName: firstNames[Math.floor(Math.random() * firstNames.length)],",
        "    lastName: lastNames[Math.floor(Math.random() * lastNames.length)]",
        "  };",
        "}",
        "",
        "function randomCompanyName() {",
        "  const prefixes = ['Tech', 'Global', 'Innovative', 'Advanced', 'Smart', 'Digital'];",
        "  const suffixes = ['Solutions', 'Systems', 'Technologies', 'Corp', 'Inc', 'Labs'];",
        "  const randomPrefix = prefixes[Math.floor(Math.random() * prefixes.length)];",
        "  const randomSuffix = suffixes[Math.floor(Math.random() * suffixes.length)];",
        "  return `${randomPrefix} ${randomSuffix}`;",
        "}",
        "",
        "// Generate random candidate data",
        "const candidateName = randomName();",
        "pm.collectionVariables.set('candidateEmail', randomEmail());",
        "pm.collectionVariables.set('candidateFirstName', candidateName.firstName);",
        "pm.collectionVariables.set('candidateLastName', candidateName.lastName);",
        "pm.collectionVariables.set('candidatePhone', randomPhone());",
        "",
        "// Generate random vendor data",
        "pm.collectionVariables.set('vendorName', randomCompanyName());",
        "pm.collectionVariables.set('vendorEmail', randomEmail());",
        "pm.collectionVariables.set('vendorPhone', randomPhone());",
        "",
        "// Generate random organization data",
        "pm.collectionVariables.set('organizationName', randomCompanyName());",
        "pm.collectionVariables.set('organizationEmail', randomEmail());",
        "pm.collectionVariables.set('organizationPhone', randomPhone());",
        "",
        "// Generate random vendorId for candidate creation",
        "pm.collectionVariables.set('vendorId', uuidv4())"
    ]

def get_comprehensive_test_script():
    """Get the comprehensive test script"""
    return [
        "// Global error handling and response validation",
        "pm.test('Response time is less than 5000ms', function () {",
        "  pm.expect(pm.response.responseTime).to.be.below(5000);",
        "});",
        "",
        "pm.test('Response has valid status code', function () {",
        "  pm.expect(pm.response.code).to.be.oneOf([200, 201, 204, 400, 401, 403, 404, 500]);",
        "});",
        "",
        "// Log response for debugging",
        "if (pm.response.code >= 400) {",
        "  console.log('Error Response:', pm.response.text());",
        "}",
        "",
        "// Validate JSON response structure",
        "if (pm.response.code < 400 && pm.response.headers.get('Content-Type') && pm.response.headers.get('Content-Type').includes('application/json')) {",
        "  try {",
        "    const jsonResponse = pm.response.json();",
        "    pm.test('Response is valid JSON', function () {",
        "      pm.expect(jsonResponse).to.be.an('object');",
        "    });",
        "  } catch (e) {",
        "    pm.test('Response is valid JSON', function () {",
        "      pm.expect.fail('Response is not valid JSON');",
        "    });",
        "  }",
        "}"
    ]

def main():
    """Main function to generate Postman collection"""
    print("Starting Postman collection generation...")
    print("Script is running!")
    
    # Check if we're in the right directory
    current_dir = os.getcwd()
    print(f"Current directory: {current_dir}")
    
    # Check if service directories exist
    for service_dir in SERVICE_DIRS:
        service_path = os.path.join('..', service_dir)
        if os.path.exists(service_path):
            print(f"✓ Found service: {service_dir}")
        else:
            print(f"✗ Missing service: {service_dir}")
    
    # 1. Find all DTOs across all services and hcm-common
    dto_fields = {}
    search_dirs = SERVICE_DIRS + ['../hcm-common']
    # Also search in helpers subdirectories
    helper_dirs = []
    for search_dir in search_dirs:
        helper_path = os.path.join(search_dir, 'src/main/java/tech/stl/hcm/common/dto/helpers')
        if os.path.exists(helper_path):
            helper_dirs.append(helper_path)
    for search_dir in search_dirs:
        java_src_root = os.path.join(search_dir, 'src/main/java')
        if os.path.exists(java_src_root):
            print(f"Scanning DTOs in {search_dir}...")
            java_files_found = 0
            dto_files_found = 0
            for java_file in find_java_files(java_src_root):
                try:
                    with open(java_file, encoding='utf-8') as f:
                        content = f.read()
                    m = RE_CLASS.search(content)
                    if m:
                        class_name = m.group(1)
                        # Only process DTO classes
                        if 'DTO' in class_name or 'CreateDTO' in class_name:
                            fields = parse_class_fields(java_file)
                            if fields:
                                dto_fields[class_name] = fields
                                dto_files_found += 1
                                print(f"    Found DTO: {class_name} with {len(fields)} fields")
                        java_files_found += 1
                except Exception as e:
                    print(f"Error processing {java_file}: {e}")
            print(f"  Found {java_files_found} Java files, {dto_files_found} DTOs in {search_dir}")
        else:
            print(f"  Skipping {search_dir} - src/main/java not found")
    
    print(f"Found {len(dto_fields)} DTOs total")
    print("Available DTOs:", list(dto_fields.keys()))
    
    # Also scan helper DTOs
    for helper_dir in helper_dirs:
        print(f"Scanning helper DTOs in {helper_dir}...")
        helper_dto_count = 0
        for java_file in find_java_files(helper_dir):
            try:
                with open(java_file, encoding='utf-8') as f:
                    content = f.read()
                m = RE_CLASS.search(content)
                if m:
                    class_name = m.group(1)
                    if 'DTO' in class_name:
                        fields = parse_class_fields(java_file)
                        if fields:
                            dto_fields[class_name] = fields
                            helper_dto_count += 1
                            print(f"    Found helper DTO: {class_name} with {len(fields)} fields")
            except Exception as e:
                print(f"Error processing helper DTO {java_file}: {e}")
        print(f"  Found {helper_dto_count} helper DTOs")
    
    print(f"Total DTOs after helpers: {len(dto_fields)}")
    print("All available DTOs:", list(dto_fields.keys()))
    
    # 2. Find all controllers and endpoints
    all_endpoints = []
    total_endpoints = 0
    
    for service_dir in SERVICE_DIRS:
        java_src_root = os.path.join('..', service_dir, 'src/main/java')
        if os.path.exists(java_src_root):
            print(f"Scanning controllers in {service_dir}...")
            controllers_found = 0
            endpoints_found = 0
            
            for java_file in find_java_files(java_src_root):
                try:
                    with open(java_file, encoding='utf-8') as f:
                        content = f.read()
                    if RE_CONTROLLER.search(content):
                        controllers_found += 1
                        endpoints = parse_controller(java_file)
                        endpoints_found += len(endpoints)
                        all_endpoints.extend(endpoints)
                except Exception as e:
                    print(f"Error processing controller {java_file}: {e}")
            
            print(f"  Found {controllers_found} controllers with {endpoints_found} endpoints in {service_dir}")
        else:
            print(f"  Skipping {service_dir} - src/main/java not found")
    
    print(f"Found {len(all_endpoints)} total endpoints")
    
    if len(all_endpoints) == 0:
        print("No endpoints found! Check if the service directories exist and contain Java controllers.")
        return
    
    # 3. Group endpoints by path structure
    groups = group_endpoints_by_path(all_endpoints)
    
    # 4. Create hierarchical structure
    collection_items = create_hierarchical_structure(groups, dto_fields)
    
    # Count total endpoints in the structured collection
    total_structured_endpoints = 0
    for functional_group in collection_items:
        for item in functional_group['item']:
            if 'item' in item:
                total_structured_endpoints += len(item['item'])
            else:
                total_structured_endpoints += 1
    
    print(f"Structured collection has {total_structured_endpoints} endpoints")
    
    # 5. Build Postman collection with comprehensive structure
    collection = {
        'info': {
            '_postman_id': 'hcm-generated-collection-uuid',
            'name': COLLECTION_NAME,
            'schema': 'https://schema.getpostman.com/json/collection/v2.1.0/collection.json',
            'description': 'Generated from Java Spring Boot controllers with hierarchical grouping and comprehensive scripts.'
        },
        'variable': [
            {'key': 'baseUrl', 'value': 'http://localhost:9112'},
            {'key': 'authToken', 'value': ''},
            {'key': 'candidateId', 'value': ''},
            {'key': 'vendorId', 'value': ''},
            {'key': 'tenantId', 'value': ''},
            {'key': 'organizationId', 'value': ''},
            {'key': 'applicationId', 'value': ''},
            {'key': 'jobRequisitionId', 'value': ''},
            {'key': 'positionId', 'value': ''},
            {'key': 'positionStatusId', 'value': ''},
            {'key': 'applicationStatusId', 'value': ''},
            {'key': 'candidateEmail', 'value': ''},
            {'key': 'candidateFirstName', 'value': ''},
            {'key': 'candidateLastName', 'value': ''},
            {'key': 'candidatePhone', 'value': ''},
            {'key': 'vendorName', 'value': ''},
            {'key': 'vendorEmail', 'value': ''},
            {'key': 'vendorPhone', 'value': ''},
            {'key': 'organizationName', 'value': ''},
            {'key': 'organizationEmail', 'value': ''},
            {'key': 'organizationPhone', 'value': ''},
            {'key': 'positionTitle', 'value': ''},
            {'key': 'tenantName', 'value': ''},
            {'key': 'tenantDomain', 'value': ''},
            {'key': 'tenantContactEmail', 'value': ''},
            {'key': 'tenantContactPhone', 'value': ''},
            {'key': 'id', 'value': '1'},
            {'key': 'name', 'value': 'Sample Name'},
            {'key': 'description', 'value': 'Sample Description'},
        ],
        'event': [
            {
                'listen': 'prerequest',
                'script': {
                    'type': 'text/javascript',
                    'exec': get_comprehensive_prerequest_script()
                }
            },
            {
                'listen': 'prerequest',
                'script': {
                    'type': 'text/javascript',
                    'exec': [
                        "// Add authentication header if token is available",
                        "if (pm.collectionVariables.get('authToken') && pm.collectionVariables.get('authToken') !== '') {",
                        "  pm.request.headers.add({",
                        "    key: 'Authorization',",
                        "    value: 'Bearer ' + pm.collectionVariables.get('authToken')",
                        "  });",
                        "}"
                    ]
                }
            },
            {
                'listen': 'test',
                'script': {
                    'type': 'text/javascript',
                    'exec': get_comprehensive_test_script()
                }
            }
        ],
        'item': collection_items
    }
    
    # Write the collection to file
    with open(OUTPUT_FILE, 'w', encoding='utf-8') as f:
        json.dump(collection, f, indent=2)
    
    print(f'Postman collection written to {OUTPUT_FILE}')
    print(f'Total endpoints generated: {total_structured_endpoints}')
    print(f'Collection structure: {len(collection_items)} functional groups')

if __name__ == '__main__':
    main() 