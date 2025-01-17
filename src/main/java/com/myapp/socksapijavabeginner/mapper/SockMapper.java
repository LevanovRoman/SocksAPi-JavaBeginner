package com.myapp.socksapijavabeginner.mapper;

import com.myapp.socksapijavabeginner.dto.SockDto;
import com.myapp.socksapijavabeginner.model.Sock;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SockMapper {

    // названия перепутаны
    Sock entityToDto(SockDto sockDto);

    SockDto dtoToEntity(Sock sock);
}
