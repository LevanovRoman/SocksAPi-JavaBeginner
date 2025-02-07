package com.myapp.socksapijavabeginner.service;

import com.myapp.socksapijavabeginner.dto.SockDto;
import com.myapp.socksapijavabeginner.util.Operators;
import org.springframework.web.multipart.MultipartFile;

public interface SockService {

    String arrivalSock(SockDto sockDto);

    String departureSock(SockDto sockDto);

    String updateSock(long id, SockDto sockDto);

    String parseAndSaveSocks(MultipartFile file);

    long searchSocks(String color, Integer cottonPercentage, Operators operators);
}
