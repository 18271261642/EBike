package com.bonlala.ebike.http.api.user;

import com.bonlala.ebike.http.api.CommApi;
import com.bonlala.ebike.http.net.ServerConstance;
import com.hjq.http.annotation.HttpHeader;
import com.hjq.http.config.IRequestType;
import com.hjq.http.model.BodyType;

/**
 * Created by Admin
 * Date 2022/3/30
 */
public class GetLoginInfoApi implements CommApi, IRequestType
{

    private String apiUrl;

    private BodyType bodyType ;

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public BodyType getBodyType() {
        return bodyType == null ? BodyType.FORM : BodyType.JSON;
    }

    public void setBodyType(BodyType bodyType) {
        this.bodyType = bodyType;
    }

    @Override
    public Class<?> getObjBean() {
        return UserInfoData.class;
    }

    @Override
    public String getApi() {
        return getApiUrl();
    }



    @HttpHeader
    private String language;

    @HttpHeader
    private String appToken;



    //获取用户信息，get请求
    public GetLoginInfoApi setParams(String lan,String tokeStr){
        setApiUrl(ServerConstance.GET_USER_INFO);
        this.language = lan;
        this.appToken = tokeStr;
        return this;
    }



    //更新用户信息，put请求
    public GetLoginInfoApi putUserInfo(String lan,String tokeStr){
        this.language = lan;
        this.appToken = tokeStr;
        setApiUrl(ServerConstance.UPDATE_USER_INFO);
        setBodyType(BodyType.JSON);
        return this;
    }


    //更新用户信息，put请求
    public GetLoginInfoApi putUserInfo(){
        setApiUrl(ServerConstance.UPDATE_USER_INFO);
        setBodyType(BodyType.JSON);
        return this;
    }

    public static class UserInfoData{


        private Integer userId;
        private String nickname;
        private Integer countryId;
        private Integer gender;
        private int height;
        private int weight;
        private long birthday;
        private String avatar;
        private String avatarTiny;
        private String backgroundUrl;
        private String introduce;
        private Integer mileageUnit;
        private HeartRateRegionBean heartRateRegion;
        private PowerRegionBean powerRegion;
        private CadenceRegionBean cadenceRegion;

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

        public Long getBirthday() {
            return birthday;
        }

        public void setBirthday(long birthday) {
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

        public HeartRateRegionBean getHeartRateRegion() {
            return heartRateRegion;
        }

        public void setHeartRateRegion(HeartRateRegionBean heartRateRegion) {
            this.heartRateRegion = heartRateRegion;
        }

        public PowerRegionBean getPowerRegion() {
            return powerRegion;
        }

        public void setPowerRegion(PowerRegionBean powerRegion) {
            this.powerRegion = powerRegion;
        }

        public CadenceRegionBean getCadenceRegion() {
            return cadenceRegion;
        }

        public void setCadenceRegion(CadenceRegionBean cadenceRegion) {
            this.cadenceRegion = cadenceRegion;
        }

        public  class HeartRateRegionBean {
            private Integer max;
            private Integer rest;
            private WarmUpBean warmUp;
            private FatBurnBean fatBurn;
            private AerobicBean aerobic;
            private PowerUpBean powerUp;
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

            public WarmUpBean getWarmUp() {
                return warmUp;
            }

            public void setWarmUp(WarmUpBean warmUp) {
                this.warmUp = warmUp;
            }

            public FatBurnBean getFatBurn() {
                return fatBurn;
            }

            public void setFatBurn(FatBurnBean fatBurn) {
                this.fatBurn = fatBurn;
            }

            public AerobicBean getAerobic() {
                return aerobic;
            }

            public void setAerobic(AerobicBean aerobic) {
                this.aerobic = aerobic;
            }

            public PowerUpBean getPowerUp() {
                return powerUp;
            }

            public void setPowerUp(PowerUpBean powerUp) {
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
            private EnduranceBean endurance;
            private TempoBean tempo;
            private ThresholdBean threshold;
            private Vo2MaxBean vo2Max;
            private AnaerobicBean anaerobic;
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

            public EnduranceBean getEndurance() {
                return endurance;
            }

            public void setEndurance(EnduranceBean endurance) {
                this.endurance = endurance;
            }

            public TempoBean getTempo() {
                return tempo;
            }

            public void setTempo(TempoBean tempo) {
                this.tempo = tempo;
            }

            public ThresholdBean getThreshold() {
                return threshold;
            }

            public void setThreshold(ThresholdBean threshold) {
                this.threshold = threshold;
            }

            public Vo2MaxBean getVo2Max() {
                return vo2Max;
            }

            public void setVo2Max(Vo2MaxBean vo2Max) {
                this.vo2Max = vo2Max;
            }

            public AnaerobicBean getAnaerobic() {
                return anaerobic;
            }

            public void setAnaerobic(AnaerobicBean anaerobic) {
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
            private MiddleCadenceBean middleCadence;
            private HighCadenceBean highCadence;
            private Integer superHighCadence;


            public Integer getLowCadence() {
                return lowCadence;
            }

            public void setLowCadence(Integer lowCadence) {
                this.lowCadence = lowCadence;
            }

            public MiddleCadenceBean getMiddleCadence() {
                return middleCadence;
            }

            public void setMiddleCadence(MiddleCadenceBean middleCadence) {
                this.middleCadence = middleCadence;
            }

            public HighCadenceBean getHighCadence() {
                return highCadence;
            }

            public void setHighCadence(HighCadenceBean highCadence) {
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
