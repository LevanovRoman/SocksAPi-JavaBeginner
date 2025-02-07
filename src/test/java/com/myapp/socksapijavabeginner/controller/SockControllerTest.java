package com.myapp.socksapijavabeginner.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myapp.socksapijavabeginner.dto.SockDto;
import com.myapp.socksapijavabeginner.mapper.SockMapperImpl;
import com.myapp.socksapijavabeginner.model.Sock;
import com.myapp.socksapijavabeginner.repository.SockRepository;
import com.myapp.socksapijavabeginner.service.SockService;
import com.myapp.socksapijavabeginner.service.SockServiceImpl;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.InputStream;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SockController.class)
@AutoConfigureMockMvc
@Import(value = {
        SockServiceImpl.class,
        SockMapperImpl.class,
})
class SockControllerTest {

    @Resource
    private MockMvc mockMvc;
    @Resource
    private ObjectMapper objectMapper;

    @MockitoBean
    private SockRepository sockRepository;

    private Sock sock;
    private SockDto sockDto;

    @BeforeEach
    public void setUp(){
        sock = new Sock(1L, "black", 50, 50);

        sockDto = new SockDto(1L, "black", 50, 50);
    }

//    @Test
//    void incomeTest() throws Exception {
//
//        SockDto sockDto = new SockDto(1L, "black", 50, 50);
//
//        String url = "/api/socks/income";
//        MvcResult mvcResult = mockMvc.perform(post(url)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(sockDto))
//                        .characterEncoding("UTF-8"))
//                .andReturn();
//
//        Assertions.assertEquals(mvcResult.getResponse().getStatus(), 200);
//        Assertions.assertEquals("Socks with color black and cottonPercentage 50 not found.", mvcResult.getResponse().getContentAsString());
//        System.out.println(mvcResult.getResponse().getContentAsString());
//    }

    @ParameterizedTest
    @CsvSource({
             "true",
            "false"
    })
    void incomeTest(boolean isSockPresentInDb) throws Exception {

        String url = "/api/socks/income";

        if (isSockPresentInDb){
            when(sockRepository.findByColorAndCottonPercentage(sockDto.getColor(), sockDto.getCottonPercentage()))
                    .thenReturn(Optional.of(sock));
            MvcResult mvcResult = mockMvc.perform(post(url)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(sockDto))
                            .characterEncoding("UTF-8"))
                    .andReturn();

            Assertions.assertEquals(mvcResult.getResponse().getStatus(), 200);
            Assertions.assertEquals("Socks with color black and cottonPercentage 50 added and now quantity 100.",
                    mvcResult.getResponse().getContentAsString());
            System.out.println(mvcResult.getResponse().getContentAsString());
        } else {
            MvcResult mvcResult = mockMvc.perform(post(url)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(sockDto))
                            .characterEncoding("UTF-8"))
                    .andReturn();

            Assertions.assertEquals(mvcResult.getResponse().getStatus(), 200);
            Assertions.assertEquals("Socks with color black and cottonPercentage 50 not found.",
                    mvcResult.getResponse().getContentAsString());
            System.out.println(mvcResult.getResponse().getContentAsString());
        }
    }

    @Test
    void outcomeTest() throws Exception {
        String url = "/api/socks/outcome";

        when(sockRepository.findByColorAndCottonPercentage(sockDto.getColor(), sockDto.getCottonPercentage()))
                .thenReturn(Optional.of(sock));

        MvcResult mvcResult = mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sockDto))
                        .characterEncoding("UTF-8"))
                .andReturn();

        Assertions.assertEquals(mvcResult.getResponse().getStatus(), 200);
        Assertions.assertEquals("Socks with color black and cottonPercentage 50 departed and now quantity 0.",
                mvcResult.getResponse().getContentAsString());
        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    void updateTest() throws Exception{
        String url = "/api/socks/%d";

        when(sockRepository.findById(anyLong())).thenReturn(Optional.of(sock));

        MvcResult mvcResult = mockMvc.perform(put(url.formatted(1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sockDto))
                        .characterEncoding("UTF-8"))
                .andExpect(status().is(200)) // заменяет Assertions.assertEquals(mvcResult.getResponse().getStatus(), 200);
                .andReturn();
//                .andExpect(content().bytes("Socks with color black and cottonPercentage 50 departed and now quantity 0.".getBytes())); // заменяет
        Assertions.assertEquals("Socks with color black and cottonPercentage 50 updated in db by ID 1.",
                mvcResult.getResponse().getContentAsString());
        System.out.println(mvcResult.getResponse().getContentAsString());

    }

    @Test
    void batchSocksSocksTest() throws Exception{
        String url = "/api/socks/batch";

        ClassPathResource classPathResource = new ClassPathResource("test.csv");
        InputStream inputStream = classPathResource.getInputStream();

        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "file",
                classPathResource.getFilename(),
                MediaType.TEXT_PLAIN_VALUE,
                inputStream
        );
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.multipart(url)
                .file(mockMultipartFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().is(200))
                .andReturn();
        Assertions.assertEquals("File csv successfully done,  in db added 3 socks.",
                mvcResult.getResponse().getContentAsString());
        System.out.println(mvcResult.getResponse().getContentAsString());
    }

}