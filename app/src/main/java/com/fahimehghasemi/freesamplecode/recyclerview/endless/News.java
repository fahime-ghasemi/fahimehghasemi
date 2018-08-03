package com.fahimehghasemi.freesamplecode.recyclerview.endless;

/**
 * Created by fahime on 8/3/18.
 */

public class News {
    String title;
    String body;

    public News(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
