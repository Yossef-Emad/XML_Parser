package com.example.ds_xml;

import java.util.ArrayList;
import java.util.List;

public class Post {
    String body;
    List<String> topics;

    public Post(String body) {
        this.body = body;
        this.topics = new ArrayList<>();
    }
}
