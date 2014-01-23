package de.uop.code.cubemerging.domain;

import de.uop.code.cubemerging.service.MatchingType;

public class MatchingComponent {

    private Component component1;
    private Component component2;

    private String resource;
    private String label;
    private String range;

    private MatchingType matchingType;

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public MatchingType getMatchingType() {
        return matchingType;
    }

    public void setMatchingType(MatchingType matchingType) {
        this.matchingType = matchingType;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Component getComponent1() {
        return component1;
    }

    public void setComponent1(Component component1) {
        this.component1 = component1;
    }

    public Component getComponent2() {
        return component2;
    }

    public void setComponent2(Component component2) {
        this.component2 = component2;
    }
}
