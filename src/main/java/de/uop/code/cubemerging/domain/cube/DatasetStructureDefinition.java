package de.uop.code.cubemerging.domain.cube;

import java.util.LinkedList;
import java.util.List;

public class DatasetStructureDefinition {

    private List<Dimension> dimensions = new LinkedList<Dimension>();
    private List<Measure> measures = new LinkedList<Measure>();

    public List<Dimension> getDimensions() {
        return dimensions;
    }

    public void setDimensions(List<Dimension> dimensions) {
        this.dimensions = dimensions;
    }

    public List<Measure> getMeasures() {
        return measures;
    }

    public void setMeasures(List<Measure> measures) {
        this.measures = measures;
    }
}
