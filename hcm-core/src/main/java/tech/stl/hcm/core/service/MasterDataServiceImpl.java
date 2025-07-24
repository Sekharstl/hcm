package tech.stl.hcm.core.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tech.stl.hcm.common.db.repositories.*;
import tech.stl.hcm.common.dto.*;
import tech.stl.hcm.common.mapper.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MasterDataServiceImpl implements MasterDataService {

    private final DocumentTypeRepository documentTypeRepository;
    private final IdTypeRepository idTypeRepository;
    private final CountryRepository countryRepository;
    private final StateRepository stateRepository;
    private final SkillRepository skillRepository;
    private final DepartmentRepository departmentRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserTypeRepository userTypeRepository;
    private final VendorStatusRepository vendorStatusRepository;
    
    // Mappers (only for instance methods)
    private final DocumentTypeMapper documentTypeMapper;
    private final IdTypeMapper idTypeMapper;
    private final CountryMapper countryMapper;
    private final StateMapper stateMapper;

    // Document Types
    @Override
    public List<DocumentTypeDTO> getAllDocumentTypes() {
        try {
            return documentTypeRepository.findAll().stream()
                    .map(documentTypeMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error fetching all document types", e);
            throw new RuntimeException("Failed to fetch document types", e);
        }
    }

    @Override
    public DocumentTypeDTO getDocumentTypeById(Integer id) {
        try {
            return documentTypeRepository.findById(id)
                    .map(documentTypeMapper::toDTO)
                    .orElseThrow(() -> new RuntimeException("Document type not found with id: " + id));
        } catch (Exception e) {
            log.error("Error fetching document type by id: {}", id, e);
            throw new RuntimeException("Failed to fetch document type", e);
        }
    }

    @Override
    public List<DocumentTypeDTO> searchDocumentTypes(String query, int limit) {
        try {
            return documentTypeRepository.findAll().stream()
                    .filter(dt -> dt.getTypeName().toLowerCase().contains(query.toLowerCase()))
                    .limit(limit)
                    .map(documentTypeMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error searching document types with query: {}", query, e);
            throw new RuntimeException("Failed to search document types", e);
        }
    }

    // ID Types
    @Override
    public List<IdTypeDTO> getAllIdTypes() {
        try {
            return idTypeRepository.findAll().stream()
                    .map(idTypeMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error fetching all ID types", e);
            throw new RuntimeException("Failed to fetch ID types", e);
        }
    }

    @Override
    public IdTypeDTO getIdTypeById(Integer id) {
        try {
            return idTypeRepository.findById(id)
                    .map(idTypeMapper::toDTO)
                    .orElseThrow(() -> new RuntimeException("ID type not found with id: " + id));
        } catch (Exception e) {
            log.error("Error fetching ID type by id: {}", id, e);
            throw new RuntimeException("Failed to fetch ID type", e);
        }
    }

    @Override
    public List<IdTypeDTO> searchIdTypes(String query, int limit) {
        try {
            return idTypeRepository.findAll().stream()
                    .filter(it -> it.getTypeName().toLowerCase().contains(query.toLowerCase()))
                    .limit(limit)
                    .map(idTypeMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error searching ID types with query: {}", query, e);
            throw new RuntimeException("Failed to search ID types", e);
        }
    }

    // Countries
    @Override
    public List<CountryDTO> getAllCountries() {
        try {
            return countryRepository.findAll().stream()
                    .map(countryMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error fetching all countries", e);
            throw new RuntimeException("Failed to fetch countries", e);
        }
    }

    @Override
    public CountryDTO getCountryById(Integer id) {
        try {
            return countryRepository.findById(id)
                    .map(countryMapper::toDTO)
                    .orElseThrow(() -> new RuntimeException("Country not found with id: " + id));
        } catch (Exception e) {
            log.error("Error fetching country by id: {}", id, e);
            throw new RuntimeException("Failed to fetch country", e);
        }
    }

    @Override
    public CountryDTO getCountryByCode(String code) {
        try {
            return countryRepository.findByCountryCode(code)
                    .map(countryMapper::toDTO)
                    .orElseThrow(() -> new RuntimeException("Country not found with code: " + code));
        } catch (Exception e) {
            log.error("Error fetching country by code: {}", code, e);
            throw new RuntimeException("Failed to fetch country", e);
        }
    }

    @Override
    public List<CountryDTO> searchCountries(String query, int limit) {
        try {
            return countryRepository.findByCountryNameContainingIgnoreCase(query).stream()
                    .limit(limit)
                    .map(countryMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error searching countries with query: {}", query, e);
            throw new RuntimeException("Failed to search countries", e);
        }
    }

    // States
    @Override
    public List<StateDTO> getAllStates() {
        try {
            return stateRepository.findAll().stream()
                    .map(stateMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error fetching all states", e);
            throw new RuntimeException("Failed to fetch states", e);
        }
    }

    @Override
    public StateDTO getStateById(Integer id) {
        try {
            return stateRepository.findById(id)
                    .map(stateMapper::toDTO)
                    .orElseThrow(() -> new RuntimeException("State not found with id: " + id));
        } catch (Exception e) {
            log.error("Error fetching state by id: {}", id, e);
            throw new RuntimeException("Failed to fetch state", e);
        }
    }

    @Override
    public List<StateDTO> getStatesByCountry(Integer countryId) {
        try {
            return stateRepository.findByCountryId(countryId).stream()
                    .map(stateMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error fetching states for country: {}", countryId, e);
            throw new RuntimeException("Failed to fetch states for country", e);
        }
    }

    @Override
    public List<StateDTO> getStatesByCountryCode(String countryCode) {
        try {
            return stateRepository.findByCountryCountryCode(countryCode).stream()
                    .map(stateMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error fetching states for country code: {}", countryCode, e);
            throw new RuntimeException("Failed to fetch states for country code", e);
        }
    }

    @Override
    public List<StateDTO> searchStates(String query, int limit) {
        try {
            return stateRepository.findByStateNameContainingIgnoreCase(query).stream()
                    .limit(limit)
                    .map(stateMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error searching states with query: {}", query, e);
            throw new RuntimeException("Failed to search states", e);
        }
    }

    @Override
    public List<StateDTO> searchStatesByCountry(Integer countryId, String query, int limit) {
        try {
            return stateRepository.findByCountryId(countryId).stream()
                    .filter(s -> s.getStateName().toLowerCase().contains(query.toLowerCase()))
                    .limit(limit)
                    .map(stateMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error searching states for country {} with query: {}", countryId, query, e);
            throw new RuntimeException("Failed to search states for country", e);
        }
    }

    @Override
    public List<StateDTO> searchStatesByCountryCode(String countryCode, String query, int limit) {
        try {
            return stateRepository.findByCountryCountryCode(countryCode).stream()
                    .filter(s -> s.getStateName().toLowerCase().contains(query.toLowerCase()))
                    .limit(limit)
                    .map(stateMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error searching states for country code {} with query: {}", countryCode, query, e);
            throw new RuntimeException("Failed to search states for country code", e);
        }
    }

    // Skills
    @Override
    public List<SkillDTO> getAllSkills() {
        try {
            return skillRepository.findAll().stream()
                    .map(SkillMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error fetching all skills", e);
            throw new RuntimeException("Failed to fetch skills", e);
        }
    }

    @Override
    public SkillDTO getSkillById(Integer id) {
        try {
            return skillRepository.findById(id)
                    .map(SkillMapper::toDTO)
                    .orElseThrow(() -> new RuntimeException("Skill not found with id: " + id));
        } catch (Exception e) {
            log.error("Error fetching skill by id: {}", id, e);
            throw new RuntimeException("Failed to fetch skill", e);
        }
    }

    @Override
    public List<SkillDTO> searchSkills(String query, int limit) {
        try {
            return skillRepository.findAll().stream()
                    .filter(s -> s.getSkillName().toLowerCase().contains(query.toLowerCase()))
                    .limit(limit)
                    .map(SkillMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error searching skills with query: {}", query, e);
            throw new RuntimeException("Failed to search skills", e);
        }
    }

    @Override
    public List<SkillDTO> searchSkillsByCategory(String category, String query, int limit) {
        try {
            return skillRepository.findAll().stream()
                    .filter(s -> s.getSkillCategory() != null && 
                               s.getSkillCategory().toLowerCase().contains(category.toLowerCase()) &&
                               s.getSkillName().toLowerCase().contains(query.toLowerCase()))
                    .limit(limit)
                    .map(SkillMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error searching skills for category {} with query: {}", category, query, e);
            throw new RuntimeException("Failed to search skills by category", e);
        }
    }

    // Departments
    @Override
    public List<DepartmentDTO> getAllDepartments() {
        try {
            return departmentRepository.findAll().stream()
                    .map(DepartmentMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error fetching all departments", e);
            throw new RuntimeException("Failed to fetch departments", e);
        }
    }

    @Override
    public DepartmentDTO getDepartmentById(Integer id) {
        try {
            return departmentRepository.findById(id)
                    .map(DepartmentMapper::toDTO)
                    .orElseThrow(() -> new RuntimeException("Department not found with id: " + id));
        } catch (Exception e) {
            log.error("Error fetching department by id: {}", id, e);
            throw new RuntimeException("Failed to fetch department", e);
        }
    }

    @Override
    public List<DepartmentDTO> searchDepartments(String query, int limit) {
        try {
            return departmentRepository.findAll().stream()
                    .filter(d -> d.getName().toLowerCase().contains(query.toLowerCase()))
                    .limit(limit)
                    .map(DepartmentMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error searching departments with query: {}", query, e);
            throw new RuntimeException("Failed to search departments", e);
        }
    }

    // User Roles
    @Override
    public List<UserRoleDTO> getAllUserRoles() {
        try {
            return userRoleRepository.findAll().stream()
                    .map(UserRoleMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error fetching all user roles", e);
            throw new RuntimeException("Failed to fetch user roles", e);
        }
    }

    @Override
    public UserRoleDTO getUserRoleById(Integer id) {
        try {
            return userRoleRepository.findById(id)
                    .map(UserRoleMapper::toDTO)
                    .orElseThrow(() -> new RuntimeException("User role not found with id: " + id));
        } catch (Exception e) {
            log.error("Error fetching user role by id: {}", id, e);
            throw new RuntimeException("Failed to fetch user role", e);
        }
    }

    @Override
    public List<UserRoleDTO> searchUserRoles(String query, int limit) {
        try {
            return userRoleRepository.findAll().stream()
                    .filter(ur -> ur.getName().toLowerCase().contains(query.toLowerCase()))
                    .limit(limit)
                    .map(UserRoleMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error searching user roles with query: {}", query, e);
            throw new RuntimeException("Failed to search user roles", e);
        }
    }

    // User Types
    @Override
    public List<UserTypeDTO> getAllUserTypes() {
        try {
            return userTypeRepository.findAll().stream()
                    .map(UserTypeMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error fetching all user types", e);
            throw new RuntimeException("Failed to fetch user types", e);
        }
    }

    @Override
    public UserTypeDTO getUserTypeById(Integer id) {
        try {
            return userTypeRepository.findById(id)
                    .map(UserTypeMapper::toDTO)
                    .orElseThrow(() -> new RuntimeException("User type not found with id: " + id));
        } catch (Exception e) {
            log.error("Error fetching user type by id: {}", id, e);
            throw new RuntimeException("Failed to fetch user type", e);
        }
    }

    @Override
    public List<UserTypeDTO> searchUserTypes(String query, int limit) {
        try {
            return userTypeRepository.findAll().stream()
                    .filter(ut -> ut.getName().toLowerCase().contains(query.toLowerCase()))
                    .limit(limit)
                    .map(UserTypeMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error searching user types with query: {}", query, e);
            throw new RuntimeException("Failed to search user types", e);
        }
    }

    // Vendor Status
    @Override
    public List<VendorStatusDTO> getAllVendorStatuses() {
        try {
            return vendorStatusRepository.findAll().stream()
                    .map(VendorStatusMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error fetching all vendor statuses", e);
            throw new RuntimeException("Failed to fetch vendor statuses", e);
        }
    }

    @Override
    public VendorStatusDTO getVendorStatusById(Integer id) {
        try {
            return vendorStatusRepository.findById(id)
                    .map(VendorStatusMapper::toDTO)
                    .orElseThrow(() -> new RuntimeException("Vendor status not found with id: " + id));
        } catch (Exception e) {
            log.error("Error fetching vendor status by id: {}", id, e);
            throw new RuntimeException("Failed to fetch vendor status", e);
        }
    }

    @Override
    public List<VendorStatusDTO> searchVendorStatuses(String query, int limit) {
        try {
            return vendorStatusRepository.findAll().stream()
                    .filter(vs -> vs.getName().toLowerCase().contains(query.toLowerCase()))
                    .limit(limit)
                    .map(VendorStatusMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error searching vendor statuses with query: {}", query, e);
            throw new RuntimeException("Failed to search vendor statuses", e);
        }
    }
} 