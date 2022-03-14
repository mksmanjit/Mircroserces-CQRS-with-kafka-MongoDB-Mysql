package com.mksmanjit.cqrs.core.domain;

import com.mksmanjit.cqrs.core.events.BaseEvent;
import lombok.extern.apachecommons.CommonsLog;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@CommonsLog
public abstract class AggregateRoot {
    protected String id;
    private int version = -1;

    private final List<BaseEvent> changes = new ArrayList<>();

    public String getId() {
        return this.id;
    }

    public int getVersion() {
        return this.version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public List<BaseEvent> getUncommittedChanges() {
        return changes;
    }

    public void markChangesAsCommitted() {
        this.changes.clear();
    }

    protected void applyChanges(BaseEvent baseEvent, Boolean isNewEvent) {
        try {
            var method = getClass().getDeclaredMethod("apply", baseEvent.getClass());
            method.setAccessible(true);
            method.invoke(this,baseEvent);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.error("Apply method not found", e);
        } finally {
            if(isNewEvent) {
                changes.add(baseEvent);
            }
        }
    }

    public void raiseEvent(BaseEvent event) {
        applyChanges(event,true);
    }

    public void replayEvents(Iterable<BaseEvent> events) {
        events.forEach(event -> applyChanges(event, false));
    }

}
