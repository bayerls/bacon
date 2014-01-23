package de.uop.code.cubemerging;

import java.util.HashMap;
import java.util.Map;

public class Observation {

    private String dataset;

    private String resource;

    private Map<String, String> measures;
    private Map<String, String> dimensions;

    public Observation() {
        measures = new HashMap<String, String>();
        dimensions = new HashMap<String, String>();
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getDataset() {
        return dataset;
    }

    public void setDataset(String dataset) {
        this.dataset = dataset;
    }

    public Map<String, String> getMeasures() {
        return measures;
    }

    public void setMeasures(Map<String, String> measures) {
        this.measures = measures;
    }

    public Map<String, String> getDimensions() {
        return dimensions;
    }

    public void setDimensions(Map<String, String> dimensions) {
        this.dimensions = dimensions;
    }
}
