package de.uop.code.cubemerging.domain;

import java.util.Comparator;

public class RankItemComparator implements Comparator<RankItem> {
    @Override
    public int compare(RankItem o1, RankItem o2) {
        return Double.compare(o1.getScore(), o2.getScore());

    }
}
