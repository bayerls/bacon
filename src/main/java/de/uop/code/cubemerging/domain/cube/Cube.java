package de.uop.code.cubemerging.domain.cube;

public class Cube {

    private String label;
    private String description;
    private String auth;
    private String id;
    private String graph;
    private DatasetStructureDefinition datasetStructureDefinition;

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGraph() {
        return graph;
    }

    public void setGraph(String graph) {
        this.graph = graph;
    }

    public DatasetStructureDefinition getDatasetStructureDefinition() {
        return datasetStructureDefinition;
    }

    public void setDatasetStructureDefinition(DatasetStructureDefinition datasetStructureDefinition) {
        this.datasetStructureDefinition = datasetStructureDefinition;
    }
}
