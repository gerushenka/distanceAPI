package com.example.distance.model.distancecitydto;

public class DistanceCityDTO {
    private Long id;
    private Long distanceId;
    private Long cityId;

    public Long getId() {
        return id;
    }

    public Long getDistanceId() {
        return distanceId;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDistanceId(Long distanceId) {
        this.distanceId = distanceId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }
}