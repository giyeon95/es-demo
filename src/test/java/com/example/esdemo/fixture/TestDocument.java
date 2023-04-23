package com.example.esdemo.fixture;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(indexName = "user", createIndex = false)
public class TestDocument {

    @Id
    private Long id;

    @Field(name = "name", type = FieldType.Text)
    private String name;
}
