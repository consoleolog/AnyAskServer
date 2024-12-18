package com.consoleolog.anyaskapiserver.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.MetadataMode;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.writer.FileDocumentWriter;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@SpringBootTest
public class TikaDocumentReaderTest {

    @Test
    public void testTikaDocumentReaderHTML() throws IOException {

        Document document = Jsoup.connect("https://offbyone.tistory.com/116").get();

        byte[] contentBytes = document.html().getBytes(StandardCharsets.UTF_8);

        Resource resource = new ByteArrayResource(contentBytes);

        TikaDocumentReader reader = new TikaDocumentReader(resource);
        System.out.println("GET");
        System.out.println(reader.get());
        System.out.println("READ");
        System.out.println(reader.read());

        TokenTextSplitter splitter = new TokenTextSplitter();
        System.out.println("APPLY");
        System.out.println(splitter.apply(reader.read()));
        System.out.println("SPLIT");
        System.out.println(splitter.split(reader.read()));

    }

    @Test
    public void testSplitAndLoad(){
        TokenTextSplitter splitter = new TokenTextSplitter(1000, 400, 20, 500, true);




    }

    @Test
    public void testWriteDocument() throws IOException {
        FileDocumentWriter writer = new FileDocumentWriter("test.pdf",true, MetadataMode.ALL, false);
//        writer.accept();
    }


}
