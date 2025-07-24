package tech.stl.hcm.core.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.stl.hcm.core.service.JobRequisitionService;
import tech.stl.hcm.common.dto.JobRequisitionDTO;
import tech.stl.hcm.common.dto.helpers.JobRequisitionCreateDTO;
import tech.stl.hcm.common.dto.PaginatedResponseDTO;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JobRequisitionControllerTest {
    @Mock
    private JobRequisitionService jobRequisitionService;
    @InjectMocks
    private JobRequisitionController jobRequisitionController;

    @Test
    void getAllJobRequisitions_delegatesToService() {
        PaginatedResponseDTO<JobRequisitionDTO> expectedResponse = new PaginatedResponseDTO<>();
        when(jobRequisitionService.getAllJobRequisitionsPaginated(0, 20, "jobRequisitionId", "ASC")).thenReturn(expectedResponse);
        jobRequisitionController.getAllJobRequisitions(0, 20, "jobRequisitionId", "ASC");
        verify(jobRequisitionService).getAllJobRequisitionsPaginated(0, 20, "jobRequisitionId", "ASC");
    }

    @Test
    void getJobRequisitionById_delegatesToService() {
        Integer id = 1;
        jobRequisitionController.getJobRequisitionById(id);
        verify(jobRequisitionService).getJobRequisitionById(id);
    }

    @Test
    void createJobRequisition_delegatesToService() {
        JobRequisitionCreateDTO dto = new JobRequisitionCreateDTO();
        jobRequisitionController.createJobRequisition(dto);
        verify(jobRequisitionService).createJobRequisition(dto);
    }

    @Test
    void updateJobRequisition_delegatesToService() {
        JobRequisitionDTO dto = new JobRequisitionDTO();
        jobRequisitionController.updateJobRequisition(dto);
        verify(jobRequisitionService).updateJobRequisition(dto);
    }

    @Test
    void deleteJobRequisition_delegatesToService() {
        Integer id = 1;
        jobRequisitionController.deleteJobRequisition(id);
        verify(jobRequisitionService).deleteJobRequisition(id);
    }

    @Test
    void getJobRequisitionById_whenServiceThrows_shouldPropagate() {
        Integer id = 1;
        when(jobRequisitionService.getJobRequisitionById(id)).thenThrow(new RuntimeException("fail"));
        assertThrows(RuntimeException.class, () -> jobRequisitionController.getJobRequisitionById(id));
    }

    @Test
    void createJobRequisition_withNull_shouldCallService() {
        jobRequisitionController.createJobRequisition(null);
        verify(jobRequisitionService).createJobRequisition(null);
    }
} 