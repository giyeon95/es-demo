package com.example.esdemo.domain;

import com.example.esdemo.util.IndexUtil;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserIndexingService {

    private final UserDocumentRepository userDocumentRepository;

    private static final String INDEX_PREFIX_NAME = "user";
    private static final String ALIAS_NAME = "user";

//    @PostConstruct
//    public void indexingUserDateV1() {
//        IndexCoordinates indexNameWrapper = IndexUtil.createIndexNameWithPostFixWrapper(INDEX_PREFIX_NAME);
//        IndexCoordinates aliasNameWrapper = IndexUtil.createIndexNameWrapper(ALIAS_NAME);
//
//        Set<String> existIndexNames = userDocumentRepository.findIndexNamesByAlias(aliasNameWrapper); // alias로 등록된 index 조회
//        userDocumentRepository.saveAll(createMockIndexingUsers(), indexNameWrapper); // index 생성
//
//        existIndexNames.forEach(indexName -> {
//            userDocumentRepository.deleteIndex(IndexCoordinates.of(indexName)); // alias로 등록된 index 삭제
//        });
//
//        userDocumentRepository.setAlias(indexNameWrapper, aliasNameWrapper); // alias 등록
//    }

    @PostConstruct
    public void indexingUserDateV2() {
        IndexCoordinates indexNameWrapper = IndexUtil.createIndexNameWithPostFixWrapper(INDEX_PREFIX_NAME);
        IndexCoordinates aliasNameWrapper = IndexUtil.createIndexNameWrapper(ALIAS_NAME);

        Set<String> existIndexNames = userDocumentRepository.findIndexNamesByAlias(aliasNameWrapper); // alias로 등록된 index 조회
        userDocumentRepository.saveAll(createMockIndexingUsers(), indexNameWrapper); // index 생성

        userDocumentRepository.updateAliases(indexNameWrapper, aliasNameWrapper); // alias 업데이트
//        existIndexNames.forEach(indexName -> {
//            userDocumentRepository.deleteIndex(IndexCoordinates.of(indexName)); // alias로 등록된 index 삭제
//        });
    }


    public List<UserDocument> createMockIndexingUsers() {
        return List.of(
            new UserDocument(1L, "user1", 1L, 20L, 30L, 40L),
            new UserDocument(2L, "user2", 1L, 2L, 3L, 5L),
            new UserDocument(3L, "user3", 1L, 5L, 10L, 20L),
            new UserDocument(4L, "user4", 1L, 2L, 4L, null),
            new UserDocument(5L, "user5", 1L, 3L, null, null)
        );

    }
}


