package org.example.survi;

public interface IndexedComponentFactory<T extends IndexedComponent> {
    T getInstance();
}
