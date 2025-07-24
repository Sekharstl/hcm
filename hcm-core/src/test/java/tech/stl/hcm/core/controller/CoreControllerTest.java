package tech.stl.hcm.core.controller;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import tech.stl.hcm.core.service.CoreService;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class CoreControllerTest {

    @Mock
    private CoreService coreService;

    @InjectMocks
    private CoreController coreController;

    @Test
    void hello_returnsHelloString() {
        String result = coreController.hello();
        assert(result.equals("Hello from hcm-core!"));
    }

    @Test
    void publishMessage_callsServicePublishEvent() {
        String message = "test-message";
        coreController.publishMessage(message);
        verify(coreService).publishEvent("test-topic", "test-key", message);
    }

    @Test
    void publishMessage_whenServiceThrowsException_shouldPropagate() {
        String message = "fail";
        doThrow(new RuntimeException("fail")).when(coreService).publishEvent(any(), any(), any());
        assertThrows(RuntimeException.class, () -> coreController.publishMessage(message));
    }

    @Test
    void publishMessage_withNullMessage_shouldCallService() {
        coreController.publishMessage(null);
        verify(coreService).publishEvent("test-topic", "test-key", null);
    }
} 