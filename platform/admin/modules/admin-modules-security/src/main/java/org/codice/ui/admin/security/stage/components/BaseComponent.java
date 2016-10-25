package org.codice.ui.admin.security.stage.components;

public interface BaseComponent<T> {

    void validate();

    void addError(String error);

    void clearAllErrors();

    boolean containsErrors();

    BaseComponent getComponent(String componentId);

    String getId();

    T getValue();

}
