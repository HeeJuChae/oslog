package com.oslog.request;

import com.oslog.exception.InvalidRequest;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@ToString
public class PostCreate {

    @NotBlank(message = "타이틀을 입력해주세요.")
    private String title;
    @NotBlank(message = "콘텐츠을 입력해주세요.")
    private String content;

    @Builder
    public PostCreate(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void validate() {
        if (title.contains("바보")) {
            throw new InvalidRequest("title", "제목에 바보를 포함 할 수 없습니다.");
        }
    }
}
