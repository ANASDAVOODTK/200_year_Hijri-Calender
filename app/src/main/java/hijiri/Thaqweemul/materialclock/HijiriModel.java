package hijiri.Thaqweemul.materialclock;

public class HijiriModel {

    String hijridate;
    String ggdate;

    public HijiriModel()
    {

    }

    public HijiriModel(String hijridate, String ggdate, String month, String year, String time, String fq, String fm, String lq) {
        this.hijridate = hijridate;
        this.ggdate = ggdate;
        this.month = month;
        this.year = year;
        this.time = time;
        this.fq = fq;
        this.fm = fm;
        this.lq = lq;
    }

    String month;
    String year;
    String time;
    String fq;
    String fm;
    String lq;


    public String getHijridate() {
        return hijridate;
    }

    public void setHijridate(String hijridate) {
        this.hijridate = hijridate;
    }

    public String getGgdate() {
        return ggdate;
    }

    public void setGgdate(String ggdate) {
        this.ggdate = ggdate;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFq() {
        return fq;
    }

    public void setFq(String fq) {
        this.fq = fq;
    }

    public String getFm() {
        return fm;
    }

    public void setFm(String fm) {
        this.fm = fm;
    }

    public String getLq() {
        return lq;
    }

    public void setLq(String lq) {
        this.lq = lq;
    }
}
