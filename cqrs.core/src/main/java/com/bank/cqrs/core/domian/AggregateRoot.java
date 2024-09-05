package com.bank.cqrs.core.domian;

import com.bank.cqrs.core.events.BaseEvent;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Getter
@Setter
public abstract class AggregateRoot {

    protected String id;
    private int version = -1;


    private final List<BaseEvent> changes = new ArrayList<>();


    public List<BaseEvent> getUncommittedChanges() {
        return this.changes;
    }

    public void markChangesAsCommitted() {
        this.changes.clear();
    }

    protected void applyChange(BaseEvent event, Boolean isNewEvent) {
        try {
            var method = getClass().getDeclaredMethod("apply", event.getClass());
            method.setAccessible(true);
            method.invoke(this, event);

        } catch (NoSuchMethodException e) {
            log.warn("No apply method found for event: " + event.getClass().getName());
        } catch (Exception e) {
            log.error("Error applying event: " + event.getClass().getName());
        } finally {
            if (isNewEvent) {
                this.changes.add(event);
            }
        }
    }

    public void raiseEvent(BaseEvent event) {
        applyChange(event, true);
    }

    public void loadFromHistory(Iterable<BaseEvent> events) {
        for (var event : events) {
            applyChange(event, false);
        }
    }
}
