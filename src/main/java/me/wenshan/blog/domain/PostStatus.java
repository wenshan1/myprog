package me.wenshan.blog.domain;

public enum PostStatus {
	publish, secret, trash;
    
    public static int  get (PostStatus p) {
        if ( p == PostStatus.publish)
            return 0;
        else if ( p == PostStatus.secret)
            return 1;
        else
            return 2;
    }
}
