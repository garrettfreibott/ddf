package org.codice.ui.admin.wizard.stage.components;

public class StatusPageComponent extends Component<String>{

    private boolean succeeded;

    public StatusPageComponent() {
        super(null);
    }

    public StatusPageComponent succeeded(boolean succeeded) {
        this.succeeded = succeeded;
        return this;
    }
}
