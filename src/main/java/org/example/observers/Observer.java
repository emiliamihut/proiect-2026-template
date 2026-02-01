package org.example.observers;

import org.example.models.Event;

public interface Observer {
    void update(Event event);
}

