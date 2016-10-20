package org.codice.ui.admin.security.stage.components;

public class ButtonActionComponent extends Component {

    private String url;

    private Method method = Method.GET;

    public ButtonActionComponent() {
        super(null);
    }

    public ButtonActionComponent setUrl(String url) {
        this.url = url;
        return this;
    }

    public ButtonActionComponent setMethod(Method method) {
        this.method = method;
        return this;
    }

    public enum Method {GET, POST}

}
