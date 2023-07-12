package org.example.survi;

import org.example.message.JsonMessage;
import org.example.survi.IndexedComponent;
import org.example.survi.IndexedComponentStorage;
import org.example.survi.circle.Circle;
import org.json.JSONObject;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.util.*;

public class HashMapIndexedComponentStorage<T extends IndexedComponent>
        implements IndexedComponentStorage<T>, Iterable<T> {
    HashMap<Integer, T> components;

    @Override
    public boolean hasInstanceWithId(int id) {
        return components.containsKey(id);
    }

    @Override
    public T getInstanceById(int id) throws NoSuchElementException {
        if (!components.containsKey(id)) {
            throw new NoSuchElementException("key not found");
        }
        return components.get(id);
    }

    @Override
    public T removeInstanceById(int id) throws NoSuchElementException {
        if (!components.containsKey(id)) {
            throw new NoSuchElementException("key not found");
        }
        return components.remove(id);
    }

    @Override
    public void addInstance(T component) throws KeyAlreadyExistsException {
        if (components.containsKey(component.getId())) {
            throw new KeyAlreadyExistsException("key already existed");
        }
        components.put(component.getId(), component);
    }

    @Override
    public Collection<T> getAllInstances() {
        return components.values();
    }

    @Override
    public Iterator<T> iterator() {
        return components.values().iterator();
    }

    @Override
    public int getNumInstances() {
        return components.size();
    }

}

//
//    @Override
//    public void start() {
//        super.start();
//        for(Map.Entry<Integer, T> component: components.entrySet()) {
//            component.getValue().start();
//        }
//    }
//
//    @Override
//    public void end() {
//        super.end();
//        for(Map.Entry<Integer, T> component: components.entrySet()) {
//            component.getValue().end();
//        }
//    }
//
//    @Override
//    public void update(long newTimeStamp) {
//        super.update(newTimeStamp);
//        for(Map.Entry<Integer, T> component: components.entrySet()) {
//            component.getValue().update(newTimeStamp);
//        }
//    }
//
//    @Override
//    public JSONObject getLastState() {
//        JSONObject JSONContent = new JSONObject();
//        for(Map.Entry<Integer, T> component: components.entrySet()) {
//            JSONContent.put(component.getKey().toString(), component.getValue().getLastState());
//        }
//        return JSONContent;
//    }
//
//    @Override
//    public void update(JsonMessage jsonMessage) {
//
//    }
//}
