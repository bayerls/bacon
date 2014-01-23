package de.uop.code.cubemerging.service;


import de.uop.code.cubemerging.domain.MergeLog;
import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.svggen.SVGGraphics2DIOException;
import org.springframework.stereotype.Service;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

import java.awt.*;
import java.io.StringWriter;
import java.io.Writer;

@Service
public class SVGService {

    private static Paint C_1_DIMS = new Color(20, 147, 0);
    private static Paint C_1_MEAS = new Color(147 ,147 ,147);
    private static Paint C_2_DIMS = new Color(67, 84, 225);
    private static Paint C_2_MEAS = new Color(147 ,147 ,147);
    private static Paint OVERLAP_DIMS = new Color(213, 62, 37);
    private static Paint OVERLAP_MEAS = new Color(213, 62, 37);

//    public static void main(String[] args) {
//
//        MergeLog log = new MergeLog();
//        log.setObsC1(200);
//        log.setObsC2(40);
//        log.setObsMerged(5);
//
//        log.setDims(10);
//        log.setMeas(2);
//
//        generateSVG(log);
//
//    }

    public static String generateSVG(MergeLog log) {
        // Get a DOMImplementation.
        DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();

        // Create an instance of org.w3c.dom.Document.
        String svgNS = "http://www.w3.org/2000/svg";
        Document document = domImpl.createDocument(svgNS, "svg", null);

        // Create an instance of the SVG Generator.
        SVGGraphics2D svgGenerator = new SVGGraphics2D(document);

        int currentOffsetX = 10;
        int currentOffsetY = 10;

        int spaceX = 5;
        int spaceY = 2;

        // 1 2 <- first cube
        // 3 4 <- overlapping
        // 5 6 <- second cube

        // 1
        svgGenerator.setPaint(C_1_DIMS);
        svgGenerator.fill(new Rectangle(currentOffsetX, currentOffsetY, log.getPercentageDimensions(), log.getPercentageC1()));

        // 2
        currentOffsetX += log.getPercentageDimensions() + spaceX;

        svgGenerator.setPaint(C_1_MEAS);
        svgGenerator.fill(new Rectangle(currentOffsetX, currentOffsetY, log.getPercentageMeasures(), log.getPercentageC1()));

        // 3
        currentOffsetX -= log.getPercentageDimensions() + spaceX;
        currentOffsetY += log.getPercentageC1() + spaceY;

        svgGenerator.setPaint(OVERLAP_DIMS);
        svgGenerator.fill(new Rectangle(currentOffsetX, currentOffsetY, log.getPercentageDimensions(), log.getPercentageOverlap()));

        // 4
        currentOffsetX += log.getPercentageDimensions() + spaceX;

        svgGenerator.setPaint(OVERLAP_MEAS);
        svgGenerator.fill(new Rectangle(currentOffsetX, currentOffsetY, log.getPercentageMeasures(), log.getPercentageOverlap()));

        // 5
        currentOffsetX -= log.getPercentageDimensions() + spaceX;
        currentOffsetY += log.getPercentageOverlap() + spaceY;

        svgGenerator.setPaint(C_2_DIMS);
        svgGenerator.fill(new Rectangle(currentOffsetX, currentOffsetY , log.getPercentageDimensions(), log.getPercentageC2()));

        // 6
        currentOffsetX += log.getPercentageDimensions() + spaceX;

        svgGenerator.setPaint(C_2_MEAS);
        svgGenerator.fill(new Rectangle(currentOffsetX, currentOffsetY, log.getPercentageMeasures(), log.getPercentageC2()));

        currentOffsetX += log.getPercentageMeasures() + 10;
        currentOffsetY += log.getPercentageC2() + 10;

        // resize SVG
        svgGenerator.setSVGCanvasSize(new Dimension(currentOffsetX, currentOffsetY));

        boolean useCSS = true;

//        FileWriter fileWriter = null;
//        try {
//            fileWriter = new FileWriter("/Users/Basti/Desktop/test.svg");
//            svgGenerator.stream(fileWriter, useCSS);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        Writer out = new StringWriter();

        try {
            svgGenerator.stream(out, useCSS);
        } catch (SVGGraphics2DIOException e) {
            e.printStackTrace();
        }

        return out.toString();
    }


}
