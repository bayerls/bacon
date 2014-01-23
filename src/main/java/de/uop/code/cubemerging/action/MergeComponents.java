package de.uop.code.cubemerging.action;

import de.uop.code.cubemerging.domain.ComponentType;
import de.uop.code.cubemerging.service.MergeService;
import de.uop.code.cubemerging.service.SVGService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * Merges two components by calling the mergeService with the corresponding parameter. Forwards to the merge.jsp
 */
@Controller
@RequestMapping("/merge-component")
@SessionAttributes({"mergeService"})
public class MergeComponents {

    @RequestMapping(method = RequestMethod.GET)
    public String handleForm(@ModelAttribute(value = "mergeService") MergeService mergeService,
                             Model model,
                             String compG1,
                             String compG2,
                             String label,
                             String range,
                             String resource) {

        ComponentType type = mergeService.isMergable(compG1, compG2);

        // only merge if two dimensions or two measures are selected
        if (type != ComponentType.UNKNOWN) {
            mergeService.mergeComponents(type, compG1, compG2, label, range, resource);
        }

        model.addAttribute("mergedDSD", mergeService.getMergedDSD());
        model.addAttribute("g1Desc", mergeService.getDatasetDescription(mergeService.getMergedDSD().getG1(), mergeService.getAuth()));
        model.addAttribute("g2Desc", mergeService.getDatasetDescription(mergeService.getMergedDSD().getG2(), mergeService.getAuth()));
        mergeService.createGenericMergedDataset(null, null);
        model.addAttribute("log", mergeService.getLog());
        model.addAttribute("svg", SVGService.generateSVG(mergeService.getLog()));

        return "merge";
    }

}
