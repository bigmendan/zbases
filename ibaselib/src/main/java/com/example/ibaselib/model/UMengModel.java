package com.example.ibaselib.model;

import android.os.Build;

/**
 * time   :  2018/10/15
 * author :  Z
 * des    :
 */
public class UMengModel {

    private String uid;
    private String openid;
    private String unionid;
    private String access_token;
    private String refresh_token;
    private String expires_in;
    private String name;
    private String gender;
    private String iconurl;

    private UMengModel(Builder builder) {
        uid = builder.uid;
        openid = builder.openid;
        unionid = builder.unionid;
        access_token = builder.access_token;
        refresh_token = builder.refresh_token;
        expires_in = builder.expires_in;
        name = builder.name;
        gender = builder.gender;
        iconurl = builder.iconurl;
    }


    public static final class Builder {
        private String uid;
        private String openid;
        private String unionid;
        private String access_token;
        private String refresh_token;
        private String expires_in;
        private String name;
        private String gender;
        private String iconurl;

        public Builder() {
        }

        public Builder uid(String val) {
            uid = val;
            return this;
        }

        public Builder openid(String val) {
            openid = val;
            return this;
        }

        public Builder unionid(String val) {
            unionid = val;
            return this;
        }

        public Builder access_token(String val) {
            access_token = val;
            return this;
        }

        public Builder refresh_token(String val) {
            refresh_token = val;
            return this;
        }

        public Builder expires_in(String val) {
            expires_in = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder gender(String val) {
            gender = val;
            return this;
        }

        public Builder iconurl(String val) {
            iconurl = val;
            return this;
        }

        public UMengModel build() {
            return new UMengModel(this);
        }
    }


    public String getUid() {
        return uid;
    }

    public String getOpenid() {
        return openid;
    }

    public String getUnionid() {
        return unionid;
    }

    public String getAccess_token() {
        return access_token;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public String getExpires_in() {
        return expires_in;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getIconurl() {
        return iconurl;
    }

    @Override
    public String toString() {
        return "UMengModel{" +
                "uid='" + uid + '\'' +
                ", openid='" + openid + '\'' +
                ", unionid='" + unionid + '\'' +
                ", access_token='" + access_token + '\'' +
                ", refresh_token='" + refresh_token + '\'' +
                ", expires_in='" + expires_in + '\'' +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", iconurl='" + iconurl + '\'' +
                '}';
    }
}
