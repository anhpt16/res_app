package com.anhpt.res_app.admin.service;

import com.anhpt.res_app.admin.dto.AdminTagMapper;
import com.anhpt.res_app.admin.dto.request.TagCreateRequest;
import com.anhpt.res_app.admin.dto.response.TagResponse;
import com.anhpt.res_app.admin.validation.TagValidation;
import com.anhpt.res_app.common.dto.response.PageResponse;
import com.anhpt.res_app.common.entity.Tag;
import com.anhpt.res_app.common.exception.ResourceNotFoundException;
import com.anhpt.res_app.common.repository.TagRepository;
import com.anhpt.res_app.common.utils.CustomSlugify;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminTagService {
    private final TagRepository tagRepository;
    private final TagValidation tagValidation;
    private final AdminTagMapper adminTagMapper;

    public TagResponse create(TagCreateRequest request) {
        tagValidation.validateTagCreate(request);
        // Slug
        String slug = CustomSlugify.slugify(request.getName());
        while (tagRepository.existsBySlug(slug)) {
            slug = slug + "-" + UUID.randomUUID().toString().substring(0, 5);
        }
        // Tag
        Tag tag = new Tag();
        tag.setName(request.getName());
        tag.setSlug(slug);
        tag.setCreatedAt(LocalDateTime.now());
        tag.setUpdatedAt(LocalDateTime.now());
        tag = tagRepository.save(tag);
    
        return adminTagMapper.toTagResponse(tag);
    }

    public TagResponse update(Long tagId, TagCreateRequest request) {
        tagValidation.validateTagUpdate(tagId, request);
        Optional<Tag> tag = tagRepository.findById(tagId);
        // Slug
        String slug = CustomSlugify.slugify(request.getName());
        while (tagRepository.existsBySlug(slug)) {
            slug = slug + "-" + UUID.randomUUID().toString().substring(0, 5);
        }
        // Tag
        Tag tagUpdate = tag.get();
        tagUpdate.setName(request.getName());
        tagUpdate.setSlug(slug);
        tagUpdate.setUpdatedAt(LocalDateTime.now());
        tagUpdate = tagRepository.save(tagUpdate);
        return adminTagMapper.toTagResponse(tagUpdate);
    }

    public void delete(Long tagId) {
        Tag tag = tagRepository.findById(tagId)
            .orElseThrow(() -> new ResourceNotFoundException("Tag không tồn tại id:" + tagId));
        // TODO: Kiểm tra xem tag có được sử dụng trong bài viết không
        tagRepository.delete(tag);
    }

    public PageResponse<TagResponse> get(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Tag> tags = tagRepository.findAll(pageable);
        if (tags.getTotalPages() > 0 && page > tags.getTotalPages()) {
            throw new IllegalArgumentException("Trang không tồn tại");
        }
        List<TagResponse> tagResponses = tags.getContent().stream()
            .map(adminTagMapper::toTagResponse)
            .collect(Collectors.toList());
        return new PageResponse<>(
            tagResponses,
            tags.getNumber() + 1,
            tags.getSize(),
            tags.getTotalElements(),
            tags.getTotalPages()
        );
    }

}
