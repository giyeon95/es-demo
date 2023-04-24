package com.example.esdemo.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;

public class IndexUtil {

    private static final String DATE_PARRERN = "yy.MM.dd";

    public static IndexCoordinates createIndexNameWithPostFixWrapper(String indexName) {
        return IndexCoordinates.of(indexName + "_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_PARRERN + "HHmmss")));
    }

    public static IndexCoordinates createIndexNameWrapper(String indexName) {
        return IndexCoordinates.of(indexName);
    }

    public static IndexCoordinates createIndexNameWithBeforeDateWildcardFixWrapper(String indexName, long beforeDay) {
        return IndexCoordinates.of(
            indexName + "_" + LocalDateTime.now().minusDays(beforeDay).format(DateTimeFormatter.ofPattern(DATE_PARRERN)) + "*");
    }

}
