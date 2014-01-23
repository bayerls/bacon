package de.uop.code.cubemerging.action;

import de.uop.code.cubemerging.domain.MergedDSD;
import de.uop.code.cubemerging.service.MergeService;
import de.uop.code.cubemerging.service.SVGService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * Adds a dimension to a specified cube. Forwards to the merge.jsp
 */
@Controller
@RequestMapping("/add-dimension")
@SessionAttributes({"mergeService"})
public class AddDimension {

    @RequestMapping(method = RequestMethod.GET)
    public String handleForm(@ModelAttribute(value = "mergeService") MergeService mergeService,
                             Model model,
                             String graph,
                             String label,
                             String resource,
                             String value) {

        mergeService.addDimension(graph, label, resource, value);

        MergedDSD mergedDSD = mergeService.getMergedDSD();
        model.addAttribute("mergedDSD", mergedDSD);
        mergeService.createGenericMergedDataset(null, null);
        model.addAttribute("log", mergeService.getLog());
        model.addAttribute("svg", SVGService.generateSVG(mergeService.getLog()));
        model.addAttribute("mergeService", mergeService);
        model.addAttribute("g1Desc", mergeService.getDatasetDescription(mergedDSD.getG1(), mergeService.getAuth()));
        model.addAttribute("g2Desc", mergeService.getDatasetDescription(mergedDSD.getG2(), mergeService.getAuth()));

        return "merge";
    }

}