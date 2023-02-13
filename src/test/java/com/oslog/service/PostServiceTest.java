package com.oslog.service;

import com.oslog.domain.Post;
import com.oslog.exception.PostNotFound;
import com.oslog.repository.PostRepository;
import com.oslog.request.PostCreate;
import com.oslog.request.PostEdit;
import com.oslog.request.PostSearch;
import com.oslog.response.PostResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


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
        assertEquals(1L, postRepository.count());
        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다.", post.getTitle());
        assertEquals("내용입니다.", post.getContent());
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
        assertEquals("foo", postResponse.getTitle());
        assertEquals("bar", postResponse.getContent());
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
        PostSearch postSearch = PostSearch.builder().build();

        // when
        List<PostResponse> posts = postService.getList(postSearch);

        // then
        assertNotNull(posts);
        assertEquals(10L, posts.size());
        assertEquals("오스카 제목 - 30", posts.get(0).getTitle());
        assertEquals("오스카 제목 - 26", posts.get(4).getTitle());
    }

    @Test
    @DisplayName("글 제목 수정")
    void editPostTitle() {
        // given
        Post post = Post.builder()
                .title("오스카")
                .content("아크자이")
                .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("양라솔")
                .content("아크자이")
                .build();

        // when
        postService.edit(post.getId(), postEdit);

        // then
        Post changePost = postRepository.findById(post.getId())
                .orElseThrow(PostNotFound::new);

        assertEquals("양라솔", changePost.getTitle());
    }

    @Test
    @DisplayName("글 내용 수정")
    void editPostContent() {
        // given
        Post post = Post.builder()
                .title("오스카")
                .content("아크자이")
                .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("오스카")
                .content("반포자이")
                .build();

        // when
        postService.edit(post.getId(), postEdit);

        // then
        Post changePost = postRepository.findById(post.getId())
                .orElseThrow(PostNotFound::new);

        assertEquals("반포자이", changePost.getContent());
    }

    @Test
    @DisplayName("글 내용 삭제")
    void deletePostContent() {
        // given
        Post post = Post.builder()
                .title("오스카")
                .content("아크자이")
                .build();

        postRepository.save(post);

        // when
        postService.delete(post.getId());

        // then
        assertEquals(0, postRepository.count());
    }

    @Test
    @DisplayName("글 1페이지 조회 에러 케이스")
    void selectPostListWithFailCase() {
        // given
        Post requestPost = Post.builder()
                .title("오스카")
                .content("아크자이")
                .build();
        postRepository.save(requestPost);

        // when
        assertThrows(PostNotFound.class, () -> {
            postService.get(requestPost.getId() + 1L);
        });
    }

}