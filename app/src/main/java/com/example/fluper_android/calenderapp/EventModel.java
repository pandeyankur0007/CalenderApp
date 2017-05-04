package com.example.fluper_android.calenderapp;

/**
 * Created by fluper-android on 4/5/17.
 */

public class EventModel {
    public String event;
    public String eventDay;

    public EventModel() {

    }

    public EventModel(String event, String eventDay) {
        this.event = event;
        this.eventDay = eventDay;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getEventDay() {
        return eventDay;
    }

    public void setEventDay(String eventDay) {
        this.eventDay = eventDay;
    }
}
