package com.anhpt.res_app.web.service;

import com.anhpt.res_app.common.entity.Tag;
import com.anhpt.res_app.common.repository.TagRepository;
import com.anhpt.res_app.web.dto.request.PostGetRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WebTagService {
    private final TagRepository tagRepository;

    public String getTagNameBySlug(PostGetRequest request) {
        if (request.getTagSlug() == null) return null;
        Optional<Tag> tag = tagRepository.findBySlug(request.getTagSlug());
        if (tag.isEmpty()) return null;
        return tag.get().getName();
    }
}
