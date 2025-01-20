package com.myapp.socksapijavabeginner.service;

import com.myapp.socksapijavabeginner.dto.SockDto;
import com.myapp.socksapijavabeginner.mapper.SockMapper;
import com.myapp.socksapijavabeginner.model.Sock;
import com.myapp.socksapijavabeginner.repository.SockRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SockServiceImpl implements SockService{

    @Resource
    private SockRepository sockRepository;
    @Resource
    private SockMapper sockMapper;

    @Override
    public String arrivalSock(SockDto sockDto){
        String responseMessage;

        Optional<Sock> optionalSock = sockRepository.findByColorAndCottonPercentage(sockDto.getColor(),
                sockDto.getCottonPercentage());
        if (optionalSock.isPresent()){
            int count = optionalSock.get().getQuantity() + sockDto.getQuantity();
            optionalSock.get().setQuantity(count);
            sockRepository.save(optionalSock.get());
            responseMessage = "Socks with color %s and cottonPercentage %d added and now quantity %d."
                    .formatted(sockDto.getColor(), sockDto.getCottonPercentage(), count);
        } else {
            sockRepository.save(sockMapper.entityToDto(sockDto));
            responseMessage = "Socks with color %s and cottonPercentage %d not found."
                    .formatted(sockDto.getColor(), sockDto.getCottonPercentage());
        }
        return responseMessage;
    }

    @Override
    public String departureSock(SockDto sockDto){
        Optional<Sock> optionalSock = sockRepository.findByColorAndCottonPercentage(sockDto.getColor(),
                sockDto.getCottonPercentage());
        if (optionalSock.isEmpty()) throw new IllegalArgumentException();
        if (optionalSock.get().getQuantity() < sockDto.getQuantity()) throw new IllegalArgumentException();
        int quantity = optionalSock.get().getQuantity() - sockDto.getQuantity();
        optionalSock.get().setQuantity(quantity);
        sockRepository.save(optionalSock.get());
        return  "Socks with color %s and cottonPercentage %d departed and now quantity %d."
                .formatted(sockDto.getColor(), sockDto.getCottonPercentage(), quantity);
    }

    @Override
    public String updateSock(long id, SockDto sockDto) {
        Optional<Sock> byId = sockRepository.findById(id);
        if (byId.isEmpty()) throw new IllegalArgumentException();

        byId.get().setColor(sockDto.getColor());
        byId.get().setCottonPercentage(sockDto.getCottonPercentage());
        byId.get().setQuantity(sockDto.getQuantity());
        Sock save = sockRepository.save(byId.get());

        return  "Socks with color %s and cottonPercentage %d updated in db by ID %d."
                .formatted(sockDto.getColor(), sockDto.getCottonPercentage(), id);
    }
}
