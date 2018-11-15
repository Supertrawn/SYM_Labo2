package heig_vd.sym_labo2.model;

import java.io.Serializable;

public class Post {

    private String title;
    private String post;

    public Post(String title, String post) {
        this.title = title;
        this.post = post;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    @Override
    public String toString() {
        return String.format("%s\n%s", this.title, this.post);
    }
}