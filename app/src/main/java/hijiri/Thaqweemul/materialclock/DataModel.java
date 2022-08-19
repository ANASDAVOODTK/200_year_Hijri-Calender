package hijiri.Thaqweemul.materialclock;

public class DataModel {

    String year, month, week, day, mn;

    public DataModel() {

    }

    public DataModel(String year, String month, String week, String day, String mn) {
        this.year = year;
        this.month = month;
        this.week = week;
        this.day = day;
        this.mn = mn;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMn() {
        return mn;
    }

    public void setMn(String mn) {
        this.mn = mn;
    }
}