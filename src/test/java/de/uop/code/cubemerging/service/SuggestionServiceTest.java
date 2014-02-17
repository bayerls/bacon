package de.uop.code.cubemerging.service;

import com.google.common.base.Stopwatch;
import de.uop.code.cubemerging.CompareAlgorithm;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.BufferedWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/dispatcher-servlet.xml")
public class SuggestionServiceTest {


    private final static String GRAPH = "http://code-research.eu/resource/cube/6ca726f9-cdb0-4057-b647-6c48b1597e2b";
    //private final static String AUTH = "8023903";

    private final static String[] GRAPHS = {
            "http://code-research.eu/resource/cube/55f5def5-d9b2-4d4f-bb1c-cb6724bda0c9",
            "http://code-research.eu/resource/cube/e023d48d-dea7-4c22-a9a0-43916115407d",
            "http://code-research.eu/resource/cube/7423430b-e6ba-495e-9399-96d6765ae7d8",
            "http://code-research.eu/resource/cube/fabc8137-f873-4fe7-807a-9f2e4bb6e0b1",
            "http://code-research.eu/resource/cube/d23517c8-4061-45fa-b9ba-c149a2d5cad3"
    };


    private final static int SKIP = 3;
    private final static int RUNS = SKIP + 5;

    @Autowired
    private SuggestionService suggestionService;

    @Autowired
    private MergeService mergeService;


    @Test
    public void testGetSuggestionListNoCacheDimsOnly() throws Exception {
        String file = "eval-noCache-dimsOnly.txt";
        CompareAlgorithm compareAlgorithm = CompareAlgorithm.DIMENSIONS_ONLY;

        mergeService.setCache(false);
        getSuggestionListHelper(file, compareAlgorithm, TimeUnit.SECONDS);
    }

    @Test
    public void testGetSuggestionListNoCacheFullStructure() throws Exception {
        String file = "eval-noCache-fullStructure.txt";
        CompareAlgorithm compareAlgorithm = CompareAlgorithm.FULL_STRUCTURE;
        mergeService.setCache(false);
        getSuggestionListHelper(file, compareAlgorithm, TimeUnit.SECONDS);
    }
    @Test
    public void testGetSuggestionListCacheDimsOnly() throws Exception {
        String file = "eval-cache-dimsOnly.txt";
        CompareAlgorithm compareAlgorithm = CompareAlgorithm.DIMENSIONS_ONLY;
        mergeService.setCache(true);
        getSuggestionListHelper(file, compareAlgorithm, TimeUnit.MILLISECONDS);
    }

    @Test
    public void testGetSuggestionListCacheFullStructure() throws Exception {
        String file = "eval-cache-fullStructure.txt";
        CompareAlgorithm compareAlgorithm = CompareAlgorithm.FULL_STRUCTURE;
        mergeService.setCache(true);
        getSuggestionListHelper(file, compareAlgorithm, TimeUnit.MILLISECONDS);
    }


    private void getSuggestionListHelper(String file, CompareAlgorithm compareAlgorithm, TimeUnit timeUnit) throws Exception {
        long[][] performance = new long[RUNS - SKIP][GRAPHS.length];

        for (int i = 0; i < GRAPHS.length; i++) {
            String auth = "" + (i + 1);

            for (int j = 0; j < RUNS; j++) {
                System.out.println("Run: " + i + " - " + j);
                Stopwatch stopwatch = Stopwatch.createStarted();
                assertTrue(suggestionService.getSuggestionList(GRAPHS[i], auth, compareAlgorithm).size() > 0);

                if (j + 1 > SKIP) {
                    performance[j - SKIP][i] = stopwatch.elapsed(timeUnit);
                }
            }
        }

        Path newFile = Paths.get(file);
        Files.deleteIfExists(newFile);

        newFile = Files.createFile(newFile);
        BufferedWriter writer = Files.newBufferedWriter(newFile, Charset.defaultCharset());


        for (int i = 0; i < performance.length; i++) {
            String line = "";
            for (int j = 0; j < performance[0].length; j++) {
                line += performance[i][j];

                if (j < performance[0].length - 1) {
                    line += ", ";
                }
            }

            writer.write(line);
            writer.newLine();
        }

        writer.flush();
    }

}
