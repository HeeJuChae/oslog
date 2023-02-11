package com.oslog.repository;

import com.oslog.domain.Post;
import com.oslog.request.PostSearch;

import java.util.List;

public interface PostRespositoryCustom {

    List<Post> getList(PostSearch postSearch);
}
