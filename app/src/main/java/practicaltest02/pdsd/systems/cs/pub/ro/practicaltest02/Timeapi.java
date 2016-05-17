package practicaltest02.pdsd.systems.cs.pub.ro.practicaltest02;

/**
 * Created by Songbird on 5/17/2016.
 */
public class Timeapi {
    private String currentTime;
    private String currentMinute;
    public Timeapi() {
        this.currentTime = null;
        this.currentMinute = null;
    }

    public Timeapi(String currentTime) {
        this.currentTime = currentTime;
    }

    public Timeapi(String currentTime, String currentMinute) {
        this.currentTime = currentTime;
        this.currentMinute = currentMinute;
    }

    public void setCurrentTime(String temperature) {
        this.currentTime = currentTime;
    }

    public String getCurrentTime() {
        return this.currentTime;
    }

    public void setCurrentMinute(String currentMinute) {
        this.currentMinute = currentMinute;
    }

    public String getCurrentMinute() {
        return this.currentMinute;
    }


    @Override
    public String toString() {
        return Constants.CRT_TIME +  this.currentTime + "\n\r";
    }

}
