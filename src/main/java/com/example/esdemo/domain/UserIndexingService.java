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

    @PostConstruct
    public void indexingUserDateV2() {
        IndexCoordinates indexName = IndexUtil.createIndexNameWithPostFixWrapper(INDEX_PREFIX_NAME);
        IndexCoordinates aliasNameWrapper = IndexUtil.createIndexNameWrapper(ALIAS_NAME);

        userDocumentRepository.createIndex(UserDocument.class, indexName); // index 생성
        userDocumentRepository.updateAliases(indexName, aliasNameWrapper); // alias 지정
        userDocumentRepository.saveAll(createMockIndexingUsers(), indexName); // 데이터 저장
        userDocumentRepository.deleteIndex(IndexUtil.createIndexNameWithBeforeDateWildcardFixWrapper(INDEX_PREFIX_NAME, 7)); // 7일 이전 index 삭제
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


