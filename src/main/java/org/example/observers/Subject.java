package org.example.observers;

import org.example.models.Event;

public interface Subject {
    void addObserver(Observer o);
    void notifyObservers(Event event);
}

