package com.example.ds_xml;

import java.util.ArrayList;
import java.util.List;

public class User {
    int id;
    String name;
    List<Post> posts;
    List<User> followers;
    List<User> followed;
      public User(){}
    public User(int id, String name) {
        this.id = id;
        this.name = name;
        this.posts = new ArrayList<>();
        this.followers = new ArrayList<>();
    }

}
