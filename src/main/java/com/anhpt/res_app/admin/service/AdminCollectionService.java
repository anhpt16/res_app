package com.anhpt.res_app.admin.service;

import com.anhpt.res_app.admin.dto.AdminCollectionMapper;
import com.anhpt.res_app.admin.dto.request.collection.CollectionCreateRequest;
import com.anhpt.res_app.admin.dto.request.collection.CollectionSearchRequest;
import com.anhpt.res_app.admin.dto.request.collection.CollectionUpdateRequest;
import com.anhpt.res_app.admin.dto.response.collection.CollectionResponse;
import com.anhpt.res_app.admin.dto.response.collection.CollectionShortResponse;
import com.anhpt.res_app.admin.filter.AdminCollectionFilter;
import com.anhpt.res_app.admin.validation.AdminCollectionValidation;
import com.anhpt.res_app.common.dto.response.PageResponse;
import com.anhpt.res_app.common.entity.Collection;
import com.anhpt.res_app.common.entity.Media;
import com.anhpt.res_app.common.entity.User;
import com.anhpt.res_app.common.enums.status.CollectionStatus;
import com.anhpt.res_app.common.exception.ResourceNotFoundException;
import com.anhpt.res_app.common.repository.CollectionRepository;
import com.anhpt.res_app.common.repository.MediaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminCollectionService {
    private final CollectionRepository collectionRepository;
    private final MediaRepository mediaRepository;
    private final AdminCollectionValidation adminCollectionValidation;
    private final AdminCollectionMapper adminCollectionMapper;
    private final AdminCollectionFilter adminCollectionFilter;

    public CollectionResponse create(CollectionCreateRequest request) {
        // TODO: Xử lý user
        User user = new User();
        user.setId(1L);

        adminCollectionValidation.validateCreate(request, user);
        // Lấy Media
        Media media = mediaRepository.findById(request.getMediaId())
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tệp có id: " + request.getMediaId()));

        Collection collection = new Collection();
        collection.setMedia(media);
        collection.setName(request.getName());
        collection.setUser(user);
        collection.setStatus(CollectionStatus.fromCode(request.getStatus()));
        collection.setDisplayOrder(request.getDisplayOrder());
        collection.setCreatedAt(LocalDateTime.now());
        collection.setUpdatedAt(LocalDateTime.now());
        if (CollectionStatus.fromCode(request.getStatus()).equals(CollectionStatus.PUBLISHED)) {
            collection.setPublishedAt(LocalDateTime.now());
        }
        collection = collectionRepository.save(collection);
        return adminCollectionMapper.toCollectionResponse(collection);
    }

    public CollectionResponse update(Long collectionId, CollectionUpdateRequest request) {
        adminCollectionValidation.validateUpdate(collectionId, request);

        Collection collection = collectionRepository.findById(collectionId)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bộ sưu tập có id: " + collectionId));
        
        if (request.getName() != null) {
            collection.setName(request.getName());
        }
        if (request.getStatus() != null) {
            CollectionStatus status = CollectionStatus.fromCode(request.getStatus());
            if (status.equals(CollectionStatus.PUBLISHED) && collection.getPublishedAt() == null) {
                collection.setPublishedAt(LocalDateTime.now());
            }
            collection.setStatus(status);
        }
        if (request.getDisplayOrder() != null) {
            collection.setDisplayOrder(request.getDisplayOrder());
        }
        collection.setUpdatedAt(LocalDateTime.now());
        collection = collectionRepository.save(collection);
        return adminCollectionMapper.toCollectionResponse(collection);
    }

    public void delete(Long CollectionId) {
        adminCollectionValidation.validateDelete(CollectionId);
        collectionRepository.deleteById(CollectionId);
    }

    public CollectionResponse getById(Long CollectionId) {
        Collection collection = collectionRepository.findById(CollectionId)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bộ sưu tập có id: " + CollectionId));
        return adminCollectionMapper.toCollectionResponse(collection);
    }

    public PageResponse<CollectionShortResponse> get(CollectionSearchRequest request) {
        adminCollectionValidation.validateSearch(request);

        PageRequest pageRequest = PageRequest.of(
            request.getPage() - 1,
            request.getSize()
        );

        Page<Collection> pageResult = collectionRepository.findAll(
            adminCollectionFilter.searchCollection(request),
            pageRequest
        );
        if (pageResult.getTotalPages() > 0 && request.getPage() > pageResult.getTotalPages()) {
            throw new IllegalArgumentException("Trang không tồn tại");
        }
        List<CollectionShortResponse> collectionShortResponses = pageResult.getContent().stream()
            .map(adminCollectionMapper::toCollectionShortResponse)
            .toList();
        return new PageResponse<>(
            collectionShortResponses,
            request.getPage(),
            request.getSize(),
            pageResult.getTotalElements(),
            pageResult.getTotalPages()
        );
    }
}
