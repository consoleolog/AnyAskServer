package com.consoleolog.anyaskapiserver.v1.controller;

import com.consoleolog.anyaskapiserver.v1.model.dto.ChatRoomDto;
import com.consoleolog.anyaskapiserver.v1.util.FileHandler;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/v1/api")
@RestController
public class MainController {

    private final FileHandler fileHandler;

    @GetMapping("/")
    public void init(){

        System.out.println("요청");


    }




    @GetMapping("/images/{fileName}")
    public ResponseEntity<Resource> getImage(@PathVariable String fileName) {

        log.debug(fileName);

        return fileHandler.getFile(fileName);
    }


}
