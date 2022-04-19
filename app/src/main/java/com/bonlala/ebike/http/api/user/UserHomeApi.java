package com.bonlala.ebike.http.api.user;

import com.bonlala.ebike.http.api.CommApi;

import androidx.annotation.NonNull;

/**
 * Created by Admin
 * Date 2022/4/14
 */
public class UserHomeApi implements CommApi {

    @Override
    public Class<?> getObjBean() {
        return null;
    }

    @NonNull
    @Override
    public String getApi() {
        return "app/exercise/cumulativeTotal";
    }


    public UserHomeApi getTotalData(){
        return this;
    }



    public static class HomeTotalBean{

        private Integer calorie;
        private Integer exerciseCount;
        private Integer reduceCarbon;
        private Integer totalTime;
        private Integer tripDist;

        public Integer getCalorie() {
            return calorie;
        }

        public void setCalorie(Integer calorie) {
            this.calorie = calorie;
        }

        public Integer getExerciseCount() {
            return exerciseCount;
        }

        public void setExerciseCount(Integer exerciseCount) {
            this.exerciseCount = exerciseCount;
        }

        public Integer getReduceCarbon() {
            return reduceCarbon;
        }

        public void setReduceCarbon(Integer reduceCarbon) {
            this.reduceCarbon = reduceCarbon;
        }

        public Integer getTotalTime() {
            return totalTime;
        }

        public void setTotalTime(Integer totalTime) {
            this.totalTime = totalTime;
        }

        public Integer getTripDist() {
            return tripDist;
        }

        public void setTripDist(Integer tripDist) {
            this.tripDist = tripDist;
        }

        @Override
        public String toString() {
            return "HomeTotalBean{" +
                    "calorie=" + calorie +
                    ", exerciseCount=" + exerciseCount +
                    ", reduceCarbon=" + reduceCarbon +
                    ", totalTime=" + totalTime +
                    ", tripDist=" + tripDist +
                    '}';
        }
    }
}
