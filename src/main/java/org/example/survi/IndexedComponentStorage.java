package org.example.survi;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.util.Collection;
import java.util.NoSuchElementException;

public interface IndexedComponentStorage<T extends IndexedComponent>  {
    void addInstance(T component) throws KeyAlreadyExistsException;
    T getInstanceById(int id) throws NoSuchElementException;
    T removeInstanceById(int id) throws NoSuchElementException;
    boolean hasInstanceWithId(int id);
    Collection<T> getAllInstances();

    int getNumInstances();
}
