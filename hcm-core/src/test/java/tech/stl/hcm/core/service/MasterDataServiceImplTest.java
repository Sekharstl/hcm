package tech.stl.hcm.core.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.stl.hcm.common.db.entities.*;
import tech.stl.hcm.common.dto.*;
import tech.stl.hcm.common.db.repositories.*;
import tech.stl.hcm.common.mapper.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MasterDataServiceImplTest {

    @Mock
    private DocumentTypeRepository documentTypeRepository;

    @Mock
    private IdTypeRepository idTypeRepository;

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private StateRepository stateRepository;

    @Mock
    private SkillRepository skillRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private UserRoleRepository userRoleRepository;

    @Mock
    private UserTypeRepository userTypeRepository;

    // Mappers
    @Mock
    private DocumentTypeMapper documentTypeMapper;

    @Mock
    private IdTypeMapper idTypeMapper;

    @Mock
    private CountryMapper countryMapper;

    @Mock
    private StateMapper stateMapper;

    @InjectMocks
    private MasterDataServiceImpl masterDataService;

    private final UUID userId = UUID.randomUUID();
    private final Instant now = Instant.now();

    // Document Types Tests
    @Test
    void getAllDocumentTypes_shouldReturnAllDocumentTypes() {
        // Given
        DocumentType docType1 = DocumentType.builder()
            .documentTypeId(1)
            .typeName("Passport")
            .description("Government issued passport")
            .isRequired(true)
            .createdAt(now)
            .createdBy(userId)
            .updatedAt(now)
            .updatedBy(userId)
            .build();
        
        DocumentType docType2 = DocumentType.builder()
            .documentTypeId(2)
            .typeName("Driver License")
            .description("Driver's license")
            .isRequired(true)
            .createdAt(now)
            .createdBy(userId)
            .updatedAt(now)
            .updatedBy(userId)
            .build();
        
        List<DocumentType> documentTypes = Arrays.asList(docType1, docType2);
        
        DocumentTypeDTO dto1 = DocumentTypeDTO.builder()
            .documentTypeId(1)
            .typeName("Passport")
            .description("Government issued passport")
            .isRequired(true)
            .createdAt(now)
            .createdBy(userId)
            .updatedAt(now)
            .updatedBy(userId)
            .build();
        
        DocumentTypeDTO dto2 = DocumentTypeDTO.builder()
            .documentTypeId(2)
            .typeName("Driver License")
            .description("Driver's license")
            .isRequired(true)
            .createdAt(now)
            .createdBy(userId)
            .updatedAt(now)
            .updatedBy(userId)
            .build();
        
        List<DocumentTypeDTO> expectedDtos = Arrays.asList(dto1, dto2);
        
        when(documentTypeRepository.findAll()).thenReturn(documentTypes);
        when(documentTypeMapper.toDTO(docType1)).thenReturn(dto1);
        when(documentTypeMapper.toDTO(docType2)).thenReturn(dto2);

        // When
        List<DocumentTypeDTO> result = masterDataService.getAllDocumentTypes();

        // Then
        assertEquals(expectedDtos.size(), result.size());
        assertEquals(expectedDtos.get(0).getTypeName(), result.get(0).getTypeName());
        assertEquals(expectedDtos.get(1).getTypeName(), result.get(1).getTypeName());
        verify(documentTypeRepository).findAll();
    }

    @Test
    void searchDocumentTypes_shouldReturnFilteredResults() {
        // Given
        String query = "pass";
        int limit = 5;
        DocumentType docType = DocumentType.builder()
            .documentTypeId(1)
            .typeName("Passport")
            .description("Government issued passport")
            .isRequired(true)
            .createdAt(now)
            .createdBy(userId)
            .updatedAt(now)
            .updatedBy(userId)
            .build();
        
        List<DocumentType> documentTypes = Arrays.asList(docType);
        
        DocumentTypeDTO expectedDto = DocumentTypeDTO.builder()
            .documentTypeId(1)
            .typeName("Passport")
            .description("Government issued passport")
            .isRequired(true)
            .createdAt(now)
            .createdBy(userId)
            .updatedAt(now)
            .updatedBy(userId)
            .build();
        
        List<DocumentTypeDTO> expectedDtos = Arrays.asList(expectedDto);
        
        when(documentTypeRepository.findAll()).thenReturn(documentTypes);
        when(documentTypeMapper.toDTO(docType)).thenReturn(expectedDto);

        // When
        List<DocumentTypeDTO> result = masterDataService.searchDocumentTypes(query, limit);

        // Then
        assertEquals(expectedDtos.size(), result.size());
        assertEquals(expectedDtos.get(0).getTypeName(), result.get(0).getTypeName());
        verify(documentTypeRepository).findAll();
    }

    // ID Types Tests
    @Test
    void getAllIdTypes_shouldReturnAllIdTypes() {
        // Given
        IdType idType1 = IdType.builder()
            .idTypeId(1)
            .typeName("Passport")
            .description("Government issued passport")
            .isRequired(true)
            .createdAt(now)
            .createdBy(userId)
            .updatedAt(now)
            .updatedBy(userId)
            .build();
        
        IdType idType2 = IdType.builder()
            .idTypeId(2)
            .typeName("Driver License")
            .description("Driver's license")
            .isRequired(true)
            .createdAt(now)
            .createdBy(userId)
            .updatedAt(now)
            .updatedBy(userId)
            .build();
        
        List<IdType> idTypes = Arrays.asList(idType1, idType2);
        
        IdTypeDTO dto1 = IdTypeDTO.builder()
            .idTypeId(1)
            .typeName("Passport")
            .description("Government issued passport")
            .isRequired(true)
            .createdAt(now)
            .createdBy(userId)
            .updatedAt(now)
            .updatedBy(userId)
            .build();
        
        IdTypeDTO dto2 = IdTypeDTO.builder()
            .idTypeId(2)
            .typeName("Driver License")
            .description("Driver's license")
            .isRequired(true)
            .createdAt(now)
            .createdBy(userId)
            .updatedAt(now)
            .updatedBy(userId)
            .build();
        
        List<IdTypeDTO> expectedDtos = Arrays.asList(dto1, dto2);
        
        when(idTypeRepository.findAll()).thenReturn(idTypes);
        when(idTypeMapper.toDTO(idType1)).thenReturn(dto1);
        when(idTypeMapper.toDTO(idType2)).thenReturn(dto2);

        // When
        List<IdTypeDTO> result = masterDataService.getAllIdTypes();

        // Then
        assertEquals(expectedDtos.size(), result.size());
        assertEquals(expectedDtos.get(0).getTypeName(), result.get(0).getTypeName());
        assertEquals(expectedDtos.get(1).getTypeName(), result.get(1).getTypeName());
        verify(idTypeRepository).findAll();
    }

    @Test
    void searchIdTypes_shouldReturnFilteredResults() {
        // Given
        String query = "pass";
        int limit = 5;
        IdType idType = IdType.builder()
            .idTypeId(1)
            .typeName("Passport")
            .description("Government issued passport")
            .isRequired(true)
            .createdAt(now)
            .createdBy(userId)
            .updatedAt(now)
            .updatedBy(userId)
            .build();
        
        List<IdType> idTypes = Arrays.asList(idType);
        
        IdTypeDTO expectedDto = IdTypeDTO.builder()
            .idTypeId(1)
            .typeName("Passport")
            .description("Government issued passport")
            .isRequired(true)
            .createdAt(now)
            .createdBy(userId)
            .updatedAt(now)
            .updatedBy(userId)
            .build();
        
        List<IdTypeDTO> expectedDtos = Arrays.asList(expectedDto);
        
        when(idTypeRepository.findAll()).thenReturn(idTypes);
        when(idTypeMapper.toDTO(idType)).thenReturn(expectedDto);

        // When
        List<IdTypeDTO> result = masterDataService.searchIdTypes(query, limit);

        // Then
        assertEquals(expectedDtos.size(), result.size());
        assertEquals(expectedDtos.get(0).getTypeName(), result.get(0).getTypeName());
        verify(idTypeRepository).findAll();
    }

    // Countries Tests
    @Test
    void getAllCountries_shouldReturnAllCountries() {
        // Given
        Country country1 = Country.builder()
            .countryId(1)
            .countryCode("US")
            .countryName("United States")
            .phoneCode("+1")
            .currencyCode("USD")
            .isActive(true)
            .createdAt(now)
            .createdBy(userId)
            .updatedAt(now)
            .updatedBy(userId)
            .build();
        
        Country country2 = Country.builder()
            .countryId(2)
            .countryCode("CA")
            .countryName("Canada")
            .phoneCode("+1")
            .currencyCode("CAD")
            .isActive(true)
            .createdAt(now)
            .createdBy(userId)
            .updatedAt(now)
            .updatedBy(userId)
            .build();
        
        List<Country> countries = Arrays.asList(country1, country2);
        
        CountryDTO dto1 = CountryDTO.builder()
            .countryId(1)
            .countryCode("US")
            .countryName("United States")
            .phoneCode("+1")
            .currencyCode("USD")
            .isActive(true)
            .createdAt(now)
            .createdBy(userId)
            .updatedAt(now)
            .updatedBy(userId)
            .build();
        
        CountryDTO dto2 = CountryDTO.builder()
            .countryId(2)
            .countryCode("CA")
            .countryName("Canada")
            .phoneCode("+1")
            .currencyCode("CAD")
            .isActive(true)
            .createdAt(now)
            .createdBy(userId)
            .updatedAt(now)
            .updatedBy(userId)
            .build();
        
        List<CountryDTO> expectedDtos = Arrays.asList(dto1, dto2);
        
        when(countryRepository.findAll()).thenReturn(countries);
        when(countryMapper.toDTO(country1)).thenReturn(dto1);
        when(countryMapper.toDTO(country2)).thenReturn(dto2);

        // When
        List<CountryDTO> result = masterDataService.getAllCountries();

        // Then
        assertEquals(expectedDtos.size(), result.size());
        assertEquals(expectedDtos.get(0).getCountryName(), result.get(0).getCountryName());
        assertEquals(expectedDtos.get(1).getCountryName(), result.get(1).getCountryName());
        verify(countryRepository).findAll();
    }

    @Test
    void searchCountries_shouldReturnFilteredResults() {
        // Given
        String query = "united";
        int limit = 5;
        Country country = Country.builder()
            .countryId(1)
            .countryCode("US")
            .countryName("United States")
            .phoneCode("+1")
            .currencyCode("USD")
            .isActive(true)
            .createdAt(now)
            .createdBy(userId)
            .updatedAt(now)
            .updatedBy(userId)
            .build();
        
        List<Country> countries = Arrays.asList(country);
        
        CountryDTO expectedDto = CountryDTO.builder()
            .countryId(1)
            .countryCode("US")
            .countryName("United States")
            .phoneCode("+1")
            .currencyCode("USD")
            .isActive(true)
            .createdAt(now)
            .createdBy(userId)
            .updatedAt(now)
            .updatedBy(userId)
            .build();
        
        List<CountryDTO> expectedDtos = Arrays.asList(expectedDto);
        
        when(countryRepository.findByCountryNameContainingIgnoreCase(query)).thenReturn(countries);
        when(countryMapper.toDTO(country)).thenReturn(expectedDto);

        // When
        List<CountryDTO> result = masterDataService.searchCountries(query, limit);

        // Then
        assertEquals(expectedDtos.size(), result.size());
        assertEquals(expectedDtos.get(0).getCountryName(), result.get(0).getCountryName());
        verify(countryRepository).findByCountryNameContainingIgnoreCase(query);
    }

    // States Tests
    @Test
    void getAllStates_shouldReturnAllStates() {
        // Given
        State state1 = State.builder()
            .stateId(1)
            .stateName("California")
            .stateCode("CA")
            .countryId(1)
            .isActive(true)
            .createdAt(now)
            .createdBy(userId)
            .updatedAt(now)
            .updatedBy(userId)
            .build();
        
        State state2 = State.builder()
            .stateId(2)
            .stateName("Texas")
            .stateCode("TX")
            .countryId(1)
            .isActive(true)
            .createdAt(now)
            .createdBy(userId)
            .updatedAt(now)
            .updatedBy(userId)
            .build();
        
        List<State> states = Arrays.asList(state1, state2);
        
        StateDTO dto1 = StateDTO.builder()
            .stateId(1)
            .stateName("California")
            .stateCode("CA")
            .countryId(1)
            .isActive(true)
            .createdAt(now)
            .createdBy(userId)
            .updatedAt(now)
            .updatedBy(userId)
            .build();
        
        StateDTO dto2 = StateDTO.builder()
            .stateId(2)
            .stateName("Texas")
            .stateCode("TX")
            .countryId(1)
            .isActive(true)
            .createdAt(now)
            .createdBy(userId)
            .updatedAt(now)
            .updatedBy(userId)
            .build();
        
        List<StateDTO> expectedDtos = Arrays.asList(dto1, dto2);
        
        when(stateRepository.findAll()).thenReturn(states);
        when(stateMapper.toDTO(state1)).thenReturn(dto1);
        when(stateMapper.toDTO(state2)).thenReturn(dto2);

        // When
        List<StateDTO> result = masterDataService.getAllStates();

        // Then
        assertEquals(expectedDtos.size(), result.size());
        assertEquals(expectedDtos.get(0).getStateName(), result.get(0).getStateName());
        assertEquals(expectedDtos.get(1).getStateName(), result.get(1).getStateName());
        verify(stateRepository).findAll();
    }

    @Test
    void searchStates_shouldReturnFilteredResults() {
        // Given
        String query = "california";
        int limit = 5;
        State state = State.builder()
            .stateId(1)
            .stateName("California")
            .stateCode("CA")
            .countryId(1)
            .isActive(true)
            .createdAt(now)
            .createdBy(userId)
            .updatedAt(now)
            .updatedBy(userId)
            .build();
        
        List<State> states = Arrays.asList(state);
        
        StateDTO expectedDto = StateDTO.builder()
            .stateId(1)
            .stateName("California")
            .stateCode("CA")
            .countryId(1)
            .isActive(true)
            .createdAt(now)
            .createdBy(userId)
            .updatedAt(now)
            .updatedBy(userId)
            .build();
        
        List<StateDTO> expectedDtos = Arrays.asList(expectedDto);
        
        when(stateRepository.findByStateNameContainingIgnoreCase(query)).thenReturn(states);
        when(stateMapper.toDTO(state)).thenReturn(expectedDto);

        // When
        List<StateDTO> result = masterDataService.searchStates(query, limit);

        // Then
        assertEquals(expectedDtos.size(), result.size());
        assertEquals(expectedDtos.get(0).getStateName(), result.get(0).getStateName());
        verify(stateRepository).findByStateNameContainingIgnoreCase(query);
    }

    // Skills Tests
    @Test
    void getAllSkills_shouldReturnAllSkills() {
        // Given
        Skill skill1 = Skill.builder()
            .skillId(1)
            .skillName("Java")
            .skillCategory("Programming")
            .description("Java programming language")
            .createdAt(now)
            .createdBy(userId)
            .updatedAt(now)
            .updatedBy(userId)
            .build();
        
        Skill skill2 = Skill.builder()
            .skillId(2)
            .skillName("Python")
            .skillCategory("Programming")
            .description("Python programming language")
            .createdAt(now)
            .createdBy(userId)
            .updatedAt(now)
            .updatedBy(userId)
            .build();
        
        List<Skill> skills = Arrays.asList(skill1, skill2);
        
        SkillDTO dto1 = SkillDTO.builder()
            .skillId(1)
            .skillName("Java")
            .skillCategory("Programming")
            .description("Java programming language")
            .createdAt(now)
            .createdBy(userId)
            .updatedAt(now)
            .updatedBy(userId)
            .build();
        
        SkillDTO dto2 = SkillDTO.builder()
            .skillId(2)
            .skillName("Python")
            .skillCategory("Programming")
            .description("Python programming language")
            .createdAt(now)
            .createdBy(userId)
            .updatedAt(now)
            .updatedBy(userId)
            .build();
        
        List<SkillDTO> expectedDtos = Arrays.asList(dto1, dto2);
        
        when(skillRepository.findAll()).thenReturn(skills);

        // When
        List<SkillDTO> result = masterDataService.getAllSkills();

        // Then
        assertEquals(expectedDtos.size(), result.size());
        assertEquals(expectedDtos.get(0).getSkillName(), result.get(0).getSkillName());
        assertEquals(expectedDtos.get(1).getSkillName(), result.get(1).getSkillName());
        verify(skillRepository).findAll();
    }

    // Departments Tests
    @Test
    void getAllDepartments_shouldReturnAllDepartments() {
        // Given
        Department dept1 = Department.builder()
            .departmentId(1)
            .tenantId(UUID.randomUUID())
            .organizationId(UUID.randomUUID())
            .name("Engineering")
            .parentDepartmentId(null)
            .statusId(1)
            .createdAt(now)
            .createdBy(userId)
            .updatedAt(now)
            .updatedBy(userId)
            .build();
        
        Department dept2 = Department.builder()
            .departmentId(2)
            .tenantId(UUID.randomUUID())
            .organizationId(UUID.randomUUID())
            .name("Marketing")
            .parentDepartmentId(null)
            .statusId(1)
            .createdAt(now)
            .createdBy(userId)
            .updatedAt(now)
            .updatedBy(userId)
            .build();
        
        List<Department> departments = Arrays.asList(dept1, dept2);
        
        DepartmentDTO dto1 = DepartmentDTO.builder()
            .departmentId(1)
            .tenantId(UUID.randomUUID())
            .organizationId(UUID.randomUUID())
            .name("Engineering")
            .parentDepartmentId(null)
            .statusId(1)
            .createdAt(now)
            .createdBy(userId)
            .updatedAt(now)
            .updatedBy(userId)
            .build();
        
        DepartmentDTO dto2 = DepartmentDTO.builder()
            .departmentId(2)
            .tenantId(UUID.randomUUID())
            .organizationId(UUID.randomUUID())
            .name("Marketing")
            .parentDepartmentId(null)
            .statusId(1)
            .createdAt(now)
            .createdBy(userId)
            .updatedAt(now)
            .updatedBy(userId)
            .build();
        
        List<DepartmentDTO> expectedDtos = Arrays.asList(dto1, dto2);
        
        when(departmentRepository.findAll()).thenReturn(departments);

        // When
        List<DepartmentDTO> result = masterDataService.getAllDepartments();

        // Then
        assertEquals(expectedDtos.size(), result.size());
        assertEquals(expectedDtos.get(0).getName(), result.get(0).getName());
        assertEquals(expectedDtos.get(1).getName(), result.get(1).getName());
        verify(departmentRepository).findAll();
    }

    // User Roles Tests
    @Test
    void getAllUserRoles_shouldReturnAllUserRoles() {
        // Given
        UserRole role1 = UserRole.builder()
            .roleId(1)
            .name("Admin")
            .permissions("{\"read\": true, \"write\": true}")
            .createdAt(now)
            .createdBy(userId)
            .updatedAt(now)
            .updatedBy(userId)
            .build();
        
        UserRole role2 = UserRole.builder()
            .roleId(2)
            .name("User")
            .permissions("{\"read\": true, \"write\": false}")
            .createdAt(now)
            .createdBy(userId)
            .updatedAt(now)
            .updatedBy(userId)
            .build();
        
        List<UserRole> userRoles = Arrays.asList(role1, role2);
        
        UserRoleDTO dto1 = UserRoleDTO.builder()
            .roleId(1)
            .name("Admin")
            .permissions("{\"read\": true, \"write\": true}")
            .createdAt(now)
            .createdBy(userId)
            .updatedAt(now)
            .updatedBy(userId)
            .build();
        
        UserRoleDTO dto2 = UserRoleDTO.builder()
            .roleId(2)
            .name("User")
            .permissions("{\"read\": true, \"write\": false}")
            .createdAt(now)
            .createdBy(userId)
            .updatedAt(now)
            .updatedBy(userId)
            .build();
        
        List<UserRoleDTO> expectedDtos = Arrays.asList(dto1, dto2);
        
        when(userRoleRepository.findAll()).thenReturn(userRoles);

        // When
        List<UserRoleDTO> result = masterDataService.getAllUserRoles();

        // Then
        assertEquals(expectedDtos.size(), result.size());
        assertEquals(expectedDtos.get(0).getName(), result.get(0).getName());
        assertEquals(expectedDtos.get(1).getName(), result.get(1).getName());
        verify(userRoleRepository).findAll();
    }

    // User Types Tests
    @Test
    void getAllUserTypes_shouldReturnAllUserTypes() {
        // Given
        UserType type1 = UserType.builder()
            .typeId(1)
            .name("Employee")
            .description("Full-time employee")
            .createdAt(now)
            .createdBy(userId)
            .updatedAt(now)
            .updatedBy(userId)
            .build();
        
        UserType type2 = UserType.builder()
            .typeId(2)
            .name("Contractor")
            .description("Contract worker")
            .createdAt(now)
            .createdBy(userId)
            .updatedAt(now)
            .updatedBy(userId)
            .build();
        
        List<UserType> userTypes = Arrays.asList(type1, type2);
        
        UserTypeDTO dto1 = UserTypeDTO.builder()
            .typeId(1)
            .name("Employee")
            .description("Full-time employee")
            .createdAt(now)
            .createdBy(userId)
            .updatedAt(now)
            .updatedBy(userId)
            .build();
        
        UserTypeDTO dto2 = UserTypeDTO.builder()
            .typeId(2)
            .name("Contractor")
            .description("Contract worker")
            .createdAt(now)
            .createdBy(userId)
            .updatedAt(now)
            .updatedBy(userId)
            .build();
        
        List<UserTypeDTO> expectedDtos = Arrays.asList(dto1, dto2);
        
        when(userTypeRepository.findAll()).thenReturn(userTypes);

        // When
        List<UserTypeDTO> result = masterDataService.getAllUserTypes();

        // Then
        assertEquals(expectedDtos.size(), result.size());
        assertEquals(expectedDtos.get(0).getName(), result.get(0).getName());
        assertEquals(expectedDtos.get(1).getName(), result.get(1).getName());
        verify(userTypeRepository).findAll();
    }
} 