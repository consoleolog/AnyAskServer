package com.consoleolog.anyaskapiserver.v1.util;

import com.consoleolog.anyaskapiserver.global.error.exception.BaseException;
import com.consoleolog.anyaskapiserver.global.error.exception.ValidationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;
/* https://docs.unstructured.io/api-reference/general/pipeline-1 */
@Slf4j
@Component
public class UnstructuredLoader {
    /*If True, return coordinates for each element extracted via OCR. Default: False */
    private static final Boolean DEFAULT_COORDINATES = false;
    /* The encoding method used to decode the text input. Default: utf-8 */
    private static final String DEFAULT_ENCODING = "UTF-8";
    /* If true, the output will include page breaks if the filetype supports it. Default: false */
    private static final Boolean DEFAULT_INCLUDE_PAGE_BREAKS = false;
    /* The format of the response. Supported formats are application/json and text/csv. Default: application/json. */
    private static final String DEFAULT_OUTPUT_FORMAT = "application/json";
    /* Deprecated! Use skip_infer_table_types to opt out of table extraction for any file type. If False and strategy=hi_res, no Table Elements will be extracted from pdf files regardless of skip_infer_table_types contents. */
    private static final Boolean DEFAULT_PDF_INFER_TABLE_STRUCTURE = true;
    /* The document types that you want to skip table extraction with. Default: [] */
    private static final String[] DEFAULT_SKIP_INFER_TABLE_TYPES = new String[]{};
    /* The strategy to use for partitioning PDF/image. Options are fast, hi_res, auto. Default: hi_res Available options: fast, hi_res, auto, ocr_only */
    private static final String DEFAULT_STRATEGY = "hi_res";
    /* When True, assign UUIDs to element IDs, which guarantees their uniqueness (useful when using them as primary keys in database). Otherwise, an SHA-256 of element text is used. Default: False */
    private static final Boolean DEFAULT_UNIQUE_ELEMENT_IDS = false;
    /* If True, will retain the XML tags in the output. Otherwise, it will simply extract the text from within the tags. Only applies to XML documents. */
    private static final Boolean DEFAULT_XML_KEEP_TAGS = false;
    /* If chunking strategy is set, combine elements until a section reaches a length of n chars. Default: 500 */
    private static final Integer DEFAULT_COMBINE_UNDER_N_CHARS = 500;
    /* When a chunking strategy is specified, each returned chunk will include the elements consolidated to form that chunk as .metadata.orig_elements. Default: true. */
    private static final Boolean DEFAULT_INCLUDE_ORIG_ELEMENTS = true;
    /* If chunking strategy is set, cut off new sections after reaching a length of n chars (hard max). Default: 500 */
    private static final Integer DEFAULT_MAX_CHARACTERS = 500;
    /* If chunking strategy is set, determines if sections can span multiple sections. Default: true */
    private static final Boolean DEFAULT_MULTIPAGE_SECTIONS = true;
    /* If chunking strategy is set, cut off new sections after reaching a length of n chars (soft max). Default: 1500 */
    private static final  Integer DEFAULT_NEW_AFTER_N_CHARS = 1500;
    /* Specifies the length of a string ('tail') to be drawn from each chunk and prefixed to the next chunk as a context-preserving mechanism. By default, this only applies to split-chunks where an oversized element is divided into multiple chunks by text-splitting. Default: 0 */
    private static final Integer DEFAULT_OVERLAP = 0;
    /* When True, apply overlap between 'normal' chunks formed from whole elements and not subject to text-splitting. Use this with caution as it entails a certain level of 'pollution' of otherwise clean semantic chunk boundaries. Default: False */
    private static final Boolean DEFAULT_OVERLAP_ALL = false;
    /* When True, slide notes from .ppt and .pptx files will be included in the response. Default: True */
    private static final Boolean DEFAULT_INCLUDE_SLIDE_NOTES = true;

