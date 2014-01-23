package de.uop.code.cubemerging.domain;


public class RankItem {

    private double score;
    private DatasetDescription datasetDescription;

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public DatasetDescription getDatasetDescription() {
        return datasetDescription;
    }

    public void setDatasetDescription(DatasetDescription datasetDescription) {
        this.datasetDescription = datasetDescription;
    }
}
