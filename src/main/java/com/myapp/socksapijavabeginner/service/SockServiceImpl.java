package com.myapp.socksapijavabeginner.service;

import com.myapp.socksapijavabeginner.dto.SockDto;
import com.myapp.socksapijavabeginner.mapper.SockMapper;
import com.myapp.socksapijavabeginner.model.Sock;
import com.myapp.socksapijavabeginner.repository.SockRepository;
import com.myapp.socksapijavabeginner.util.Operators;
import jakarta.annotation.Resource;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
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

    @Override
    public String parseAndSaveSocks(MultipartFile file) {

        if (!isCsvFile(file)) throw  new IllegalArgumentException();

        List<SockDto> sockDtos = new ArrayList<>();

        try(InputStreamReader reader = new InputStreamReader(file.getInputStream())){
            CSVParser csvParser = new CSVParser(
                    reader,
                    CSVFormat.DEFAULT.builder()
                            .setHeader("id","color","cottonPercentage","quantity")
                            .setSkipHeaderRecord(true)
                            .setIgnoreHeaderCase(true)
                            .setTrim(true)
                            .build()
            );
            for (CSVRecord csvRecord : csvParser){
                long id = Long.parseLong(csvRecord.get("id"));
                String color = csvRecord.get("color");
                int cottonPercentage = Integer.parseInt(csvRecord.get("cottonPercentage"));
                int quantity = Integer.parseInt(csvRecord.get("quantity"));

                SockDto sockDto = new SockDto(id, color, cottonPercentage, quantity);
                sockDtos.add(sockDto);
            }
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        sockRepository.saveAll(sockMapper.entityListToDtoList(sockDtos));
        return "File csv successfully done,  in db added %d socks."
                .formatted(sockDtos.size());
    }

    @Override
    public long searchSocks(String color, Integer cottonPercentage, Operators operators) {
        switch (operators){
            case MORE_THAN -> ;
            case LESS_THAN -> ;
            case EQUAL -> ;
            default -> throw new IllegalArgumentException();
        }

        return 0;
    }

    private boolean isCsvFile(MultipartFile file){
        String fileName = file.getOriginalFilename();
        return fileName != null && fileName.endsWith(".csv");
    }
}
