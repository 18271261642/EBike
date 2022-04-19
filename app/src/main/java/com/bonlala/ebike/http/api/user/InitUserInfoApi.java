package com.bonlala.ebike.http.api.user;

import com.bonlala.ebike.http.api.CommApi;
import com.hjq.http.annotation.HttpHeader;

/**
 * 设置用户信息的api
 * Created by Admin
 * Date 2022/4/2
 */
public class InitUserInfoApi implements CommApi {


    @Override
    public Class<?> getObjBean() {
        return null;
    }

    @Override
    public String getApi() {
        return "app/common/defaultUserSetting";
    }


    @HttpHeader
    private String language;

    @HttpHeader
    private String appToken;



    public InitUserInfoApi getDefaultUser(String token,String language){
        this.language = language;
        this.appToken = token;
        return this;
    }



    public static class UserInfoData{


        private Integer userId;
        private String nickname;
        private Integer countryId;
        private Integer gender;
        private Integer height;
        private Integer weight;
        private Integer birthday;
        private String avatar;
        private String avatarTiny;
        private String backgroundUrl;
        private String introduce;
        private Integer mileageUnit;
        private GetLoginInfoApi.UserInfoData.HeartRateRegionBean heartRateRegion;
        private GetLoginInfoApi.UserInfoData.PowerRegionBean powerRegion;
        private GetLoginInfoApi.UserInfoData.CadenceRegionBean cadenceRegion;

        public UserInfoData() {
        }


