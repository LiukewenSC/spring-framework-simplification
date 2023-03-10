package com.kewen.spring.http;
/**
 * @descrpition  媒体类型 实际上继承的MimeType，先这样把
 * @author kewen
 * @since 2023-03-09
 */
public class MediaType {

    String type;

    public MediaType(String type) {
        this.type = type;
    }

    public static MediaType parseMediaType(String value) {
        return new MediaType(value);
    }

    public boolean isConcrete() {
        //继承 MimeType ，逻辑很复杂
        return false;
    }
}
