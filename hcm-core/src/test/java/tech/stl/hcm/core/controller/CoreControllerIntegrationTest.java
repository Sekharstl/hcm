package tech.stl.hcm.core.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import tech.stl.hcm.message.broker.producer.ProducerService;
import tech.stl.hcm.core.config.ServiceProperties;
import org.springframework.web.reactive.function.client.WebClient;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CoreControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProducerService producerService;
    
    @MockBean
    private ServiceProperties serviceProperties;
    
    @MockBean
    private WebClient.Builder webClientBuilder;

    @Test
    void helloEndpoint_returnsHelloString() throws Exception {
        mockMvc.perform(get("/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello from hcm-core!"));
    }
} 