package io.github.jboxx.catering_subscribe.pojo;

public class SubcriptionTime {

    public static final String UNDEFINED = "UNDEFINED";

    private String param;
    private String daySubcriptionDisplay;
    private String descSubcriptionDisplay;
    private int numberOfDays;
    private String maxNumberOfDays;
    private double pricePerDay;
    private boolean addMoreDays = false;

    public SubcriptionTime(String param, String daySubcriptionDisplay, String descSubcriptionDisplay, int numberOfDays, String maxNumberOfDays, double pricePerDay, boolean addMoreDays) {
        this.param = param;
        this.daySubcriptionDisplay = daySubcriptionDisplay;
        this.descSubcriptionDisplay = descSubcriptionDisplay;
        this.numberOfDays = numberOfDays;
        this.maxNumberOfDays = maxNumberOfDays;
        this.pricePerDay = pricePerDay;
        this.addMoreDays = addMoreDays;
    }

    public String getParam() {
        return param;
    }

    public String getDaySubcriptionDisplay() {
        return daySubcriptionDisplay;
    }

    public String getDescSubcriptionDisplay() {
        return descSubcriptionDisplay;
    }

    public int getNumberOfDays() {
        return numberOfDays;
    }

    public String getMaxNumberOfDays() {
        return maxNumberOfDays;
    }

    public double getPricePerDay() {
        return pricePerDay;
    }

    public boolean isAddMoreDays() {
        return addMoreDays;
    }
}
