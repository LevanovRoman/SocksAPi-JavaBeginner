package com.myapp.socksapijavabeginner.service;

import com.myapp.socksapijavabeginner.dto.SockDto;

public interface SockService {

    String arrivalSock(SockDto sockDto);

    String departureSock(SockDto sockDto);

    String updateSock(long id, SockDto sockDto);
}
