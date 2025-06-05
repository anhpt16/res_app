package com.anhpt.res_app.web.service;

import com.anhpt.res_app.common.dto.response.PageResponse;
import com.anhpt.res_app.common.entity.Collection;
import com.anhpt.res_app.common.enums.status.CollectionStatus;
import com.anhpt.res_app.common.repository.CollectionRepository;
import com.anhpt.res_app.web.dto.WebCollectionMapper;
import com.anhpt.res_app.web.dto.request.CollectionGetRequest;
import com.anhpt.res_app.web.dto.response.CollectionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class WebCollectionService {
    private final CollectionRepository collectionRepository;
    private final WebCollectionMapper webCollectionMapper;

    public PageResponse<CollectionResponse> get(
        CollectionGetRequest request
    ) {
        PageRequest pageRequest = PageRequest.of(
            request.getPage() - 1,
            request.getSize()
        );
        Page<Collection> collections = collectionRepository.findByStatusOrderByPublishedAtDesc(CollectionStatus.PUBLISHED, pageRequest);
        if (request.getPage() > collections.getTotalPages()) {
            throw new IllegalArgumentException("Trang không tồn tại");
        }
        List<CollectionResponse> collectionResponses = collections.stream()
            .map(webCollectionMapper::toCollectionResponse)
            .toList();
        return new PageResponse<>(
            collectionResponses,
            request.getPage(),
            request.getSize(),
            collections.getTotalElements(),
            collections.getTotalPages()
        );
    }
}
