package de.uop.code.cubemerging.domain.cube;

public class Component {

    private String label;
    private String url;
    private String subpropertyOf;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSubpropertyOf() {
        return subpropertyOf;
    }

    public void setSubpropertyOf(String subpropertyOf) {
        this.subpropertyOf = subpropertyOf;
    }
}
