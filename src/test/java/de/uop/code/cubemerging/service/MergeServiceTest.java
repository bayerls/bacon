package de.uop.code.cubemerging.service;

import com.google.common.base.Stopwatch;
import de.uop.code.cubemerging.CompareAlgorithm;
import de.uop.code.cubemerging.domain.RankItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/dispatcher-servlet.xml")
public class MergeServiceTest {

    @Autowired
    private MergeService mergeService;

    @Autowired
    private SuggestionService suggestionService;

    @Autowired
    private StoreService storeService;

    @Test
    public void testGenerateContext() throws Exception {
        assertNotSame(mergeService.generateContext(), mergeService.generateContext());
    }

    @Test
    public void testMerge() throws Exception {
        String graph = "http://code-research.eu/resource/cube/f3e32e5e-603d-47c9-997e-91bac6d7c849";
        String auth = "8023903";
        mergeService.setAuth(auth);

        List<RankItem> suggestions = suggestionService.getSuggestionList(graph, auth, CompareAlgorithm.FULL_STRUCTURE);
        List<RankItem> matchingItems = new ArrayList<RankItem>();

        for (RankItem rankItem : suggestions) {
            if (rankItem.getScore() == 1.0) {
                matchingItems.add(rankItem);
            }
        }

        assertEquals(25, matchingItems.size());

        String currentMergedGraph = matchingItems.get(0).getDatasetDescription().getNamedGraph();

        String label = "headlessMergedCube";
        String description = "";
        List<String> contextList = new ArrayList<String>();
        Stopwatch stopwatch = Stopwatch.createStarted();

        int i = 0;
        for (RankItem rankItem : matchingItems) {
            description += rankItem.getDatasetDescription().getLabel() + "  +  ";
            mergeService.getMergedDSD(currentMergedGraph, rankItem.getDatasetDescription().getNamedGraph());
            mergeService.createGenericMergedDataset(label, description);

            currentMergedGraph = mergeService.generateContext();
            contextList.add(currentMergedGraph);
            storeService.persist(mergeService.getMergedDataset(), ContentTypeRdf.N3, currentMergedGraph);

            String id = currentMergedGraph.replace(MergeService.CONTEXT_REFIX, "");

            PrintWriter writer = new PrintWriter("/Users/Basti/Desktop/dump/" + i + "-" + id + ".n3", "UTF-8");
            writer.println(mergeService.getMergedDataset());
            writer.close();

            System.out.println("---");
            System.out.println(currentMergedGraph);
            System.out.println(mergeService.getLog().getObsC1() + mergeService.getLog().getObsC2() - mergeService.getLog().getObsMerged() + ", " + stopwatch.elapsed(TimeUnit.SECONDS));

            i++;

        // TODO write asserts for the contextList


    }
}
