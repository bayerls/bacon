package de.uop.code.cubemerging.service;

import de.uop.code.cubemerging.StructureDefinition;
import de.uop.code.cubemerging.CompareAlgorithm;
import de.uop.code.cubemerging.domain.DatasetDescription;
import de.uop.code.cubemerging.domain.RankItem;
import de.uop.code.cubemerging.domain.RankItemComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Service
public class SuggestionService {

    @Autowired
    private MergeService mergeService;

    public List<RankItem> getSuggestionList(String g1, String auth, CompareAlgorithm compareAlgorithm) {

        // Check here the compareAlgorithm, if it is not (only) based on the DSD
        List<RankItem> items = new LinkedList<RankItem>();
        items.addAll(getRankedList(g1, auth, compareAlgorithm));

        return items;
    }

    private List<RankItem> getRankedList(String g1, String auth, CompareAlgorithm compareAlgorithm) {
        List<RankItem> items = new LinkedList<RankItem>();
        List<DatasetDescription> descriptions = mergeService.getDatasetDescriptions(auth);
        // compute all ranked items
        for (DatasetDescription description : descriptions) {
            double score = getScoreByGraph(g1, description.getNamedGraph(), compareAlgorithm);
            RankItem ri = new RankItem();
            ri.setScore(score);
            ri.setDatasetDescription(description);
            items.add(ri);
        }

        // sort the items
        Collections.sort(items, Collections.reverseOrder(new RankItemComparator()));

        return items;
    }

    private double getScoreByGraph(String g1, String g2, CompareAlgorithm compareAlgorithm) {
        StructureDefinition sd1 = mergeService.getStructureDetails(g1);
        StructureDefinition sd2 = mergeService.getStructureDetails(g2);

        return getScore(sd1, sd2, compareAlgorithm);
    }

    public double getScore(StructureDefinition sd1, StructureDefinition sd2, CompareAlgorithm compareAlgorithm) {
        int equalDims = 0;
        int equalMeas = 0;

        int totalDims1 = sd1.getDimensions().size();
        int totalDims2 = sd2.getDimensions().size();

        int totalMeas1 = sd1.getMeasures().size();
        int totalMeas2 = sd2.getMeasures().size();

        if (totalDims1 == 0 || totalDims2 == 0 || totalMeas1 == 0 || totalMeas2 == 0) {
            // potentially invalid cube!
            return 0.0;
        }

        for (String dim1 : sd1.getDimensions()) {
            if (sd2.getDimensions().contains(dim1)) {
                equalDims++;
            }
        }

        for (String meas1 : sd1.getMeasures()) {
            if (sd2.getMeasures().contains(meas1)) {
                equalMeas++;
            }
        }

        double score = 0.0;

        if (compareAlgorithm == CompareAlgorithm.FULL_STRUCTURE) {
            score = computeScoreFullStructure(equalDims, equalMeas, totalDims1, totalDims2, totalMeas1, totalMeas2);
        } else if (compareAlgorithm == CompareAlgorithm.DIMENSIONS_ONLY) {
            score = computeScoreDimensionsOnly(equalDims, totalDims1, totalDims2);
        }

        return score;
    }


    private double computeScoreFullStructure(double equalDims, double equalMeas, double totalDims1, double totalDims2, double totalMeas1, double totalMeas2) {
        double result;
        result = equalDims / totalDims1 + equalDims / totalDims2 + equalMeas / totalMeas1 + equalMeas / totalMeas2;
        result = result / 4.0;

        return result;
    }

    private double computeScoreDimensionsOnly(double equalDims, double totalDims1, double totalDims2) {
        double result;

        result = equalDims / totalDims1 + equalDims / totalDims2;
        result = result / 2.0;

        return result;
    }

}
