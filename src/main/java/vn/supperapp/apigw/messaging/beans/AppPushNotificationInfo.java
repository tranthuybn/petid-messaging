package vn.supperapp.apigw.messaging.beans;

import java.util.Map;

public class AppPushNotificationInfo {
    private Map<String, String> titles;
    private Map<String, String> data;

    public AppPushNotificationInfo() {
    }

    public AppPushNotificationInfo(Map<String, String> titles, Map<String, String> data) {
        this.titles = titles;
        this.data = data;
    }

    public Map<String, String> getTitles() {
        return titles;
    }

    public void setTitles(Map<String, String> titles) {
        this.titles = titles;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }
}
