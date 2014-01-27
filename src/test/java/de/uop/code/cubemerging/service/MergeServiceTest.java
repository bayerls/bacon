package de.uop.code.cubemerging.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertNotSame;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/dispatcher-servlet.xml")
public class MergeServiceTest {

    @Autowired
    private MergeService mergeService;

    @Test
    public void testGenerateContext() throws Exception {
        assertNotSame(mergeService.generateContext(), mergeService.generateContext());
    }
}
