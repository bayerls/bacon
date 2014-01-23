package de.uop.code.cubemerging;

import java.util.List;

public class StructureDefinition {

    private List<String> dimensions;
    private List<String> measures;

    public List<String> getDimensions() {
        return dimensions;
    }

    public void setDimensions(List<String> dimensions) {
        this.dimensions = dimensions;
    }

    public List<String> getMeasures() {
        return measures;
    }

    public void setMeasures(List<String> measures) {
        this.measures = measures;
    }
}
