package com.consoleolog.anyaskapiserver.v1.service.impl;

import com.consoleolog.anyaskapiserver.v1.service.RagService;
import com.consoleolog.anyaskapiserver.v1.util.UnstructuredLoader;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
@Service
public class RagServiceImpl implements RagService {

    private final UnstructuredLoader loader;

    private final VectorStore vectorStore;


    @Transactional
    @Override
    public void createVectorStore(MultipartFile file) throws IOException {

        List<Document> documents = loader.load(file, new HashMap<>());

        vectorStore.add(documents);
    }

}
