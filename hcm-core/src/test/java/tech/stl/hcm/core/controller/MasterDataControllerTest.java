package tech.stl.hcm.core.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import tech.stl.hcm.core.service.MasterDataService;
import tech.stl.hcm.common.dto.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MasterDataControllerTest {

    @Mock
    private MasterDataService masterDataService;

    @InjectMocks
    private MasterDataController masterDataController;

    // Document Types Tests
    @Test
    void getAllDocumentTypes_shouldReturnDocumentTypes() {
        // Given
        List<DocumentTypeDTO> expectedDocumentTypes = Arrays.asList(
            DocumentTypeDTO.builder().documentTypeId(1).typeName("Passport").description("Travel document").build(),
            DocumentTypeDTO.builder().documentTypeId(2).typeName("Driver License").description("Driving permit").build()
        );
        when(masterDataService.getAllDocumentTypes()).thenReturn(expectedDocumentTypes);

        // When
        ResponseEntity<List<DocumentTypeDTO>> response = masterDataController.getAllDocumentTypes();

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedDocumentTypes, response.getBody());
        verify(masterDataService).getAllDocumentTypes();
    }

    @Test
    void getDocumentTypeById_shouldReturnDocumentType() {
        // Given
        Integer id = 1;
        DocumentTypeDTO expectedDocumentType = DocumentTypeDTO.builder().documentTypeId(id).typeName("Passport").description("Travel document").build();
        when(masterDataService.getDocumentTypeById(id)).thenReturn(expectedDocumentType);

        // When
        ResponseEntity<DocumentTypeDTO> response = masterDataController.getDocumentTypeById(id);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedDocumentType, response.getBody());
        verify(masterDataService).getDocumentTypeById(id);
    }

    @Test
    void searchDocumentTypes_shouldReturnFilteredResults() {
        // Given
        String query = "pass";
        int limit = 5;
        List<DocumentTypeDTO> expectedResults = Arrays.asList(
            DocumentTypeDTO.builder().documentTypeId(1).typeName("Passport").description("Travel document").build()
        );
        when(masterDataService.searchDocumentTypes(query, limit)).thenReturn(expectedResults);

        // When
        ResponseEntity<List<DocumentTypeDTO>> response = masterDataController.searchDocumentTypes(query, limit);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResults, response.getBody());
        verify(masterDataService).searchDocumentTypes(query, limit);
    }

    // ID Types Tests
    @Test
    void getAllIdTypes_shouldReturnIdTypes() {
        // Given
        List<IdTypeDTO> expectedIdTypes = Arrays.asList(
            IdTypeDTO.builder().idTypeId(1).typeName("National ID").description("National identification").build(),
            IdTypeDTO.builder().idTypeId(2).typeName("Social Security").description("Social security number").build()
        );
        when(masterDataService.getAllIdTypes()).thenReturn(expectedIdTypes);

        // When
        ResponseEntity<List<IdTypeDTO>> response = masterDataController.getAllIdTypes();

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedIdTypes, response.getBody());
        verify(masterDataService).getAllIdTypes();
    }

    @Test
    void getIdTypeById_shouldReturnIdType() {
        // Given
        Integer id = 1;
        IdTypeDTO expectedIdType = IdTypeDTO.builder().idTypeId(id).typeName("National ID").description("National identification").build();
        when(masterDataService.getIdTypeById(id)).thenReturn(expectedIdType);

        // When
        ResponseEntity<IdTypeDTO> response = masterDataController.getIdTypeById(id);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedIdType, response.getBody());
        verify(masterDataService).getIdTypeById(id);
    }

    @Test
    void searchIdTypes_shouldReturnFilteredResults() {
        // Given
        String query = "national";
        int limit = 5;
        List<IdTypeDTO> expectedResults = Arrays.asList(
            IdTypeDTO.builder().idTypeId(1).typeName("National ID").description("National identification").build()
        );
        when(masterDataService.searchIdTypes(query, limit)).thenReturn(expectedResults);

        // When
        ResponseEntity<List<IdTypeDTO>> response = masterDataController.searchIdTypes(query, limit);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResults, response.getBody());
        verify(masterDataService).searchIdTypes(query, limit);
    }

    // Countries Tests
    @Test
    void getAllCountries_shouldReturnCountries() {
        // Given
        List<CountryDTO> expectedCountries = Arrays.asList(
            CountryDTO.builder().countryId(1).countryCode("US").countryName("United States").phoneCode("USA").build(),
            CountryDTO.builder().countryId(2).countryCode("CA").countryName("Canada").phoneCode("CAN").build()
        );
        when(masterDataService.getAllCountries()).thenReturn(expectedCountries);

        // When
        ResponseEntity<List<CountryDTO>> response = masterDataController.getAllCountries();

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedCountries, response.getBody());
        verify(masterDataService).getAllCountries();
    }

    @Test
    void getCountryById_shouldReturnCountry() {
        // Given
        Integer id = 1;
        CountryDTO expectedCountry = CountryDTO.builder().countryId(id).countryCode("US").countryName("United States").phoneCode("USA").build();
        when(masterDataService.getCountryById(id)).thenReturn(expectedCountry);

        // When
        ResponseEntity<CountryDTO> response = masterDataController.getCountryById(id);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedCountry, response.getBody());
        verify(masterDataService).getCountryById(id);
    }

    @Test
    void getCountryByCode_shouldReturnCountry() {
        // Given
        String code = "US";
        CountryDTO expectedCountry = CountryDTO.builder().countryId(1).countryCode(code).countryName("United States").phoneCode("USA").build();
        when(masterDataService.getCountryByCode(code)).thenReturn(expectedCountry);

        // When
        ResponseEntity<CountryDTO> response = masterDataController.getCountryByCode(code);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedCountry, response.getBody());
        verify(masterDataService).getCountryByCode(code);
    }

    @Test
    void searchCountries_shouldReturnFilteredResults() {
        // Given
        String query = "united";
        int limit = 5;
        List<CountryDTO> expectedResults = Arrays.asList(
            CountryDTO.builder().countryId(1).countryCode("US").countryName("United States").phoneCode("USA").build()
        );
        when(masterDataService.searchCountries(query, limit)).thenReturn(expectedResults);

        // When
        ResponseEntity<List<CountryDTO>> response = masterDataController.searchCountries(query, limit);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResults, response.getBody());
        verify(masterDataService).searchCountries(query, limit);
    }

    // States Tests
    @Test
    void getAllStates_shouldReturnStates() {
        // Given
        List<StateDTO> expectedStates = Arrays.asList(
            StateDTO.builder().stateId(1).stateCode("CA").stateName("California").countryId(1).build(),
            StateDTO.builder().stateId(2).stateCode("NY").stateName("New York").countryId(1).build()
        );
        when(masterDataService.getAllStates()).thenReturn(expectedStates);

        // When
        ResponseEntity<List<StateDTO>> response = masterDataController.getAllStates();

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedStates, response.getBody());
        verify(masterDataService).getAllStates();
    }

    @Test
    void getStateById_shouldReturnState() {
        // Given
        Integer id = 1;
        StateDTO expectedState = StateDTO.builder().stateId(id).stateCode("CA").stateName("California").countryId(1).build();
        when(masterDataService.getStateById(id)).thenReturn(expectedState);

        // When
        ResponseEntity<StateDTO> response = masterDataController.getStateById(id);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedState, response.getBody());
        verify(masterDataService).getStateById(id);
    }

    @Test
    void getStatesByCountry_shouldReturnStates() {
        // Given
        Integer countryId = 1;
        List<StateDTO> expectedStates = Arrays.asList(
            StateDTO.builder().stateId(1).stateCode("CA").stateName("California").countryId(countryId).build(),
            StateDTO.builder().stateId(2).stateCode("NY").stateName("New York").countryId(countryId).build()
        );
        when(masterDataService.getStatesByCountry(countryId)).thenReturn(expectedStates);

        // When
        ResponseEntity<List<StateDTO>> response = masterDataController.getStatesByCountry(countryId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedStates, response.getBody());
        verify(masterDataService).getStatesByCountry(countryId);
    }

    @Test
    void getStatesByCountryCode_shouldReturnStates() {
        // Given
        String countryCode = "US";
        List<StateDTO> expectedStates = Arrays.asList(
            StateDTO.builder().stateId(1).stateCode("CA").stateName("California").countryId(1).build(),
            StateDTO.builder().stateId(2).stateCode("NY").stateName("New York").countryId(1).build()
        );
        when(masterDataService.getStatesByCountryCode(countryCode)).thenReturn(expectedStates);

        // When
        ResponseEntity<List<StateDTO>> response = masterDataController.getStatesByCountryCode(countryCode);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedStates, response.getBody());
        verify(masterDataService).getStatesByCountryCode(countryCode);
    }

    @Test
    void searchStates_shouldReturnFilteredResults() {
        // Given
        String query = "california";
        int limit = 5;
        List<StateDTO> expectedResults = Arrays.asList(
            StateDTO.builder().stateId(1).stateCode("CA").stateName("California").countryId(1).build()
        );
        when(masterDataService.searchStates(query, limit)).thenReturn(expectedResults);

        // When
        ResponseEntity<List<StateDTO>> response = masterDataController.searchStates(query, limit);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResults, response.getBody());
        verify(masterDataService).searchStates(query, limit);
    }

    @Test
    void searchStatesByCountry_shouldReturnFilteredResults() {
        // Given
        Integer countryId = 1;
        String query = "california";
        int limit = 5;
        List<StateDTO> expectedResults = Arrays.asList(
            StateDTO.builder().stateId(1).stateCode("CA").stateName("California").countryId(countryId).build()
        );
        when(masterDataService.searchStatesByCountry(countryId, query, limit)).thenReturn(expectedResults);

        // When
        ResponseEntity<List<StateDTO>> response = masterDataController.searchStatesByCountry(countryId, query, limit);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResults, response.getBody());
        verify(masterDataService).searchStatesByCountry(countryId, query, limit);
    }

    @Test
    void searchStatesByCountryCode_shouldReturnFilteredResults() {
        // Given
        String countryCode = "US";
        String query = "california";
        int limit = 5;
        List<StateDTO> expectedResults = Arrays.asList(
            StateDTO.builder().stateId(1).stateCode("CA").stateName("California").countryId(1).build()
        );
        when(masterDataService.searchStatesByCountryCode(countryCode, query, limit)).thenReturn(expectedResults);

        // When
        ResponseEntity<List<StateDTO>> response = masterDataController.searchStatesByCountryCode(countryCode, query, limit);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResults, response.getBody());
        verify(masterDataService).searchStatesByCountryCode(countryCode, query, limit);
    }

    // Skills Tests
    @Test
    void getAllSkills_shouldReturnSkills() {
        // Given
        List<SkillDTO> expectedSkills = Arrays.asList(
            SkillDTO.builder().skillId(1).skillName("Java").skillCategory("Programming").description("Technical").build(),
            SkillDTO.builder().skillId(2).skillName("Python").skillCategory("Programming").description("Technical").build()
        );
        when(masterDataService.getAllSkills()).thenReturn(expectedSkills);

        // When
        ResponseEntity<List<SkillDTO>> response = masterDataController.getAllSkills();

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedSkills, response.getBody());
        verify(masterDataService).getAllSkills();
    }

    @Test
    void getSkillById_shouldReturnSkill() {
        // Given
        Integer id = 1;
        SkillDTO expectedSkill = SkillDTO.builder().skillId(id).skillName("Java").skillCategory("Programming").description("Technical").build();
        when(masterDataService.getSkillById(id)).thenReturn(expectedSkill);

        // When
        ResponseEntity<SkillDTO> response = masterDataController.getSkillById(id);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedSkill, response.getBody());
        verify(masterDataService).getSkillById(id);
    }

    @Test
    void searchSkills_shouldReturnFilteredResults() {
        // Given
        String query = "java";
        int limit = 5;
        List<SkillDTO> expectedResults = Arrays.asList(
            SkillDTO.builder().skillId(1).skillName("Java").skillCategory("Programming").description("Technical").build()
        );
        when(masterDataService.searchSkills(query, limit)).thenReturn(expectedResults);

        // When
        ResponseEntity<List<SkillDTO>> response = masterDataController.searchSkills(query, limit);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResults, response.getBody());
        verify(masterDataService).searchSkills(query, limit);
    }

    @Test
    void searchSkillsByCategory_shouldReturnFilteredResults() {
        // Given
        String category = "Technical";
        String query = "java";
        int limit = 5;
        List<SkillDTO> expectedResults = Arrays.asList(
            SkillDTO.builder().skillId(1).skillName("Java").skillCategory("Programming").description(category).build()
        );
        when(masterDataService.searchSkillsByCategory(category, query, limit)).thenReturn(expectedResults);

        // When
        ResponseEntity<List<SkillDTO>> response = masterDataController.searchSkillsByCategory(category, query, limit);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResults, response.getBody());
        verify(masterDataService).searchSkillsByCategory(category, query, limit);
    }

    // Departments Tests
    @Test
    void getAllDepartments_shouldReturnDepartments() {
        // Given
        List<DepartmentDTO> expectedDepartments = Arrays.asList(
            DepartmentDTO.builder().departmentId(1).name("Engineering").build(),
            DepartmentDTO.builder().departmentId(2).name("HR").build()
        );
        when(masterDataService.getAllDepartments()).thenReturn(expectedDepartments);

        // When
        ResponseEntity<List<DepartmentDTO>> response = masterDataController.getAllDepartments();

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedDepartments, response.getBody());
        verify(masterDataService).getAllDepartments();
    }

    @Test
    void getDepartmentById_shouldReturnDepartment() {
        // Given
        Integer id = 1;
        DepartmentDTO expectedDepartment = DepartmentDTO.builder().departmentId(id).name("Engineering").build();
        when(masterDataService.getDepartmentById(id)).thenReturn(expectedDepartment);

        // When
        ResponseEntity<DepartmentDTO> response = masterDataController.getDepartmentById(id);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedDepartment, response.getBody());
        verify(masterDataService).getDepartmentById(id);
    }

    @Test
    void searchDepartments_shouldReturnFilteredResults() {
        // Given
        String query = "engineering";
        int limit = 5;
        List<DepartmentDTO> expectedResults = Arrays.asList(
            DepartmentDTO.builder().departmentId(1).name("Engineering").build()
        );
        when(masterDataService.searchDepartments(query, limit)).thenReturn(expectedResults);

        // When
        ResponseEntity<List<DepartmentDTO>> response = masterDataController.searchDepartments(query, limit);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResults, response.getBody());
        verify(masterDataService).searchDepartments(query, limit);
    }

    // User Roles Tests
    @Test
    void getAllUserRoles_shouldReturnUserRoles() {
        // Given
        List<UserRoleDTO> expectedUserRoles = Arrays.asList(
            UserRoleDTO.builder().roleId(1).name("Admin").permissions("Administrator").build(),
            UserRoleDTO.builder().roleId(2).name("User").permissions("Regular user").build()
        );
        when(masterDataService.getAllUserRoles()).thenReturn(expectedUserRoles);

        // When
        ResponseEntity<List<UserRoleDTO>> response = masterDataController.getAllUserRoles();

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedUserRoles, response.getBody());
        verify(masterDataService).getAllUserRoles();
    }

    @Test
    void getUserRoleById_shouldReturnUserRole() {
        // Given
        Integer id = 1;
        UserRoleDTO expectedUserRole = UserRoleDTO.builder().roleId(id).name("Admin").permissions("Administrator").build();
        when(masterDataService.getUserRoleById(id)).thenReturn(expectedUserRole);

        // When
        ResponseEntity<UserRoleDTO> response = masterDataController.getUserRoleById(id);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedUserRole, response.getBody());
        verify(masterDataService).getUserRoleById(id);
    }

    @Test
    void searchUserRoles_shouldReturnFilteredResults() {
        // Given
        String query = "admin";
        int limit = 5;
        List<UserRoleDTO> expectedResults = Arrays.asList(
            UserRoleDTO.builder().roleId(1).name("Admin").permissions("Administrator").build()
        );
        when(masterDataService.searchUserRoles(query, limit)).thenReturn(expectedResults);

        // When
        ResponseEntity<List<UserRoleDTO>> response = masterDataController.searchUserRoles(query, limit);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResults, response.getBody());
        verify(masterDataService).searchUserRoles(query, limit);
    }

    // User Types Tests
    @Test
    void getAllUserTypes_shouldReturnUserTypes() {
        // Given
        List<UserTypeDTO> expectedUserTypes = Arrays.asList(
            UserTypeDTO.builder().typeId(1).name("Employee").description("Full-time employee").build(),
            UserTypeDTO.builder().typeId(2).name("Contractor").description("Contract worker").build()
        );
        when(masterDataService.getAllUserTypes()).thenReturn(expectedUserTypes);

        // When
        ResponseEntity<List<UserTypeDTO>> response = masterDataController.getAllUserTypes();

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedUserTypes, response.getBody());
        verify(masterDataService).getAllUserTypes();
    }

    @Test
    void getUserTypeById_shouldReturnUserType() {
        // Given
        Integer id = 1;
        UserTypeDTO expectedUserType = UserTypeDTO.builder().typeId(id).name("Employee").description("Full-time employee").build();
        when(masterDataService.getUserTypeById(id)).thenReturn(expectedUserType);

        // When
        ResponseEntity<UserTypeDTO> response = masterDataController.getUserTypeById(id);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedUserType, response.getBody());
        verify(masterDataService).getUserTypeById(id);
    }

    @Test
    void searchUserTypes_shouldReturnFilteredResults() {
        // Given
        String query = "employee";
        int limit = 5;
        List<UserTypeDTO> expectedResults = Arrays.asList(
            UserTypeDTO.builder().typeId(1).name("Employee").description("Full-time employee").build()
        );
        when(masterDataService.searchUserTypes(query, limit)).thenReturn(expectedResults);

        // When
        ResponseEntity<List<UserTypeDTO>> response = masterDataController.searchUserTypes(query, limit);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResults, response.getBody());
        verify(masterDataService).searchUserTypes(query, limit);
    }

    // Error Handling Tests
    @Test
    void getAllDocumentTypes_whenServiceThrowsException_shouldReturnInternalServerError() {
        // Given
        when(masterDataService.getAllDocumentTypes()).thenThrow(new RuntimeException("Database error"));

        // When
        ResponseEntity<List<DocumentTypeDTO>> response = masterDataController.getAllDocumentTypes();

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void getDocumentTypeById_whenServiceThrowsException_shouldReturnInternalServerError() {
        // Given
        Integer id = 1;
        when(masterDataService.getDocumentTypeById(id)).thenThrow(new RuntimeException("Not found"));

        // When
        ResponseEntity<DocumentTypeDTO> response = masterDataController.getDocumentTypeById(id);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void searchDocumentTypes_whenServiceThrowsException_shouldReturnInternalServerError() {
        // Given
        String query = "test";
        int limit = 10;
        when(masterDataService.searchDocumentTypes(query, limit)).thenThrow(new RuntimeException("Search error"));

        // When
        ResponseEntity<List<DocumentTypeDTO>> response = masterDataController.searchDocumentTypes(query, limit);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }
} 