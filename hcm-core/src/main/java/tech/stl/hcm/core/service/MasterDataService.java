package tech.stl.hcm.core.service;

import tech.stl.hcm.common.dto.*;

import java.util.List;

public interface MasterDataService {

    // Document Types
    List<DocumentTypeDTO> getAllDocumentTypes();
    DocumentTypeDTO getDocumentTypeById(Integer id);
    List<DocumentTypeDTO> searchDocumentTypes(String query, int limit);

    // ID Types
    List<IdTypeDTO> getAllIdTypes();
    IdTypeDTO getIdTypeById(Integer id);
    List<IdTypeDTO> searchIdTypes(String query, int limit);

    // Countries
    List<CountryDTO> getAllCountries();
    CountryDTO getCountryById(Integer id);
    CountryDTO getCountryByCode(String code);
    List<CountryDTO> searchCountries(String query, int limit);

    // States
    List<StateDTO> getAllStates();
    StateDTO getStateById(Integer id);
    List<StateDTO> getStatesByCountry(Integer countryId);
    List<StateDTO> getStatesByCountryCode(String countryCode);
    List<StateDTO> searchStates(String query, int limit);
    List<StateDTO> searchStatesByCountry(Integer countryId, String query, int limit);
    List<StateDTO> searchStatesByCountryCode(String countryCode, String query, int limit);

    // Skills
    List<SkillDTO> getAllSkills();
    SkillDTO getSkillById(Integer id);
    List<SkillDTO> searchSkills(String query, int limit);
    List<SkillDTO> searchSkillsByCategory(String category, String query, int limit);

    // Departments
    List<DepartmentDTO> getAllDepartments();
    DepartmentDTO getDepartmentById(Integer id);
    List<DepartmentDTO> searchDepartments(String query, int limit);

    // User Roles
    List<UserRoleDTO> getAllUserRoles();
    UserRoleDTO getUserRoleById(Integer id);
    List<UserRoleDTO> searchUserRoles(String query, int limit);

    // User Types
    List<UserTypeDTO> getAllUserTypes();
    UserTypeDTO getUserTypeById(Integer id);
    List<UserTypeDTO> searchUserTypes(String query, int limit);

    // Vendor Status
    List<VendorStatusDTO> getAllVendorStatuses();
    VendorStatusDTO getVendorStatusById(Integer id);
    List<VendorStatusDTO> searchVendorStatuses(String query, int limit);
} 