package com.consoleolog.anyaskapiserver.v1.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface RagService {

    void createVectorStore(MultipartFile file) throws IOException;

}
