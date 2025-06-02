package com.anhpt.res_app.admin.service;

import com.anhpt.res_app.admin.dto.request.PostCreateRequest;
import com.anhpt.res_app.admin.dto.request.PostUpdateRequest;
import com.anhpt.res_app.admin.dto.response.PostResponse;
import com.anhpt.res_app.admin.dto.response.PostShortResponse;
import com.anhpt.res_app.common.dto.response.PageResponse;
import com.anhpt.res_app.common.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminPostService {
    private final PostRepository postRepository;

    public PostResponse create(PostCreateRequest request) {
        return null;
    }

    public PostResponse update(PostUpdateRequest request) {
        return null;
    }

    public void delete(Long id) {

    }

    public PageResponse<PostShortResponse> get() {
        return null;
    }
}
