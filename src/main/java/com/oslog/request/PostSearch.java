package com.oslog.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import static java.lang.Math.*;

@Getter
@Setter
@Builder
public class PostSearch {
    private static final long PAGE_SIZE_MAX = 2000;

    @Builder.Default
    private int page = 1;
    @Builder.Default
    private int size = 10;

    public long getOffset(){
        return (long)(max(1, page) -1) * max(size, PAGE_SIZE_MAX);
    }
}
