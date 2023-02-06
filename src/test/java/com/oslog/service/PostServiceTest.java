package com.oslog.service;

import com.oslog.domain.Post;
import com.oslog.repository.PostRepository;
import com.oslog.request.PostCreate;
import com.oslog.response.PostResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.springframework.data.domain.Sort.Direction.DESC;


@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("글 작성")
    void postTest() {
        // given
        PostCreate postCreate = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        // when
        postService.write(postCreate);

        // then
        Assertions.assertEquals(1L, postRepository.count());
        Post post = postRepository.findAll().get(0);
        Assertions.assertEquals("제목입니다.", post.getTitle());
        Assertions.assertEquals("내용입니다.", post.getContent());
    }

    @Test
    @DisplayName("글 1개 조회")
    void selectPostById() {
        // given
        Post requestPost = Post.builder()
                            .title("foo")
                            .content("bar")
                            .build();
        postRepository.save(requestPost);

        // when
        PostResponse postResponse = postService.get(requestPost.getId());

        // then
        assertNotNull(postResponse);
        Assertions.assertEquals("foo", postResponse.getTitle());
        Assertions.assertEquals("bar", postResponse.getContent());
    }

    @Test
    @DisplayName("글 1페이지 조회")
    void selectPostList() {
        // given
        List<Post> requestPosts = IntStream.range(1, 31)
                .mapToObj(i -> Post.builder()
                        .title("오스카 제목 - "+ i)
                        .content("블로그 내용"+ i)
                        .build())
                .collect(Collectors.toList());
        postRepository.saveAll(requestPosts);
        Pageable pageable = PageRequest.of(0, 5, DESC, "id");

        // when
        List<PostResponse> posts = postService.getList(pageable);

        // then
        assertNotNull(posts);
        Assertions.assertEquals(5L, posts.size());
        Assertions.assertEquals("오스카 제목 - 30", posts.get(0).getTitle());
        Assertions.assertEquals("오스카 제목 - 26", posts.get(4).getTitle());
    }

}