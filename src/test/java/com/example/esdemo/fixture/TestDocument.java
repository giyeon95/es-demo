package com.example.esdemo.fixture;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(indexName = "test", createIndex = false)
public class TestDocument {

    @Id
    private Long id;

    @Field(name = "name", type = FieldType.Text)
    private String name;

    @Builder
    public TestDocument(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static TestDocument of(Long id, String name) {
        return TestDocument.builder()
            .id(id)
            .name(name)
            .build();
    }
}
