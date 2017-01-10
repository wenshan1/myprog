package me.wenshan.blog.backend.form;

import me.wenshan.blog.domain.Post;

public class PostVO {
    private Post  post;
    private String strTags;

   
    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public String getStrTags() {
        return strTags;
    }

    public void setStrTags(String strTags) {
        this.strTags = strTags;
    }

}
