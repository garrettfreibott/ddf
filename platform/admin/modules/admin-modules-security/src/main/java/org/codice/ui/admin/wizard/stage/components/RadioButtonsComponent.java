package org.codice.ui.admin.wizard.stage.components;

public class RadioButtonsComponent extends Component<String>{

    private String[] options;

    public RadioButtonsComponent(String id) {
        super(id);
    }

    public RadioButtonsComponent setOptions(String[] options){
        this.options = options;
        return this;
    }

    public RadioButtonsComponent setDefault(String defaultValue){
        this.value(defaultValue);
        return this;
    }
}
