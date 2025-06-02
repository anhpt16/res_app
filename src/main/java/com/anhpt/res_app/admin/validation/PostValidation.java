package com.anhpt.res_app.admin.validation;

import com.anhpt.res_app.admin.dto.request.post.PostCreateRequest;
import com.anhpt.res_app.admin.dto.request.post.PostSearchRequest;
import com.anhpt.res_app.admin.dto.request.post.PostUpdateRequest;
import com.anhpt.res_app.common.entity.Post;
import com.anhpt.res_app.common.entity.User;
import com.anhpt.res_app.common.enums.status.PostStatus;
import com.anhpt.res_app.common.exception.MultiDuplicateException;
import com.anhpt.res_app.common.exception.ResourceNotFoundException;
import com.anhpt.res_app.common.repository.PostRepository;
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
public class PostValidation {
    private final PostRepository postRepository;

    public void validatePostCreate(PostCreateRequest request) {
        Map<String, String> errors = new HashMap<>();
        if (postRepository.existsByTitle(request.getTitle())) {
            String field = FieldNameUtil.getFieldName(PostCreateRequest::getTitle);
            errors.put(field, "Đã tồn tại");
        }
        if (errors.size() > 0) {
            throw new MultiDuplicateException(errors);
        }
    }

    public void validatePostUpdate(PostUpdateRequest request, Long postId) {
        User user = new User();
        user.setId(1L);
        Map<String, String> errors = new HashMap<>();
        // Kiểm tra không có dữ liệu gửi lên
        if (request.isEmpty()) {
            throw new IllegalArgumentException("Dữ liệu không hợp lệ");
        }
        Optional<Post> post = postRepository.findByIdAndUser(postId, user);
        // Kiểm tra user-post
        if (!post.isPresent()) {
            log.warn("Không tìm thấy userId: {} - postId: {}", user.getId(), postId);
            throw new ResourceNotFoundException("Không tìm thấy bài viết");
        }
        // Kiểm tra post có nằm trong trạng thái phát hành
        if (post.get().getStatus() == PostStatus.PUBLISHED) {
            throw new IllegalArgumentException("Bài viết đã được phát hành");
        }
        // Kiểm tra trùng tiêu đề
        Optional<Post> postByTitle = postRepository.findByTitle(request.getTitle());
        if (postByTitle.isPresent() && !postByTitle.get().getId().equals(postId)) {
            String field = FieldNameUtil.getFieldName(PostCreateRequest::getTitle);
            errors.put(field, "Đã tồn tại");
        } else {
            request.setTitle(null);
        }
        if (errors.size() > 0) {
            throw new MultiDuplicateException(errors);
        }
    }

    public void validatePostSearch(PostSearchRequest request) {

    }

    // Quản lý cấp cao

    public void validatePostDelete(Long postId) {   
        Optional<Post> post = postRepository.findById(postId);
        if (!post.isPresent()) {
            log.warn("Không tìm thấy postId: {}", postId);
            throw new ResourceNotFoundException("Không tìm thấy bài viết");
        }
    }
}