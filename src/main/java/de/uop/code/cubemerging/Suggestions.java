package de.uop.code.cubemerging;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Suggestions {

    private Map<String, String> equalStructure;
    private Map<String, String>  overlappingStructure;
    private Map<String, String>  others;

    public Suggestions() {
        equalStructure = new HashMap<String, String>();
        overlappingStructure = new HashMap<String, String>();
        others = new HashMap<String, String>();
    }

    public Map<String, String> getEqualStructure() {
        return equalStructure;
    }

    public void setEqualStructure(Map<String, String> equalStructure) {
        this.equalStructure = equalStructure;
    }

    public Map<String, String> getOverlappingStructure() {
        return overlappingStructure;
    }

    public void setOverlappingStructure(Map<String, String> overlappingStructure) {
        this.overlappingStructure = overlappingStructure;
    }

    public Map<String, String> getOthers() {
        return others;
    }

    public void setOthers(Map<String, String> others) {
        this.others = others;
    }
}
