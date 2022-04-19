package com.bonlala.ebike.http.api;

import com.hjq.http.config.IRequestApi;

/**
 * Created by Admin
 * Date 2022/3/29
 */
public class PrivacyBeanApi implements CommApi {


    @Override
    public String getApi() {
        return "app/common/agreement";
    }

    private DataBean dataBean;

    public PrivacyBeanApi getThis() {
        return this;
    }

    @Override
    public Class<DataBean> getObjBean() {
        return DataBean.class;
    }

    public DataBean getDataBean() {
        return dataBean;
    }

    public static class DataBean{
        private  String userAgreement;
        private  String privacyAgreement;

        public  String getUserAgreement() {
            return userAgreement;
        }

        public  void setUserAgreement(String userAgreement) {
            userAgreement = userAgreement;
        }

        public  String getPrivacyAgreement() {
            return privacyAgreement;
        }

        public  void setPrivacyAgreement(String privacyAgreement) {
            privacyAgreement = privacyAgreement;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "userAgreement='" + userAgreement + '\'' +
                    ", privacyAgreement='" + privacyAgreement + '\'' +
                    '}';
        }
    }


}
