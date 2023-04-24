package com.example.esdemo.fixture;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.InnerField;
import org.springframework.data.elasticsearch.annotations.MultiField;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(indexName = "test", createIndex = false)
public class TestDocument {

    @Id
    private Long id;

    @MultiField(
        mainField = @Field(name = "name", type = FieldType.Text, copyTo = "standard_name"),
        otherFields = {
            @InnerField(suffix = "keyword", type = FieldType.Keyword, ignoreAbove = 10)
        }
    )
    private String name;


    @Field(name = "code", type = FieldType.Keyword)
    private String code;


    @Field(name = "standard_name", type = FieldType.Text, analyzer = "standard")
    private String standardName;


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
