package de.uop.code.cubemerging.domain;

import java.util.LinkedList;
import java.util.List;

public class MergedDSD {

    private String g1;
    private String g2;

    private List<MatchingComponent> matchingDimensions = new LinkedList<MatchingComponent>();
    private List<Component> dimensions1 = new LinkedList<Component>();
    private List<Component> dimensions2 = new LinkedList<Component>();

    private List<MatchingComponent> matchingMeasures = new LinkedList<MatchingComponent>();
    private List<Component> measures1 = new LinkedList<Component>();
    private List<Component> measures2 = new LinkedList<Component>();

    public String getG1() {
        return g1;
    }

    public void setG1(String g1) {
        this.g1 = g1;
    }

    public String getG2() {
        return g2;
    }

    public void setG2(String g2) {
        this.g2 = g2;
    }

    public List<MatchingComponent> getMatchingDimensions() {
        return matchingDimensions;
    }

    public void setMatchingDimensions(List<MatchingComponent> matchingDimensions) {
        this.matchingDimensions = matchingDimensions;
    }

    public List<Component> getDimensions1() {
        return dimensions1;
    }

    public void setDimensions1(List<Component> dimensions1) {
        this.dimensions1 = dimensions1;
    }

    public List<Component> getDimensions2() {
        return dimensions2;
    }

    public void setDimensions2(List<Component> dimensions2) {
        this.dimensions2 = dimensions2;
    }

    public List<MatchingComponent> getMatchingMeasures() {
        return matchingMeasures;
    }

    public void setMatchingMeasures(List<MatchingComponent> matchingMeasures) {
        this.matchingMeasures = matchingMeasures;
    }

    public List<Component> getMeasures1() {
        return measures1;
    }

    public void setMeasures1(List<Component> measures1) {
        this.measures1 = measures1;
    }

    public List<Component> getMeasures2() {
        return measures2;
    }

    public void setMeasures2(List<Component> measures2) {
        this.measures2 = measures2;
    }
}
