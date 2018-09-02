package io.github.jboxx.catering_subscribe.pojo;

public class SubcriptionTime {

    public static final String UNDEFINED = "UNDEFINED";
    public static final String CUSTOM = "CUSTOM";

    private int pid;
    private String param;
    private String daySubcriptionDisplay;
    private String descSubcriptionDisplay;
    private int shouldSelectedDays;
    private String maxNumberOfDays;
    private double pricePerDay;

    public SubcriptionTime() {
    }

    public SubcriptionTime(SubcriptionTime subcriptionTime) {
        this.param = subcriptionTime.getParam();
        this.daySubcriptionDisplay = subcriptionTime.getDaySubcriptionDisplay();
        this.descSubcriptionDisplay = subcriptionTime.getDescSubcriptionDisplay();
        this.shouldSelectedDays = subcriptionTime.getShouldSelectedDays();
        this.maxNumberOfDays = subcriptionTime.getMaxNumberOfDays();
        this.pricePerDay = subcriptionTime.getPricePerDay();
    }

    public SubcriptionTime(String param, String daySubcriptionDisplay, String descSubcriptionDisplay, int shouldSelectedDays, String maxNumberOfDays, double pricePerDay) {
        this.param = param;
        this.daySubcriptionDisplay = daySubcriptionDisplay;
        this.descSubcriptionDisplay = descSubcriptionDisplay;
        this.shouldSelectedDays = shouldSelectedDays;
        this.maxNumberOfDays = maxNumberOfDays;
        this.pricePerDay = pricePerDay;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public void setPricePerDay(double pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public void setDescSubcriptionDisplay(String descSubcriptionDisplay) {
        this.descSubcriptionDisplay = descSubcriptionDisplay;
    }

    public int getPid() {
        return pid;
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

    public int getShouldSelectedDays() {
        return shouldSelectedDays;
    }

    public String getMaxNumberOfDays() {
        return maxNumberOfDays;
    }

    public double getPricePerDay() {
        return pricePerDay;
    }

}
