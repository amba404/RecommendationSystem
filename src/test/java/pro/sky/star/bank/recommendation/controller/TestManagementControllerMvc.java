package pro.sky.star.bank.recommendation.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pro.sky.star.bank.recommendation.dto.Info;

import static org.mockito.Mockito.when;

@WebMvcTest(ManagementController.class)
@ExtendWith(MockitoExtension.class)
public class TestManagementControllerMvc {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ManagementController managementController;

    @Test
    public void testGetInfo() throws Exception {
        Info info = new Info("ServiceName", "1.1.1.1");
        when(managementController.getInfo()).thenReturn(info);

        mockMvc.perform(MockMvcRequestBuilders.get("/management/info")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(info.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.version").value(info.getVersion()));
    }
}

