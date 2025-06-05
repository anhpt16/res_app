package com.anhpt.res_app.web.service;

import com.anhpt.res_app.common.dto.response.PageResponse;
import com.anhpt.res_app.common.entity.Post;
import com.anhpt.res_app.common.entity.Tag;
import com.anhpt.res_app.common.entity.TagPost;
import com.anhpt.res_app.common.enums.status.PostStatus;
import com.anhpt.res_app.common.exception.ResourceNotFoundException;
import com.anhpt.res_app.common.repository.PostRepository;
import com.anhpt.res_app.common.utils.Constants;
import com.anhpt.res_app.web.dto.WebPostMapper;
import com.anhpt.res_app.web.dto.WebTagMapper;
import com.anhpt.res_app.web.dto.request.PostGetRequest;
import com.anhpt.res_app.web.dto.response.PostResponse;
import com.anhpt.res_app.web.dto.response.PostShortResponse;
import com.anhpt.res_app.web.dto.response.TagResponse;
import com.anhpt.res_app.web.dto.shared.PostDetail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebPostService {
    private final PostRepository postRepository;
    private final WebPostMapper webPostMapper;
    private final WebTagMapper webTagMapper;

    public PageResponse<PostShortResponse> get(PostGetRequest request) {
        PageRequest pageRequest = PageRequest.of(
            request.getPage() - 1,
            request.getSize(),
            Sort.by("publishedAt").descending()
        );

        Page<Post> posts = request.getTagSlug() == null
            ? postRepository.findByStatus(PostStatus.PUBLISHED, pageRequest)
            : postRepository.findByStatusAndTagSlug(PostStatus.PUBLISHED, request.getTagSlug(), pageRequest);

        if (request.getPage() > posts.getTotalPages()) {
            throw new IllegalArgumentException("Trang không tồn tại");
        }
        List<PostShortResponse> postShortResponses = posts.getContent().stream()
            .map(webPostMapper::toPostShortResponse)
            .toList();

        return new PageResponse<>(
            postShortResponses,
            request.getPage(),
            request.getSize(),
            posts.getTotalElements(),
            posts.getTotalPages()
        );
    }

    // Lấy chi tiết bài viết
    public PostResponse get(String slug) {
        // Lấy chi tiết bài viết
        PostDetail postDetail = getPostDetail(slug);
        // Lấy bài viết liên quan
        List<PostShortResponse> postsRelated = getPostsRelated(postDetail.getId(), postDetail.getTags(), Constants.NUMBER_POST_RELATED);
        return webPostMapper.toPostResponse(postDetail, postsRelated);
    }
    public PostDetail getPostDetail(String slug) {
        Post post = postRepository.findBySlugAndStatus(slug, PostStatus.PUBLISHED)
            .orElseThrow(() -> {
                log.warn("Không tìm thấy bài viết phát hành slug: {}", slug);
                throw new ResourceNotFoundException("Không tìm thấy bài viết được phát hành");
            });
        // Lấy Tags
        List<TagResponse> tagResponses = new ArrayList<>();
        if (!post.getTagPosts().isEmpty()) {
            tagResponses = post.getTagPosts().stream()
                .map(tp -> webTagMapper.toTagResponse(tp.getTag()))
                .collect(Collectors.toList());
        }
        return webPostMapper.toPostDetail(post, tagResponses);
    }
    public List<PostShortResponse> getPostsRelated(Long postId, List<TagResponse> tags, int number) {
        if (number <= 0) {
            return Collections.emptyList();
        }
        // Lấy các bài viết liên quan tới tagIds
        Pageable pageable = PageRequest.of(0, number);
        Set<Long> tagIds = tags.stream()
            .map(TagResponse::getId)
            .collect(Collectors.toSet());
        List<Post> posts = postRepository.findPostsRelated(postId, tagIds, PostStatus.PUBLISHED, pageable);
        // Nếu chưa đủ, lấy thêm các bài viết phát hành gần nhất
        if (posts.size() < number) {
            Set<Long> postIdsExist = posts.stream()
                .map(Post::getId)
                .collect(Collectors.toSet());
            // Thêm Id bài viết hiện tại vào postIdsExist
            postIdsExist.add(postId);
            List<Post> postsAdded = getPostsLatest(number - posts.size(), postIdsExist);
            posts.addAll(postsAdded);
        }
        // Map
        return posts.stream()
            .map(webPostMapper::toPostShortResponse)
            .toList();
    }
    public List<Post> getPostsLatest(int number, Set<Long> postIdsExist) {
        if (number <= 0) {
            return Collections.emptyList();
        }
        Pageable pageable = PageRequest.of(0, number);
        List<Post> posts = postRepository.findByStatusOrderByPublishedAt(postIdsExist ,PostStatus.PUBLISHED, pageable);
        return posts.isEmpty() ? Collections.emptyList() : posts;
    }
}
