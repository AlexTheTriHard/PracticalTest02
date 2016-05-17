package practicaltest02.pdsd.systems.cs.pub.ro.practicaltest02;

/**
 * Created by Songbird on 5/17/2016.
 */
public class Timeapi {
    private String currentTime;

    public Timeapi() {
        this.currentTime = null;
    }

    public Timeapi(String currentTime) {
        this.currentTime = currentTime;
    }

    public void setCurrentTime(String temperature) {
        this.currentTime = currentTime;
    }

    public String getCurrentTime() {
        return this.currentTime;
    }

    @Override
    public String toString() {
        return Constants.CRT_TIME +  this.currentTime + "\n\r";
    }

}
