package com.consoleolog.anyaskapiserver.v1.util;

import org.jsoup.Jsoup;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
@Component
public class WebBaseLoader {

    public List<Document> load(String url) throws IOException {
        org.jsoup.nodes.Document document = Jsoup.connect(url).get();

        byte[] content = document.text().getBytes(StandardCharsets.UTF_8);

        Resource resource = new ByteArrayResource(content);

        TikaDocumentReader documentReader = new TikaDocumentReader(resource);

        TokenTextSplitter splitter = new TokenTextSplitter(1000, 400, 20, 500, true);

        List<Document> documents = documentReader.read();

        return splitter.apply(documents);
    }

}
