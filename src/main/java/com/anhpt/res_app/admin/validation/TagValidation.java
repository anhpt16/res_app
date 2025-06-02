package com.anhpt.res_app.admin.validation;

import com.anhpt.res_app.admin.dto.request.TagCreateRequest;
import com.anhpt.res_app.common.entity.Tag;
import com.anhpt.res_app.common.exception.MultiDuplicateException;
import com.anhpt.res_app.common.repository.TagRepository;
import com.anhpt.res_app.common.utils.function.FieldNameUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class TagValidation {
    private final TagRepository tagRepository;

    public void validateTagCreate(TagCreateRequest request) {
        Map<String, String> errors = new HashMap<>();
        if (tagRepository.existsByName(request.getName())) {
            String field = FieldNameUtil.getFieldName(TagCreateRequest::getName);
            errors.put(field, "Đã đã tồn tại");
        }
        if (errors.size() > 0) {
            throw new MultiDuplicateException(errors);
        }
    }

    public void validateTagUpdate(Long tagId, TagCreateRequest request) {
        Map<String, String> errors = new HashMap<>();
        // Kiểm tra tồn tại
        Optional<Tag> tag = tagRepository.findById(tagId);
        if (!tag.isPresent()) {
            String field = FieldNameUtil.getFieldName(TagCreateRequest::getName);
            errors.put(field, "Không tồn tại");
            throw new MultiDuplicateException(errors);
        }
        // Kiểm tra trùng tên
        Optional<Tag> tagByName = tagRepository.findByName(request.getName());
        if (tagByName.isPresent() && !tagByName.get().getId().equals(tagId)) {
            String field = FieldNameUtil.getFieldName(TagCreateRequest::getName);
            errors.put(field, "Đã đã tồn tại");
        }
        if (errors.size() > 0) {
            throw new MultiDuplicateException(errors);
        }
    }
}
