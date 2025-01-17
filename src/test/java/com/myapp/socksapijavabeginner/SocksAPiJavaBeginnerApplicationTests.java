package com.myapp.socksapijavabeginner;

import com.myapp.socksapijavabeginner.dto.SockDto;
import com.myapp.socksapijavabeginner.mapper.SockMapper;
import com.myapp.socksapijavabeginner.model.Sock;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@EnableAutoConfiguration(exclude = {
        DataSourceAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class
})
class SocksAPiJavaBeginnerApplicationTests {

    @Resource
    private SockMapper sockMapper;

    @Test
    void contextLoads() {
    }

    @Test
    void testMapping(){
        Sock sock = new Sock(1L, "black", 50, 10);
        SockDto sockDto = new SockDto(1L, "black", 50, 10);

        SockDto sockDtoTest = sockMapper.dtoToEntity(sock);
        Sock sockTest = sockMapper.entityToDto(sockDto);

        assertEquals(sockDto, sockDtoTest);
        assertEquals(sock, sockTest);
    }

}
