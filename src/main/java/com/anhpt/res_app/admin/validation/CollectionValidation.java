package com.anhpt.res_app.admin.validation;

import com.anhpt.res_app.admin.dto.request.collection.CollectionCreateRequest;
import com.anhpt.res_app.admin.dto.request.collection.CollectionSearchRequest;
import com.anhpt.res_app.admin.dto.request.collection.CollectionUpdateRequest;
import com.anhpt.res_app.common.entity.Collection;
import com.anhpt.res_app.common.entity.User;
import com.anhpt.res_app.common.enums.status.CollectionStatus;
import com.anhpt.res_app.common.exception.ForbiddenActionException;
import com.anhpt.res_app.common.exception.MultiDuplicateException;
import com.anhpt.res_app.common.exception.ResourceNotFoundException;
import com.anhpt.res_app.common.repository.CollectionRepository;
import com.anhpt.res_app.common.utils.function.FieldNameUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
@RequiredArgsConstructor
@Component
@Slf4j
public class CollectionValidation {
    private final CollectionRepository collectionRepository;

    public void validateCreate(CollectionCreateRequest request, User user) {
        Map<String, String> errors = new HashMap<>();
        // TODO: Kiểm tra (user-media)
        // Kiểm tra tồn tại (collection-media)
        if (collectionRepository.existsByMediaId(request.getMediaId())) {
            String field = FieldNameUtil.getFieldName(CollectionCreateRequest::getMediaId);
            errors.put(field, "Đã tồn tại");
        }
        // Gán trạng thái mặc định nếu không tồn tại
        if (request.getStatus() == null) {
            request.setStatus(CollectionStatus.DRAFT.name());
        }
        // Gán độ ưu tiên mặc định nếu không tồn tại
        if (request.getDisplayOrder() == null) {
            request.setDisplayOrder(0);
        }
        if (errors.size() > 0) {
            throw new MultiDuplicateException(errors);
        }
    }

    public void validateUpdate(Long collectionId, CollectionUpdateRequest request) {
        Map<String, String> errors = new HashMap<>();
        if (request.isEmpty()) {
            throw new IllegalArgumentException("Dữ liệu không hợp lệ");
        }
        // Kiểm tra (Collection-Media)
        Collection collection = collectionRepository.findById(collectionId)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bộ sưu tập"));
        if (request.getStatus() != null) {
            // Kiểm tra trạng thái hợp lệ
            CollectionStatus status = CollectionStatus.fromCode(request.getStatus());
            // Kiểm tra trạng thái trùng lặp
            if (collection.getStatus().equals(status)) {
                String field = FieldNameUtil.getFieldName(CollectionUpdateRequest::getStatus);
                errors.put(field, "Trạng thái không hợp lệ");
            }
        }
        if (errors.size() > 0) {
            throw new MultiDuplicateException(errors);
        }
    }

    public void validateSearch(CollectionSearchRequest request) {
        Map<String, String> errors = new HashMap<>();

        if (errors.size() > 0) {
            throw new MultiDuplicateException(errors);
        }
    }

    // Quản lý cấp cao
    public void validateDelete(Long collectionId) {
        Optional<Collection> collection = collectionRepository.findById(collectionId);
        if (!collection.isPresent()) {
            log.warn("Không tìm thấy collectionId: {}", collectionId);
            throw new ResourceNotFoundException("Không tìm thấy bộ sưu tập");
        }
        if (collection.get().getStatus().equals(CollectionStatus.PUBLISHED)) {
            log.warn("Không thể xóa bộ sưu tập ở trạng thái phát hành: {}", collectionId);
            throw new ForbiddenActionException("Không thể xóa bộ sưu tập");
        }
    }
}
