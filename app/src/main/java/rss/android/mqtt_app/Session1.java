package rss.android.mqtt_app;

/**
 * Created by AliFakih on 11-Dec-19.
 */

public class Session1 {

    String ClassName;
    String CourseName;
    String Day;
    String StartTime;
    String EndTime;

    public Session1( String className, String courseName, String day, String startTime, String endTime) {

        ClassName = className;
        CourseName = courseName;
        Day = day;
        StartTime = startTime;
        EndTime = endTime;
    }

    public String getClassName() {
        return ClassName;
    }

    public void setClassName(String className) {
        ClassName = className;
    }

    public String getCourseName() {
        return CourseName;
    }

    public void setCourseName(String courseName) {
        CourseName = courseName;
    }

    public String getDay() {
        return Day;
    }

    public void setDay(String day) {
        Day = day;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }
}
