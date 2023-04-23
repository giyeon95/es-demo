package com.example.esdemo.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(indexName = "user", createIndex = false)
public class UserDocument {

    @Id
    private Long id;

    @Field(name = "name", type = FieldType.Text)
    private String name;

    @Field(name = "category_id1", type = FieldType.Long, copyTo = "category_ids")
    private Long category_id1;

    @Field(name = "category_id2", type = FieldType.Long, copyTo = "category_ids")
    private Long categoryId2;


    @Field(name = "category_id3", type = FieldType.Long, copyTo = "category_ids")
    private Long categoryId3;

    @Field(name = "category_id4", type = FieldType.Long, copyTo = "category_ids")
    private Long categoryId4;

    @Field(name = "category_ids", type = FieldType.Text, analyzer = "standard")
    private String categoryIds;

    @Builder
    public UserDocument(Long id, String name, Long category_id1, Long categoryId2, Long categoryId3, Long categoryId4) {
        this.id = id;
        this.name = name;
        this.category_id1 = category_id1;
        this.categoryId2 = categoryId2;
        this.categoryId3 = categoryId3;
        this.categoryId4 = categoryId4;
    }
}
