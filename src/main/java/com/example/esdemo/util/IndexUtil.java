package com.example.esdemo.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;

public class IndexUtil {

    public static IndexCoordinates createIndexNameWithPostFixWrapper(String indexName) {
        return IndexCoordinates.of(indexName + "_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy.MM.dd.HHmmss")));
    }

    public static IndexCoordinates createIndexNameWrapper(String indexName) {
        return IndexCoordinates.of(indexName);
    }

}