        public UserInfoData(Integer userId, String nickname) {
            this.userId = userId;
            this.nickname = nickname;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public Integer getCountryId() {
            return countryId;
        }

        public void setCountryId(Integer countryId) {
            this.countryId = countryId;
        }

        public Integer getGender() {
            return gender;
        }

        public void setGender(Integer gender) {
            this.gender = gender;
        }

        public Integer getHeight() {
            return height;
        }

        public void setHeight(Integer height) {
            this.height = height;
        }

        public Integer getWeight() {
            return weight;
        }

        public void setWeight(Integer weight) {
            this.weight = weight;
        }

        public Integer getBirthday() {
            return birthday;
        }

        public void setBirthday(Integer birthday) {
            this.birthday = birthday;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getAvatarTiny() {
            return avatarTiny;
        }

        public void setAvatarTiny(String avatarTiny) {
            this.avatarTiny = avatarTiny;
        }

        public String getBackgroundUrl() {
            return backgroundUrl;
        }

        public void setBackgroundUrl(String backgroundUrl) {
            this.backgroundUrl = backgroundUrl;
        }

        public String getIntroduce() {
            return introduce;
        }

        public void setIntroduce(String introduce) {
            this.introduce = introduce;
        }

        public Integer getMileageUnit() {
            return mileageUnit;
        }

        public void setMileageUnit(Integer mileageUnit) {
            this.mileageUnit = mileageUnit;
        }

        public GetLoginInfoApi.UserInfoData.HeartRateRegionBean getHeartRateRegion() {
            return heartRateRegion;
        }

        public void setHeartRateRegion(GetLoginInfoApi.UserInfoData.HeartRateRegionBean heartRateRegion) {
            this.heartRateRegion = heartRateRegion;
        }

        public GetLoginInfoApi.UserInfoData.PowerRegionBean getPowerRegion() {
            return powerRegion;
        }

        public void setPowerRegion(GetLoginInfoApi.UserInfoData.PowerRegionBean powerRegion) {
            this.powerRegion = powerRegion;
        }

        public GetLoginInfoApi.UserInfoData.CadenceRegionBean getCadenceRegion() {
            return cadenceRegion;
        }

        public void setCadenceRegion(GetLoginInfoApi.UserInfoData.CadenceRegionBean cadenceRegion) {
            this.cadenceRegion = cadenceRegion;
        }

        public  class HeartRateRegionBean {
            private Integer max;
            private Integer rest;
            private GetLoginInfoApi.UserInfoData.HeartRateRegionBean.WarmUpBean warmUp;
            private GetLoginInfoApi.UserInfoData.HeartRateRegionBean.FatBurnBean fatBurn;
            private GetLoginInfoApi.UserInfoData.HeartRateRegionBean.AerobicBean aerobic;
            private GetLoginInfoApi.UserInfoData.HeartRateRegionBean.PowerUpBean powerUp;
            private Integer anaerobic;

            public Integer getMax() {
                return max;
            }

            public void setMax(Integer max) {
                this.max = max;
            }

            public Integer getRest() {
                return rest;
            }

            public void setRest(Integer rest) {
                this.rest = rest;
            }

            public GetLoginInfoApi.UserInfoData.HeartRateRegionBean.WarmUpBean getWarmUp() {
                return warmUp;
            }

            public void setWarmUp(GetLoginInfoApi.UserInfoData.HeartRateRegionBean.WarmUpBean warmUp) {
                this.warmUp = warmUp;
            }

            public GetLoginInfoApi.UserInfoData.HeartRateRegionBean.FatBurnBean getFatBurn() {
                return fatBurn;
            }

            public void setFatBurn(GetLoginInfoApi.UserInfoData.HeartRateRegionBean.FatBurnBean fatBurn) {
                this.fatBurn = fatBurn;
            }

            public GetLoginInfoApi.UserInfoData.HeartRateRegionBean.AerobicBean getAerobic() {
                return aerobic;
            }

            public void setAerobic(GetLoginInfoApi.UserInfoData.HeartRateRegionBean.AerobicBean aerobic) {
                this.aerobic = aerobic;
            }

            public GetLoginInfoApi.UserInfoData.HeartRateRegionBean.PowerUpBean getPowerUp() {
                return powerUp;
            }

            public void setPowerUp(GetLoginInfoApi.UserInfoData.HeartRateRegionBean.PowerUpBean powerUp) {
                this.powerUp = powerUp;
            }

            public Integer getAnaerobic() {
                return anaerobic;
            }

            public void setAnaerobic(Integer anaerobic) {
                this.anaerobic = anaerobic;
            }

            public  class WarmUpBean {
                private Integer begin;
                private Integer end;

                public Integer getBegin() {
                    return begin;
                }

                public void setBegin(Integer begin) {
                    this.begin = begin;
                }

                public Integer getEnd() {
                    return end;
                }

                public void setEnd(Integer end) {
                    this.end = end;
                }
            }

            public  class FatBurnBean {
                private Integer begin;
                private Integer end;

                public Integer getBegin() {
                    return begin;
                }

                public void setBegin(Integer begin) {
                    this.begin = begin;
                }

                public Integer getEnd() {
                    return end;
                }

                public void setEnd(Integer end) {
                    this.end = end;
                }
            }

            public  class AerobicBean {
                private Integer begin;
                private Integer end;

                public Integer getBegin() {
                    return begin;
                }

                public void setBegin(Integer begin) {
                    this.begin = begin;
                }

                public Integer getEnd() {
                    return end;
                }

                public void setEnd(Integer end) {
                    this.end = end;
                }
            }


            public  class PowerUpBean {
                private Integer begin;
                private Integer end;

                public Integer getBegin() {
                    return begin;
                }

                public void setBegin(Integer begin) {
                    this.begin = begin;
                }

                public Integer getEnd() {
                    return end;
                }

                public void setEnd(Integer end) {
                    this.end = end;
                }
            }
        }


        public  class PowerRegionBean {
            private Integer powerValve;
            private Integer activeRecovery;
            private GetLoginInfoApi.UserInfoData.PowerRegionBean.EnduranceBean endurance;
            private GetLoginInfoApi.UserInfoData.PowerRegionBean.TempoBean tempo;
            private GetLoginInfoApi.UserInfoData.PowerRegionBean.ThresholdBean threshold;
            private GetLoginInfoApi.UserInfoData.PowerRegionBean.Vo2MaxBean vo2Max;
            private GetLoginInfoApi.UserInfoData.PowerRegionBean.AnaerobicBean anaerobic;
            private Integer neuroMuscular;


            public Integer getPowerValve() {
                return powerValve;
            }

            public void setPowerValve(Integer powerValve) {
                this.powerValve = powerValve;
            }

            public Integer getActiveRecovery() {
                return activeRecovery;
            }

            public void setActiveRecovery(Integer activeRecovery) {
                this.activeRecovery = activeRecovery;
            }

            public GetLoginInfoApi.UserInfoData.PowerRegionBean.EnduranceBean getEndurance() {
                return endurance;
            }

            public void setEndurance(GetLoginInfoApi.UserInfoData.PowerRegionBean.EnduranceBean endurance) {
                this.endurance = endurance;
            }

            public GetLoginInfoApi.UserInfoData.PowerRegionBean.TempoBean getTempo() {
                return tempo;
            }

            public void setTempo(GetLoginInfoApi.UserInfoData.PowerRegionBean.TempoBean tempo) {
                this.tempo = tempo;
            }

            public GetLoginInfoApi.UserInfoData.PowerRegionBean.ThresholdBean getThreshold() {
                return threshold;
            }

            public void setThreshold(GetLoginInfoApi.UserInfoData.PowerRegionBean.ThresholdBean threshold) {
                this.threshold = threshold;
            }

            public GetLoginInfoApi.UserInfoData.PowerRegionBean.Vo2MaxBean getVo2Max() {
                return vo2Max;
            }

            public void setVo2Max(GetLoginInfoApi.UserInfoData.PowerRegionBean.Vo2MaxBean vo2Max) {
                this.vo2Max = vo2Max;
            }

            public GetLoginInfoApi.UserInfoData.PowerRegionBean.AnaerobicBean getAnaerobic() {
                return anaerobic;
            }

            public void setAnaerobic(GetLoginInfoApi.UserInfoData.PowerRegionBean.AnaerobicBean anaerobic) {
                this.anaerobic = anaerobic;
            }

            public Integer getNeuroMuscular() {
                return neuroMuscular;
            }

            public void setNeuroMuscular(Integer neuroMuscular) {
                this.neuroMuscular = neuroMuscular;
            }

            public  class EnduranceBean {
                private Integer begin;
                private Integer end;
            }


            public  class TempoBean {
                private Integer begin;
                private Integer end;
            }


            public  class ThresholdBean {
                private Integer begin;
                private Integer end;
            }

            public  class Vo2MaxBean {
                private Integer begin;
                private Integer end;
            }

            public  class AnaerobicBean {
                private Integer begin;
                private Integer end;
            }
        }


        public  class CadenceRegionBean {
            private Integer lowCadence;
            private GetLoginInfoApi.UserInfoData.CadenceRegionBean.MiddleCadenceBean middleCadence;
            private GetLoginInfoApi.UserInfoData.CadenceRegionBean.HighCadenceBean highCadence;
            private Integer superHighCadence;


            public Integer getLowCadence() {
                return lowCadence;
            }

            public void setLowCadence(Integer lowCadence) {
                this.lowCadence = lowCadence;
            }

            public GetLoginInfoApi.UserInfoData.CadenceRegionBean.MiddleCadenceBean getMiddleCadence() {
                return middleCadence;
            }

            public void setMiddleCadence(GetLoginInfoApi.UserInfoData.CadenceRegionBean.MiddleCadenceBean middleCadence) {
                this.middleCadence = middleCadence;
            }

            public GetLoginInfoApi.UserInfoData.CadenceRegionBean.HighCadenceBean getHighCadence() {
                return highCadence;
            }

            public void setHighCadence(GetLoginInfoApi.UserInfoData.CadenceRegionBean.HighCadenceBean highCadence) {
                this.highCadence = highCadence;
            }

            public Integer getSuperHighCadence() {
                return superHighCadence;
            }

            public void setSuperHighCadence(Integer superHighCadence) {
                this.superHighCadence = superHighCadence;
            }

            public  class MiddleCadenceBean {
                private Integer begin;
                private Integer end;
            }

            public  class HighCadenceBean {
                private Integer begin;
                private Integer end;

                public Integer getBegin() {
                    return begin;
                }

                public void setBegin(Integer begin) {
                    this.begin = begin;
                }

                public Integer getEnd() {
                    return end;
                }

                public void setEnd(Integer end) {
                    this.end = end;
                }
            }
        }
    }
}
