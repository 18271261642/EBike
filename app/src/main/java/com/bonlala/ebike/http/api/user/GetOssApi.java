package com.bonlala.ebike.http.api.user;

import com.bonlala.ebike.http.api.CommApi;
import com.hjq.http.annotation.HttpHeader;

/**
 * Created by Admin
 * Date 2022/4/6
 */
public class GetOssApi implements CommApi {


    @Override
    public Class<?> getObjBean() {
        return null;
    }

    @Override
    public String getApi() {
        return "app/common/oss/token";
    }


    @HttpHeader
    private String language;

    @HttpHeader
    private String appToken;

    public GetOssApi getOssToke(String language,String token){
        this.language = language;
        this.appToken = token;
        return this;
    }




    public static class OssTokenBean{

        private String bucket;
        private String accessId;
        private String accessKey;
        private String securityToken;
        private String expiration;
        private String endpoint;


        public String getBucket() {
            return bucket;
        }

        public void setBucket(String bucket) {
            this.bucket = bucket;
        }

        public String getAccessId() {
            return accessId;
        }

        public void setAccessId(String accessId) {
            this.accessId = accessId;
        }

        public String getAccessKey() {
            return accessKey;
        }

        public void setAccessKey(String accessKey) {
            this.accessKey = accessKey;
        }

        public String getSecurityToken() {
            return securityToken;
        }

        public void setSecurityToken(String securityToken) {
            this.securityToken = securityToken;
        }

        public String getExpiration() {
            return expiration;
        }

        public void setExpiration(String expiration) {
            this.expiration = expiration;
        }

        public String getEndpoint() {
            return endpoint;
        }

        public void setEndpoint(String endpoint) {
            this.endpoint = endpoint;
        }

        @Override
        public String toString() {
            return "OssTokenBean{" +
                    "bucket='" + bucket + '\'' +
                    ", accessId='" + accessId + '\'' +
                    ", accessKey='" + accessKey + '\'' +
                    ", securityToken='" + securityToken + '\'' +
                    ", expiration='" + expiration + '\'' +
                    ", endpoint='" + endpoint + '\'' +
                    '}';
        }
    }


}
