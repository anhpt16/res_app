package com.anhpt.res_app.admin.service;

import com.anhpt.res_app.admin.dto.AdminPostMapper;
import com.anhpt.res_app.admin.dto.request.post.PostCreateRequest;
import com.anhpt.res_app.admin.dto.request.post.PostSearchRequest;
import com.anhpt.res_app.admin.dto.request.post.PostUpdateRequest;
import com.anhpt.res_app.admin.dto.request.post.PostUpdateStatusRequest;
import com.anhpt.res_app.admin.dto.response.post.PostResponse;
import com.anhpt.res_app.admin.dto.response.post.PostShortResponse;
import com.anhpt.res_app.admin.filter.AdminPostFilter;
import com.anhpt.res_app.admin.validation.PostValidation;
import com.anhpt.res_app.common.dto.response.PageResponse;
import com.anhpt.res_app.common.entity.*;
import com.anhpt.res_app.common.enums.status.PostStatus;
import com.anhpt.res_app.common.exception.ResourceNotFoundException;
import com.anhpt.res_app.common.repository.MediaRepository;
import com.anhpt.res_app.common.repository.PostRepository;
import com.anhpt.res_app.common.repository.TagRepository;
import com.anhpt.res_app.common.utils.CustomSlugify;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminPostService {
    private final AdminPostMapper adminPostMapper;
    private final PostValidation postValidation;
    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final MediaRepository mediaRepository;
    private final AdminPostFilter adminPostFilter;

    @Transactional
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
        if (request.getThumbnail() != null) {
            Media media = mediaRepository.findById(request.getThumbnail())
                .orElseThrow(() -> new ResourceNotFoundException("Không tồn tại media id: " + request.getThumbnail()));
            post.setMedia(media);
        }
        if (request.getTagIds() != null) {
            Set<Long> newTagIds = new HashSet<>(request.getTagIds());
            List<Tag> tags = tagRepository.findAllById(newTagIds);
            if (tags.size() != newTagIds.size()) {
                throw new ResourceNotFoundException("Có lỗi khi tìm kiếm thẻ");
            }
            List<TagPost> tagPosts = new ArrayList<>();
            for (Tag tag : tags) {
                TagPost tagPost = new TagPost();
                tagPost.setPost(post);
                tagPost.setTag(tag);
                tagPosts.add(tagPost);
            }
            post.getTagPosts().addAll(tagPosts);
        }

        post.setTitle(request.getTitle());
        post.setSlug(slug);
        post.setUser(user);
        post.setStatus(PostStatus.DRAFT);
        post.setContent(request.getContent());
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());
        post = postRepository.save(post);
        return adminPostMapper.toPostResponse(post);
    }

    @Transactional
    public PostResponse update(PostUpdateRequest request, Long postId) {
        // TODO: Lấy user hiện tại
        postValidation.validatePostUpdate(request, postId);
        Optional<Post> post = postRepository.findById(postId);
        if (!post.isPresent()) {
            throw new ResourceNotFoundException("Không tồn tại bài viết id:" + postId);
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
        // Cập nhật thumbnail
        if (request.getThumbnail() != null) {
            Media media = mediaRepository.findById(request.getThumbnail())
                .orElseThrow(() -> new ResourceNotFoundException("Không tồn tại media id: " + request.getThumbnail()));
            post.get().setMedia(media);
        }
        // Cập nhật thẻ
        if (request.getTagIds() != null) {
            Set<Long> newTagIds = new HashSet<>(request.getTagIds());
            Set<Long> currentTagIds = post.get().getTagPosts().stream()
                .map(tp -> tp.getTag().getId())
                .collect(Collectors.toSet());
            // Nếu các thẻ có sự thay đổi -> xóa và tạo mới các thẻ
            if (!currentTagIds.equals(newTagIds)) {
                List<Tag> tags = tagRepository.findAllById(newTagIds);
                if (tags.size() != newTagIds.size()) {
                    throw new ResourceNotFoundException("Có lỗi khi tìm kiếm thẻ");
                }
                post.get().getTagPosts().clear();
                List<TagPost> tagPosts = new ArrayList<>();
                for (Tag tag : tags) {
                    TagPost tagPost = new TagPost();
                    tagPost.setPost(post.get());
                    tagPost.setTag(tag);
                    tagPosts.add(tagPost);
                }
                post.get().getTagPosts().addAll(tagPosts);
            }
        }
        post.get().setUpdatedAt(LocalDateTime.now());
        Post postUpdated = postRepository.save(post.get());
        return adminPostMapper.toPostResponse(postUpdated);
    }

    public PostResponse updateStatus(Long postId, PostUpdateStatusRequest request) {
        Optional<Post> post = postRepository.findById(postId);
        if (!post.isPresent()) {
            throw new ResourceNotFoundException("Không tồn tại bài viết id: " + postId);
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
        return adminPostMapper.toPostResponse(postUpdated);
    }

    public void delete(Long postId) {
        postValidation.validatePostDelete(postId);
        postRepository.deleteById(postId);
    }

    public PostResponse getById(Long postId) {
        Optional<Post> post = postRepository.findById(postId);
        if (!post.isPresent()) {
            throw new ResourceNotFoundException("Không tồn tại bài viết id: " + postId);
        }
        return adminPostMapper.toPostResponse(post.get());
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
        if (pageResult.getTotalPages() > 0 && request.getPage() > pageResult.getTotalPages()) {
            throw new IllegalArgumentException("Trang không tồn tại");
        }
        List<PostShortResponse> postShortResponses = pageResult.getContent().stream()
            .map(adminPostMapper::toPostShortResponse)
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
