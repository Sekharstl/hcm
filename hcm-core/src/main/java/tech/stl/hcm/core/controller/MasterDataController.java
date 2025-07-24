package tech.stl.hcm.core.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.stl.hcm.common.dto.*;
import tech.stl.hcm.core.service.MasterDataService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/master-data")
@RequiredArgsConstructor
public class MasterDataController {

    private final MasterDataService masterDataService;

    // Document Types
    @GetMapping("/document-types")
    public ResponseEntity<List<DocumentTypeDTO>> getAllDocumentTypes() {
        try {
            List<DocumentTypeDTO> documentTypes = masterDataService.getAllDocumentTypes();
            return ResponseEntity.ok(documentTypes);
        } catch (Exception e) {
            log.error("Error fetching document types", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/document-types/{id}")
    public ResponseEntity<DocumentTypeDTO> getDocumentTypeById(@PathVariable Integer id) {
        try {
            DocumentTypeDTO documentType = masterDataService.getDocumentTypeById(id);
            return ResponseEntity.ok(documentType);
        } catch (Exception e) {
            log.error("Error fetching document type: {}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // Document Types Autocomplete
    @GetMapping("/document-types/search")
    public ResponseEntity<List<DocumentTypeDTO>> searchDocumentTypes(
            @RequestParam String query,
            @RequestParam(defaultValue = "10") int limit) {
        try {
            List<DocumentTypeDTO> documentTypes = masterDataService.searchDocumentTypes(query, limit);
            return ResponseEntity.ok(documentTypes);
        } catch (Exception e) {
            log.error("Error searching document types with query: {}", query, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // ID Types
    @GetMapping("/id-types")
    public ResponseEntity<List<IdTypeDTO>> getAllIdTypes() {
        try {
            List<IdTypeDTO> idTypes = masterDataService.getAllIdTypes();
            return ResponseEntity.ok(idTypes);
        } catch (Exception e) {
            log.error("Error fetching ID types", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/id-types/{id}")
    public ResponseEntity<IdTypeDTO> getIdTypeById(@PathVariable Integer id) {
        try {
            IdTypeDTO idType = masterDataService.getIdTypeById(id);
            return ResponseEntity.ok(idType);
        } catch (Exception e) {
            log.error("Error fetching ID type: {}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // ID Types Autocomplete
    @GetMapping("/id-types/search")
    public ResponseEntity<List<IdTypeDTO>> searchIdTypes(
            @RequestParam String query,
            @RequestParam(defaultValue = "10") int limit) {
        try {
            List<IdTypeDTO> idTypes = masterDataService.searchIdTypes(query, limit);
            return ResponseEntity.ok(idTypes);
        } catch (Exception e) {
            log.error("Error searching ID types with query: {}", query, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // Countries
    @GetMapping("/countries")
    public ResponseEntity<List<CountryDTO>> getAllCountries() {
        try {
            List<CountryDTO> countries = masterDataService.getAllCountries();
            return ResponseEntity.ok(countries);
        } catch (Exception e) {
            log.error("Error fetching countries", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/countries/{id}")
    public ResponseEntity<CountryDTO> getCountryById(@PathVariable Integer id) {
        try {
            CountryDTO country = masterDataService.getCountryById(id);
            return ResponseEntity.ok(country);
        } catch (Exception e) {
            log.error("Error fetching country: {}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/countries/code/{code}")
    public ResponseEntity<CountryDTO> getCountryByCode(@PathVariable String code) {
        try {
            CountryDTO country = masterDataService.getCountryByCode(code);
            return ResponseEntity.ok(country);
        } catch (Exception e) {
            log.error("Error fetching country by code: {}", code, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // Countries Autocomplete
    @GetMapping("/countries/search")
    public ResponseEntity<List<CountryDTO>> searchCountries(
            @RequestParam String query,
            @RequestParam(defaultValue = "10") int limit) {
        try {
            List<CountryDTO> countries = masterDataService.searchCountries(query, limit);
            return ResponseEntity.ok(countries);
        } catch (Exception e) {
            log.error("Error searching countries with query: {}", query, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // States
    @GetMapping("/states")
    public ResponseEntity<List<StateDTO>> getAllStates() {
        try {
            List<StateDTO> states = masterDataService.getAllStates();
            return ResponseEntity.ok(states);
        } catch (Exception e) {
            log.error("Error fetching states", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/states/{id}")
    public ResponseEntity<StateDTO> getStateById(@PathVariable Integer id) {
        try {
            StateDTO state = masterDataService.getStateById(id);
            return ResponseEntity.ok(state);
        } catch (Exception e) {
            log.error("Error fetching state: {}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/states/country/{countryId}")
    public ResponseEntity<List<StateDTO>> getStatesByCountry(@PathVariable Integer countryId) {
        try {
            List<StateDTO> states = masterDataService.getStatesByCountry(countryId);
            return ResponseEntity.ok(states);
        } catch (Exception e) {
            log.error("Error fetching states for country: {}", countryId, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/states/country-code/{countryCode}")
    public ResponseEntity<List<StateDTO>> getStatesByCountryCode(@PathVariable String countryCode) {
        try {
            List<StateDTO> states = masterDataService.getStatesByCountryCode(countryCode);
            return ResponseEntity.ok(states);
        } catch (Exception e) {
            log.error("Error fetching states for country code: {}", countryCode, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // States Autocomplete
    @GetMapping("/states/search")
    public ResponseEntity<List<StateDTO>> searchStates(
            @RequestParam String query,
            @RequestParam(defaultValue = "10") int limit) {
        try {
            List<StateDTO> states = masterDataService.searchStates(query, limit);
            return ResponseEntity.ok(states);
        } catch (Exception e) {
            log.error("Error searching states with query: {}", query, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // States Autocomplete by Country
    @GetMapping("/states/country/{countryId}/search")
    public ResponseEntity<List<StateDTO>> searchStatesByCountry(
            @PathVariable Integer countryId,
            @RequestParam String query,
            @RequestParam(defaultValue = "10") int limit) {
        try {
            List<StateDTO> states = masterDataService.searchStatesByCountry(countryId, query, limit);
            return ResponseEntity.ok(states);
        } catch (Exception e) {
            log.error("Error searching states for country {} with query: {}", countryId, query, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // States Autocomplete by Country Code
    @GetMapping("/states/country-code/{countryCode}/search")
    public ResponseEntity<List<StateDTO>> searchStatesByCountryCode(
            @PathVariable String countryCode,
            @RequestParam String query,
            @RequestParam(defaultValue = "10") int limit) {
        try {
            List<StateDTO> states = masterDataService.searchStatesByCountryCode(countryCode, query, limit);
            return ResponseEntity.ok(states);
        } catch (Exception e) {
            log.error("Error searching states for country code {} with query: {}", countryCode, query, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // Skills
    @GetMapping("/skills")
    public ResponseEntity<List<SkillDTO>> getAllSkills() {
        try {
            List<SkillDTO> skills = masterDataService.getAllSkills();
            return ResponseEntity.ok(skills);
        } catch (Exception e) {
            log.error("Error fetching skills", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/skills/{id}")
    public ResponseEntity<SkillDTO> getSkillById(@PathVariable Integer id) {
        try {
            SkillDTO skill = masterDataService.getSkillById(id);
            return ResponseEntity.ok(skill);
        } catch (Exception e) {
            log.error("Error fetching skill: {}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // Skills Autocomplete
    @GetMapping("/skills/search")
    public ResponseEntity<List<SkillDTO>> searchSkills(
            @RequestParam String query,
            @RequestParam(defaultValue = "10") int limit) {
        try {
            List<SkillDTO> skills = masterDataService.searchSkills(query, limit);
            return ResponseEntity.ok(skills);
        } catch (Exception e) {
            log.error("Error searching skills with query: {}", query, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // Skills Autocomplete by Category
    @GetMapping("/skills/category/{category}/search")
    public ResponseEntity<List<SkillDTO>> searchSkillsByCategory(
            @PathVariable String category,
            @RequestParam String query,
            @RequestParam(defaultValue = "10") int limit) {
        try {
            List<SkillDTO> skills = masterDataService.searchSkillsByCategory(category, query, limit);
            return ResponseEntity.ok(skills);
        } catch (Exception e) {
            log.error("Error searching skills for category {} with query: {}", category, query, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // Departments
    @GetMapping("/departments")
    public ResponseEntity<List<DepartmentDTO>> getAllDepartments() {
        try {
            List<DepartmentDTO> departments = masterDataService.getAllDepartments();
            return ResponseEntity.ok(departments);
        } catch (Exception e) {
            log.error("Error fetching departments", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/departments/{id}")
    public ResponseEntity<DepartmentDTO> getDepartmentById(@PathVariable Integer id) {
        try {
            DepartmentDTO department = masterDataService.getDepartmentById(id);
            return ResponseEntity.ok(department);
        } catch (Exception e) {
            log.error("Error fetching department: {}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // Departments Autocomplete
    @GetMapping("/departments/search")
    public ResponseEntity<List<DepartmentDTO>> searchDepartments(
            @RequestParam String query,
            @RequestParam(defaultValue = "10") int limit) {
        try {
            List<DepartmentDTO> departments = masterDataService.searchDepartments(query, limit);
            return ResponseEntity.ok(departments);
        } catch (Exception e) {
            log.error("Error searching departments with query: {}", query, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // User Roles
    @GetMapping("/user-roles")
    public ResponseEntity<List<UserRoleDTO>> getAllUserRoles() {
        try {
            List<UserRoleDTO> userRoles = masterDataService.getAllUserRoles();
            return ResponseEntity.ok(userRoles);
        } catch (Exception e) {
            log.error("Error fetching user roles", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/user-roles/{id}")
    public ResponseEntity<UserRoleDTO> getUserRoleById(@PathVariable Integer id) {
        try {
            UserRoleDTO userRole = masterDataService.getUserRoleById(id);
            return ResponseEntity.ok(userRole);
        } catch (Exception e) {
            log.error("Error fetching user role: {}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // User Roles Autocomplete
    @GetMapping("/user-roles/search")
    public ResponseEntity<List<UserRoleDTO>> searchUserRoles(
            @RequestParam String query,
            @RequestParam(defaultValue = "10") int limit) {
        try {
            List<UserRoleDTO> userRoles = masterDataService.searchUserRoles(query, limit);
            return ResponseEntity.ok(userRoles);
        } catch (Exception e) {
            log.error("Error searching user roles with query: {}", query, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // User Types
    @GetMapping("/user-types")
    public ResponseEntity<List<UserTypeDTO>> getAllUserTypes() {
        try {
            List<UserTypeDTO> userTypes = masterDataService.getAllUserTypes();
            return ResponseEntity.ok(userTypes);
        } catch (Exception e) {
            log.error("Error fetching user types", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/user-types/{id}")
    public ResponseEntity<UserTypeDTO> getUserTypeById(@PathVariable Integer id) {
        try {
            UserTypeDTO userType = masterDataService.getUserTypeById(id);
            return ResponseEntity.ok(userType);
        } catch (Exception e) {
            log.error("Error fetching user type: {}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // User Types Autocomplete
    @GetMapping("/user-types/search")
    public ResponseEntity<List<UserTypeDTO>> searchUserTypes(
            @RequestParam String query,
            @RequestParam(defaultValue = "10") int limit) {
        try {
            List<UserTypeDTO> userTypes = masterDataService.searchUserTypes(query, limit);
            return ResponseEntity.ok(userTypes);
        } catch (Exception e) {
            log.error("Error searching user types with query: {}", query, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // Vendor Status
    @GetMapping("/vendor-statuses")
    public ResponseEntity<List<VendorStatusDTO>> getAllVendorStatuses() {
        try {
            List<VendorStatusDTO> vendorStatuses = masterDataService.getAllVendorStatuses();
            return ResponseEntity.ok(vendorStatuses);
        } catch (Exception e) {
            log.error("Error fetching vendor statuses", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/vendor-statuses/{id}")
    public ResponseEntity<VendorStatusDTO> getVendorStatusById(@PathVariable Integer id) {
        try {
            VendorStatusDTO vendorStatus = masterDataService.getVendorStatusById(id);
            return ResponseEntity.ok(vendorStatus);
        } catch (Exception e) {
            log.error("Error fetching vendor status: {}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // Vendor Status Autocomplete
    @GetMapping("/vendor-statuses/search")
    public ResponseEntity<List<VendorStatusDTO>> searchVendorStatuses(
            @RequestParam String query,
            @RequestParam(defaultValue = "10") int limit) {
        try {
            List<VendorStatusDTO> vendorStatuses = masterDataService.searchVendorStatuses(query, limit);
            return ResponseEntity.ok(vendorStatuses);
        } catch (Exception e) {
            log.error("Error searching vendor statuses with query: {}", query, e);
            return ResponseEntity.internalServerError().build();
        }
    }
} 