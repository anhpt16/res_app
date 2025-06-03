package com.anhpt.res_app.admin.service;

import com.anhpt.res_app.admin.dto.PostMapper;
import com.anhpt.res_app.admin.dto.request.post.PostCreateRequest;
import com.anhpt.res_app.admin.dto.request.post.PostSearchRequest;
import com.anhpt.res_app.admin.dto.request.post.PostUpdateRequest;
import com.anhpt.res_app.admin.dto.request.post.PostUpdateStatusRequest;
import com.anhpt.res_app.admin.dto.response.post.PostResponse;
import com.anhpt.res_app.admin.dto.response.post.PostShortResponse;
import com.anhpt.res_app.admin.filter.AdminPostFilter;
import com.anhpt.res_app.admin.validation.PostValidation;
import com.anhpt.res_app.common.dto.response.PageResponse;
import com.anhpt.res_app.common.entity.Post;
import com.anhpt.res_app.common.entity.User;
import com.anhpt.res_app.common.enums.status.PostStatus;
import com.anhpt.res_app.common.repository.PostRepository;
import com.anhpt.res_app.common.utils.CustomSlugify;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminPostService {
    private final PostMapper postMapper;
    private final PostValidation postValidation;
    private final PostRepository postRepository;
    private final AdminPostFilter adminPostFilter;

    public PostResponse create(PostCreateRequest request) {
        // TODO: Lấy user hiện tại
        postValidation.validatePostCreate(request);
        // Slug
        String slug = CustomSlugify.slugify(request.getTitle());
        while (postRepository.existsBySlug(slug)) {
            slug = slug + "-" + UUID.randomUUID().toString().substring(0, 5);
        }
        // Post
        User user = new User();
        user.setId(1L);

        Post post = new Post();
        post.setTitle(request.getTitle());
        post.setSlug(slug);
        post.setUser(user);
        post.setStatus(PostStatus.DRAFT);
        post.setContent(request.getContent());
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());
        post = postRepository.save(post);
        return postMapper.toPostResponse(post);
    }

    public PostResponse update(PostUpdateRequest request, Long postId) {
        // TODO: Lấy user hiện tại
        postValidation.validatePostUpdate(request, postId);
        Optional<Post> post = postRepository.findById(postId);
        if (!post.isPresent()) {
            throw new IllegalArgumentException("Không tồn tại bài viết id:" + postId);
        }
        // Cập nhật tiêu đề
        if (request.getTitle() != null) {
            String slug = CustomSlugify.slugify(request.getTitle());
            while (postRepository.existsBySlug(slug)) {
                slug = slug + "-" + UUID.randomUUID().toString().substring(0, 5);
            }
            post.get().setSlug(slug);
            post.get().setTitle(request.getTitle());
        }
        // Cập nhật nội dung
        if (request.getContent() != null) {
            post.get().setContent(request.getContent());
        }
        post.get().setUpdatedAt(LocalDateTime.now());
        Post postUpdated = postRepository.save(post.get());
        return postMapper.toPostResponse(postUpdated);
    }

    public PostResponse updateStatus(Long postId, PostUpdateStatusRequest request) {
        Optional<Post> post = postRepository.findById(postId);
        if (!post.isPresent()) {
            throw new IllegalArgumentException("Không tồn tại bài viết id: " + postId);
        }
        PostStatus postStatus = PostStatus.fromCode(request.getStatus());
        // Kiểm tra trạng thái hiện tại của bài viết
        if (post.get().getStatus().equals(postStatus)) {
            throw new IllegalArgumentException("Trạng thái đã tồn tại: " + postStatus);
        }
        post.get().setStatus(postStatus);
        // Kiểm tra bài viết đã phát hành
        if (postStatus.equals(PostStatus.PUBLISHED)) {
            // Nếu chưa phát hành lần nào thì cập nhật thời điểm phát hành
            if (post.get().getStatus() != null) {
                post.get().setPublishedAt(LocalDateTime.now());
            }
        }
        Post postUpdated = postRepository.save(post.get());
        return postMapper.toPostResponse(postUpdated);
    }

    public void delete(Long postId) {
        postValidation.validatePostDelete(postId);
        postRepository.deleteById(postId);
    }

    public PageResponse<PostShortResponse> get(PostSearchRequest request) {
        // TODO: Lấy user hiện tại
        postValidation.validatePostSearch(request);
        User user = new User();
        user.setId(1L);

        PageRequest pageRequest = PageRequest.of(
            request.getPage() - 1,
            request.getSize()
        );
        Page<Post> pageResult = postRepository.findAll(
            adminPostFilter.searchPost(request, user),
            pageRequest
        );
        
        List<PostShortResponse> postShortResponses = pageResult.getContent().stream()
            .map(postMapper::toPostShortResponse)
            .toList();

        return new PageResponse<>(
            postShortResponses,
            request.getPage(),
            request.getSize(),
            pageResult.getTotalElements(),
            pageResult.getTotalPages()
        );
    }
}
