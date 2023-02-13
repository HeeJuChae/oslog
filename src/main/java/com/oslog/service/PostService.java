package com.oslog.service;

import com.oslog.domain.Post;
import com.oslog.domain.PostEditor;
import com.oslog.exception.PostNotFound;
import com.oslog.repository.PostRepository;
import com.oslog.request.PostCreate;
import com.oslog.request.PostEdit;
import com.oslog.request.PostSearch;
import com.oslog.response.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public void write(PostCreate postCreate) {
        postRepository.save(Post.builder()
                .title(postCreate.getTitle())
                .content(postCreate.getContent())
                .build()
        );
    }

    public PostResponse get(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);

        return PostResponse.builder()
                    .id(post.getId())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .build();
    }

    public List<PostResponse> getList(PostSearch postSearch) {
        return postRepository.getList(postSearch).stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void edit(Long id, PostEdit postEdit) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);

        PostEditor.PostEditorBuilder editorBuilder = post.toEditor();

        PostEditor postEditor =  editorBuilder
                .title(postEdit.getTitle())
                .content(postEdit.getContent())
                .build();

        post.edit(postEditor);

        postRepository.save(post);
    }

    public void delete(Long id) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFound::new);
        postRepository.delete(post);
    }
}
