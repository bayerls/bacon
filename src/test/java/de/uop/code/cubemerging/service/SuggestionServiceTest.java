package de.uop.code.cubemerging.service;

import de.uop.code.cubemerging.CompareAlgorithm;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertTrue;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/dispatcher-servlet.xml")
public class SuggestionServiceTest {


    private final static String GRAPH = "http://code-research.eu/resource/cube/551b982f-14fd-441a-a78e-12ccdb950fda";
    private final static String AUTH = "8023903";


    @Autowired
    private SuggestionService suggestionService;


    @Test
    public void testGetSuggestionList() throws Exception {
        assertTrue(suggestionService.getSuggestionList(GRAPH, AUTH, CompareAlgorithm.FULL_STRUCTURE).size() > 0);
    }

}
