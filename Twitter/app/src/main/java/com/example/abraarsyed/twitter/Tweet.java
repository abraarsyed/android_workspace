package com.example.abraarsyed.twitter;

/**
 * Created by abraarsyed on 19/7/15.
 */

import java.io.Serializable;

public class Tweet implements Serializable {
    private String id;
    private String title;
    private String body;
    private static final long serialVersionUID = 1L;

    public void setTitle(String title){
        this.title = title;
    }

    public String getTitle(){
        return this.title;
    }

    public void setBody(String body){
        this.body = body;
    }

    public String getBody(){
        return this.body;
    }

    public String getId(){
        return this.id;
    }


}