    private static final String UNSTRUCTURED_API_URL = "https://api.unstructured.io/general/v0/general";

    @Value("${unstructured.api.key}")
    private String UNSTRUCTURED_API_KEY;

    public List<Document> load(MultipartFile multipartFile, Map<String, Object> attributes) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        if (attributes == null){
            attributes = new HashMap<>();
        }

        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.add("accept", "application/json");
        headers.add("unstructured-api-key", UNSTRUCTURED_API_KEY);

        File file = File.createTempFile(UUID.randomUUID().toString(), multipartFile.getOriginalFilename());
        multipartFile.transferTo(file);

        FileSystemResource resource = new FileSystemResource(file);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("files", resource);
        body.add("coordinates", attributes.getOrDefault("coordinates", DEFAULT_COORDINATES));
        body.add("encoding", attributes.getOrDefault("encoding", DEFAULT_ENCODING));
        body.add("include_page_breaks", attributes.getOrDefault("include_page_breaks", DEFAULT_INCLUDE_PAGE_BREAKS));
        body.add("output_format", attributes.getOrDefault("output_format", DEFAULT_OUTPUT_FORMAT));
        body.add("pdf_infer_table_structure", attributes.getOrDefault("pdf_infer_table_structure", DEFAULT_PDF_INFER_TABLE_STRUCTURE));
        body.add("skip_infer_table_types", attributes.getOrDefault("skip_infer_table_types", DEFAULT_SKIP_INFER_TABLE_TYPES));
        body.add("strategy", attributes.getOrDefault("strategy", DEFAULT_STRATEGY));
        body.add("unique_element_ids", attributes.getOrDefault("unique_element_ids", DEFAULT_UNIQUE_ELEMENT_IDS));
        body.add("xml_keep_tags", attributes.getOrDefault("xml_keep_tags", DEFAULT_XML_KEEP_TAGS));
        body.add("combine_under_n_chars", attributes.getOrDefault("combine_under_n_chars", DEFAULT_COMBINE_UNDER_N_CHARS));
        body.add("include_orig_elements", attributes.getOrDefault("include_orig_elements", DEFAULT_INCLUDE_ORIG_ELEMENTS));
        body.add("max_characters", attributes.getOrDefault("max_characters", DEFAULT_MAX_CHARACTERS));
        body.add("multipage_sections", attributes.getOrDefault("multipage_sections", DEFAULT_MULTIPAGE_SECTIONS));
        body.add("new_after_n_chars", attributes.getOrDefault("new_after_n_chars", DEFAULT_NEW_AFTER_N_CHARS));
        body.add("overlap", attributes.getOrDefault("overlap", DEFAULT_OVERLAP));
        body.add("overlap_all", attributes.getOrDefault("overlap_all", DEFAULT_OVERLAP_ALL));
        body.add("include_slide_notes", attributes.getOrDefault("include_slide_notes", DEFAULT_INCLUDE_SLIDE_NOTES));

        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                UNSTRUCTURED_API_URL,
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<>() {}
        );

        List<Map<String, Object>> results = response.getBody();

        ObjectMapper objMapper = new ObjectMapper();

        return Objects.requireNonNull(results).stream().map(result ->
                {
                    try {
                        Object metadataObj = result.get("metadata");
                        @SuppressWarnings("unchecked")
                        Map<String, Object> metadata = (Map<String, Object>) metadataObj;
                        return new Document(objMapper.writeValueAsString(result.get("text")).replace("\n", ""), metadata);
                    } catch (JsonProcessingException e) {
                        log.warn("UnstructuredLoader Error : {}", e.getMessage());
                        throw new UnstructuredLoaderException("objMapper.writeValueAsString(result.get(\"text\")) Error");
                    }
                }).toList();
    }

    class UnstructuredLoaderException extends ValidationException {
        public UnstructuredLoaderException(String message) {
            super(message);
        }
    }

}
