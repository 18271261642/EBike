package com.bonlala.ebike.http.api.weather;

import com.bonlala.ebike.http.api.CommApi;

import java.util.List;

import androidx.annotation.NonNull;

/**
 * Created by Admin
 * Date 2022/4/11
 */
public class WeatherApi implements CommApi {
    @Override
    public Class<?> getObjBean() {
        return null;
    }

    @NonNull
    @Override
    public String getApi() {
        return "app/weather/immediate";
    }


    //参数 城市名称-地址 eg:深圳市宝安区
    private String cityDetailName;
    //参数 纬度 lat
    private double lat;
    //经度
    private double lng;

    public WeatherApi(String cityDetailName, double lat, double lng) {
        this.cityDetailName = cityDetailName;
        this.lat = lat;
        this.lng = lng;
    }

    public WeatherApi getWeatherApi(){
        return this;
    }



    public static class WeatherBean{

        private Integer temp;
        private List<TipsListBean> tipsList;
        private String weatherImgUrl;
        private String weatherTypeName;


        public Integer getTemp() {
            return temp;
        }

        public void setTemp(Integer temp) {
            this.temp = temp;
        }

        public List<TipsListBean> getTipsList() {
            return tipsList;
        }

        public void setTipsList(List<TipsListBean> tipsList) {
            this.tipsList = tipsList;
        }

        public String getWeatherImgUrl() {
            return weatherImgUrl;
        }

        public void setWeatherImgUrl(String weatherImgUrl) {
            this.weatherImgUrl = weatherImgUrl;
        }

        public String getWeatherTypeName() {
            return weatherTypeName;
        }

        public void setWeatherTypeName(String weatherTypeName) {
            this.weatherTypeName = weatherTypeName;
        }

        public static class TipsListBean {
            private String tipsImgUrl;
            private String tipsMsg;

            public String getTipsImgUrl() {
                return tipsImgUrl;
            }

            public void setTipsImgUrl(String tipsImgUrl) {
                this.tipsImgUrl = tipsImgUrl;
            }

            public String getTipsMsg() {
                return tipsMsg;
            }

            public void setTipsMsg(String tipsMsg) {
                this.tipsMsg = tipsMsg;
            }
        }
    }
}
