package de.uop.code.cubemerging.domain;

public class MergeLog {

    private int obsC1;
    private int obsC2;
    private int obsMerged;

    private int dims;
    private int meas;

    public int getDims() {
        return dims;
    }

    public void setDims(int dims) {
        this.dims = dims;
    }

    public int getMeas() {
        return meas;
    }

    public void setMeas(int meas) {
        this.meas = meas;
    }

    public int getObsC1() {
        return obsC1;
    }

    public void setObsC1(int obsC1) {
        this.obsC1 = obsC1;
    }

    public int getObsC2() {
        return obsC2;
    }

    public void setObsC2(int obsC2) {
        this.obsC2 = obsC2;
    }

    public int getObsMerged() {
        return obsMerged;
    }

    public void setObsMerged(int obsMerged) {
        this.obsMerged = obsMerged;
    }

    private int getUnmergedC1() {
        return obsC1 - obsMerged;
    }

    private int getUnmergedC2() {
        return obsC2 - obsMerged;
    }


    public int getPercentageC1() {
        if (getUnmergedC1() + getUnmergedC2() + obsMerged == 0) {
            return 0;
        }

        int percentage = (getUnmergedC1() * 100) / (getUnmergedC1() + getUnmergedC2() + obsMerged);

        return percentage;
    }

    public int getPercentageC2() {

        if (getUnmergedC1() + getUnmergedC2() + obsMerged == 0) {
            return 0;
        }

        int percentage = (getUnmergedC2() * 100) / (getUnmergedC1() + getUnmergedC2() + obsMerged);

        return percentage;
    }


    public int getPercentageOverlap() {
        int divideBy = getUnmergedC1() + getUnmergedC2() + obsMerged;

        if (divideBy == 0) {
            return 0;
        }

        int overlap = (obsMerged * 100) / divideBy;

        return overlap;
    }

    public int getPercentageDimensions() {

        if (dims + meas == 0) {
            return 0;
        }

        return 100 * dims / (dims + meas);
    }

    public int getPercentageMeasures() {

        if (dims + meas == 0) {
            return 0;
        }

        return 100 * meas / (dims + meas);
    }

}
