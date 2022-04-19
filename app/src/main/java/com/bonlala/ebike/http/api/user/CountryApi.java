package com.bonlala.ebike.http.api.user;

import com.bonlala.ebike.http.api.CommApi;

/**
 * Created by Admin
 * Date 2022/3/30
 */
public class CountryApi implements CommApi {
    @Override
    public Class<?> getObjBean() {
        return CountryItemBean.class;
    }

    @Override
    public String getApi() {
        return "app/common/countryList";
    }


    public class CountryItemBean{

        private Integer id;
        private String labelName;
        private String value;
        private String dictType;
        private Integer sort;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getLabelName() {
            return labelName;
        }

        public void setLabelName(String labelName) {
            this.labelName = labelName;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getDictType() {
            return dictType;
        }

        public void setDictType(String dictType) {
            this.dictType = dictType;
        }

        public Integer getSort() {
            return sort;
        }

        public void setSort(Integer sort) {
            this.sort = sort;
        }
    }


}
