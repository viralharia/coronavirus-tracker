package com.vharia.coronavirustracker.models;

/**
 * LocationStats
 */
public class LocationStats {

    private String state;
    private String country;
    private int latestTotalCases;
    private int diffFromPrevDay;
    private float last3DaysAvgChange;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getLatestTotalCases() {
        return latestTotalCases;
    }

    public void setLatestTotalCases(int latestTotalCases) {
        this.latestTotalCases = latestTotalCases;
    }

    @Override
    public String toString() {
        return "LocationStats [country=" + country + ", latestTotalCases=" + latestTotalCases + ", state=" + state
                + "]";
    }

    public int getDiffFromPrevDay() {
        return diffFromPrevDay;
    }

    public void setDiffFromPrevDay(int diffFromPrevDay) {
        this.diffFromPrevDay = diffFromPrevDay;
    }

    public float getLast3DaysAvgChange() {
        return last3DaysAvgChange;
    }

    public void setLast3DaysAvgChange(float last3DaysAvg) {
        this.last3DaysAvgChange = last3DaysAvg;
    }
    
    
}