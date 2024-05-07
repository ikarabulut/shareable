package com.ikarabulut.shareable.integrated;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.ikarabulut.shareable.web.controllers.FileController;
import com.ikarabulut.shareable.common.models.FileModel;
import com.ikarabulut.shareable.web.repository.FileRepository;

import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FileController.class)
class CreateFileTest {
    @MockBean
    private FileRepository fileRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ObjectWriter objectWriter = objectMapper.writer();
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void allowedFileExtensionShouldPass() throws Exception {
        var requestModel = new FileModel();
        requestModel.setName("validName.tif");
        requestModel.setSignature("alkjlk");
        requestModel.setOwnedBy(1);
        requestModel.setDescription("aaah but they dont hear me though");
        requestModel.setIsMalware(false);
        var bodyContent = objectWriter.writeValueAsString(requestModel);
        Mockito.when(fileRepository.save(requestModel)).thenReturn(requestModel);
        var request = MockMvcRequestBuilders.post("/files")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(bodyContent);

        mockMvc.perform(request)
                .andExpect(status().isCreated());
    }

    @Test
    public void disAllowedFileExtensionShouldReturnFailure() throws Exception {
        var requestModel = new FileModel();
        requestModel.setName("validName.bad");
        requestModel.setSignature("alkjlk");
        requestModel.setOwnedBy(1);
        requestModel.setDescription("aaah but they dont hear me though");
        requestModel.setIsMalware(false);
        var bodyContent = objectWriter.writeValueAsString(requestModel);
        Mockito.when(fileRepository.save(requestModel)).thenReturn(requestModel);
        var request = MockMvcRequestBuilders.post("/files")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(bodyContent);

        mockMvc.perform(request)
                .andExpect(status().isNotAcceptable());
    }

    @Test
    public void disAllowedFileExtensionWithNullBytesShouldReturnFailure() throws Exception {
        var requestModel = new FileModel();
        requestModel.setName("validName.php%00.tif");
        requestModel.setSignature("alkjlk");
        requestModel.setOwnedBy(1);
        requestModel.setDescription("aaah but they dont hear me though");
        requestModel.setIsMalware(false);
        var bodyContent = objectWriter.writeValueAsString(requestModel);
        Mockito.when(fileRepository.save(requestModel)).thenReturn(requestModel);
        var request = MockMvcRequestBuilders.post("/files")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(bodyContent);

        mockMvc.perform(request)
                .andExpect(status().is5xxServerError());
    }

    @Test
    public void disAllowedFileExtensionRegexWorkaroundShouldReturnFailure() throws Exception {
        var requestModel = new FileModel();
        requestModel.setName("validName.jpg.php");
        requestModel.setSignature("alkjlk");
        requestModel.setOwnedBy(1);
        requestModel.setDescription("aaah but they dont hear me though");
        requestModel.setIsMalware(false);
        var bodyContent = objectWriter.writeValueAsString(requestModel);
        Mockito.when(fileRepository.save(requestModel)).thenReturn(requestModel);
        var request = MockMvcRequestBuilders.post("/files")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(bodyContent);

        mockMvc.perform(request)
                .andExpect(status().is5xxServerError());
    }
}
