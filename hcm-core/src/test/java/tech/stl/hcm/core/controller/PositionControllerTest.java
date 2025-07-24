package tech.stl.hcm.core.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.stl.hcm.core.service.PositionService;
import tech.stl.hcm.common.dto.PositionDTO;
import tech.stl.hcm.common.dto.PositionStatusDTO;
import tech.stl.hcm.common.dto.helpers.PositionCreateDTO;
import tech.stl.hcm.common.dto.PaginatedResponseDTO;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PositionControllerTest {
    @Mock
    private PositionService positionService;
    @InjectMocks
    private PositionController positionController;

    @Test
    void getAllPositions_delegatesToService() {
        // Given
        PositionDTO position1 = PositionDTO.builder()
            .positionId(1)
            .title("Software Engineer")
            .description("Develop software applications")
            .location("San Francisco, CA")
            .employmentType("Full-time")
            .build();
        
        PositionDTO position2 = PositionDTO.builder()
            .positionId(2)
            .title("Product Manager")
            .description("Manage product development")
            .location("New York, NY")
            .employmentType("Full-time")
            .build();
        
        PaginatedResponseDTO<PositionDTO> expectedResponse = new PaginatedResponseDTO<>();
        expectedResponse.setContent(List.of(position1, position2));
        expectedResponse.setTotalElements(2);
        expectedResponse.setTotalPages(1);
        expectedResponse.setPageNumber(0);
        expectedResponse.setPageSize(20);
        
        when(positionService.getAllPositionsPaginated(0, 20, "positionId", "ASC")).thenReturn(expectedResponse);
        
        // When
        PaginatedResponseDTO<PositionDTO> result = positionController.getAllPositions(0, 20, "positionId", "ASC");
        
        // Then
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(position1.getTitle(), result.getContent().get(0).getTitle());
        assertEquals(position2.getTitle(), result.getContent().get(1).getTitle());
        verify(positionService).getAllPositionsPaginated(0, 20, "positionId", "ASC");
    }

    @Test
    void getPositionById_delegatesToService() {
        Integer id = 1;
        positionController.getPositionById(id);
        verify(positionService).getPositionById(id);
    }

    @Test
    void createPosition_delegatesToService() {
        // Given
        PositionCreateDTO dto = PositionCreateDTO.builder()
            .title("Software Engineer")
            .description("Develop software applications")
            .location("San Francisco, CA")
            .employmentType("Full-time")
            .build();
        
        // When
        positionController.createPosition(dto);
        
        // Then
        verify(positionService).createPosition(dto);
    }

    @Test
    void updatePosition_delegatesToService() {
        // Given
        Integer id = 1;
        PositionDTO dto = PositionDTO.builder()
            .positionId(id)
            .title("Senior Software Engineer")
            .description("Develop advanced software applications")
            .location("San Francisco, CA")
            .employmentType("Full-time")
            .build();
        
        // When
        positionController.updatePosition(id, dto);
        
        // Then
        verify(positionService).updatePosition(id, dto);
    }

    @Test
    void deletePosition_delegatesToService() {
        Integer id = 1;
        positionController.deletePosition(id);
        verify(positionService).deletePosition(id);
    }

    @Test
    void getAllPositionStatuses_delegatesToService() {
        when(positionService.getAllPositionStatuses()).thenReturn(Collections.emptyList());
        positionController.getAllPositionStatuses();
        verify(positionService).getAllPositionStatuses();
    }

    @Test
    void getPositionStatusById_delegatesToService() {
        Integer id = 1;
        positionController.getPositionStatusById(id);
        verify(positionService).getPositionStatusById(id);
    }

    @Test
    void createPositionStatus_delegatesToService() {
        // Given
        PositionStatusDTO dto = PositionStatusDTO.builder()
            .name("Active")
            .description("Position is currently active and accepting applications")
            .build();
        
        // When
        positionController.createPositionStatus(dto);
        
        // Then
        verify(positionService).createPositionStatus(dto);
    }

    @Test
    void updatePositionStatus_delegatesToService() {
        // Given
        Integer id = 1;
        PositionStatusDTO dto = PositionStatusDTO.builder()
            .statusId(id)
            .name("Inactive")
            .description("Position is currently inactive")
            .build();
        
        // When
        positionController.updatePositionStatus(id, dto);
        
        // Then
        verify(positionService).updatePositionStatus(id, dto);
    }

    @Test
    void deletePositionStatus_delegatesToService() {
        Integer id = 1;
        positionController.deletePositionStatus(id);
        verify(positionService).deletePositionStatus(id);
    }

    @Test
    void getPositionById_whenServiceThrows_shouldPropagate() {
        Integer id = 1;
        when(positionService.getPositionById(id)).thenThrow(new RuntimeException("fail"));
        assertThrows(RuntimeException.class, () -> positionController.getPositionById(id));
    }

    @Test
    void createPosition_withNull_shouldCallService() {
        positionController.createPosition(null);
        verify(positionService).createPosition(null);
    }
} 