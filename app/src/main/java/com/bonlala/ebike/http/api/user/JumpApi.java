package com.bonlala.ebike.http.api.user;

import com.bonlala.ebike.http.api.CommApi;

import androidx.annotation.NonNull;

/**
 * Created by Admin
 * Date 2022/4/14
 */
public class JumpApi implements CommApi {


    @Override
    public Class<?> getObjBean() {
        return null;
    }

    @NonNull
    @Override
    public String getApi() {
        return "app/user/initUserSetting";
    }




    public JumpApi setDefaultApi(){
        return this;
    }

    public static class DefaultUserInfo{


        private Long birthday;
        private CadenceRegionBean cadenceRegion;
        private Integer countryId;
        private Integer gender;
        private HeartRateRegionBean heartRateRegion;
        private Integer height;
        private Integer mileageUnit;
        private String nickname;
        private PowerRegionBean powerRegion;
        private Boolean skip;
        private Integer weight;


        public Long getBirthday() {
            return birthday;
        }

        public void setBirthday(Long birthday) {
            this.birthday = birthday;
        }

        public CadenceRegionBean getCadenceRegion() {
            return cadenceRegion;
        }

        public void setCadenceRegion(CadenceRegionBean cadenceRegion) {
            this.cadenceRegion = cadenceRegion;
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

        public HeartRateRegionBean getHeartRateRegion() {
            return heartRateRegion;
        }

        public void setHeartRateRegion(HeartRateRegionBean heartRateRegion) {
            this.heartRateRegion = heartRateRegion;
        }

        public Integer getHeight() {
            return height;
        }

        public void setHeight(Integer height) {
            this.height = height;
        }

        public Integer getMileageUnit() {
            return mileageUnit;
        }

        public void setMileageUnit(Integer mileageUnit) {
            this.mileageUnit = mileageUnit;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public PowerRegionBean getPowerRegion() {
            return powerRegion;
        }

        public void setPowerRegion(PowerRegionBean powerRegion) {
            this.powerRegion = powerRegion;
        }

        public Boolean getSkip() {
            return skip;
        }

        public void setSkip(Boolean skip) {
            this.skip = skip;
        }

        public Integer getWeight() {
            return weight;
        }

        public void setWeight(Integer weight) {
            this.weight = weight;
        }

        public static class CadenceRegionBean {
            private HighCadenceBean highCadence;
            private Integer lowCadence;
            private MiddleCadenceBean middleCadence;
            private Integer superHighCadence;

            public HighCadenceBean getHighCadence() {
                return highCadence;
            }

            public void setHighCadence(HighCadenceBean highCadence) {
                this.highCadence = highCadence;
            }

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

            public Integer getSuperHighCadence() {
                return superHighCadence;
            }

            public void setSuperHighCadence(Integer superHighCadence) {
                this.superHighCadence = superHighCadence;
            }

            public static class HighCadenceBean {
                private Integer begin;
                private Integer end;
            }


            public static class MiddleCadenceBean {
                private Integer begin;
                private Integer end;
            }
        }


        public static class HeartRateRegionBean {
            private AerobicBean aerobic;
            private Integer anaerobic;
            private FatBurnBean fatBurn;
            private Integer max;
            private PowerUpBean powerUp;
            private Integer rest;
            private WarmUpBean warmUp;

            public AerobicBean getAerobic() {
                return aerobic;
            }

            public void setAerobic(AerobicBean aerobic) {
                this.aerobic = aerobic;
            }

            public Integer getAnaerobic() {
                return anaerobic;
            }

            public void setAnaerobic(Integer anaerobic) {
                this.anaerobic = anaerobic;
            }

            public FatBurnBean getFatBurn() {
                return fatBurn;
            }

            public void setFatBurn(FatBurnBean fatBurn) {
                this.fatBurn = fatBurn;
            }

            public Integer getMax() {
                return max;
            }

            public void setMax(Integer max) {
                this.max = max;
            }

            public PowerUpBean getPowerUp() {
                return powerUp;
            }

            public void setPowerUp(PowerUpBean powerUp) {
                this.powerUp = powerUp;
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

            public static class AerobicBean {
                private Integer begin;
                private Integer end;
            }


            public static class FatBurnBean {
                private Integer begin;
                private Integer end;
            }


            public static class PowerUpBean {
                private Integer begin;
                private Integer end;
            }


            public static class WarmUpBean {
                private Integer begin;
                private Integer end;
            }
        }


        public static class PowerRegionBean {
            private Integer activeRecovery;
            private AnaerobicBean anaerobic;
            private EnduranceBean endurance;
            private Integer neuroMuscular;
            private Integer powerValve;
            private TempoBean tempo;
            private ThresholdBean threshold;
            private Vo2MaxBean vo2Max;

            public Integer getActiveRecovery() {
                return activeRecovery;
            }

            public void setActiveRecovery(Integer activeRecovery) {
                this.activeRecovery = activeRecovery;
            }

            public AnaerobicBean getAnaerobic() {
                return anaerobic;
            }

            public void setAnaerobic(AnaerobicBean anaerobic) {
                this.anaerobic = anaerobic;
            }

            public EnduranceBean getEndurance() {
                return endurance;
            }

            public void setEndurance(EnduranceBean endurance) {
                this.endurance = endurance;
            }

            public Integer getNeuroMuscular() {
                return neuroMuscular;
            }

            public void setNeuroMuscular(Integer neuroMuscular) {
                this.neuroMuscular = neuroMuscular;
            }

            public Integer getPowerValve() {
                return powerValve;
            }

            public void setPowerValve(Integer powerValve) {
                this.powerValve = powerValve;
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

            public static class AnaerobicBean {
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


            public static class EnduranceBean {
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


            public static class TempoBean {
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


            public static class ThresholdBean {
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


            public static class Vo2MaxBean {
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
