package com.charroux.carservice.entity;

public class Event {

    long id;
    String entityType;
    long entityId;
    String eventType;
    Object data;

    public Event(){}

    public Event(String entityType, long entityId, String eventType) {
        this.entityType = entityType;
        this.entityId = entityId;
        this.eventType = eventType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public long getEntityId() {
        return entityId;
    }

    public void setEntityId(long entityId) {
        this.entityId = entityId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", entityType='" + entityType + '\'' +
                ", entityId=" + entityId +
                ", eventType='" + eventType + '\'' +
                ", data=" + data +
                '}';
    }
}
