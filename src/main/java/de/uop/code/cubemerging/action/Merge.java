package de.uop.code.cubemerging.action;

import de.uop.code.cubemerging.service.MergeService;
import de.uop.code.cubemerging.service.SVGService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * Initially computes the merged dataset. Forwards to the merge.jsp
 */
@Controller
@RequestMapping("/merge")
@SessionAttributes({"mergeService"})
public class Merge {

    @Autowired
    private MergeService mergeService;

    @RequestMapping(method = RequestMethod.GET)
    public String handleForm(Model model,
                             @RequestParam String g1,
                             @RequestParam String g2,
                             @RequestParam String auth) {

        mergeService.setAuth(auth);
        model.addAttribute("mergeService", mergeService);
        model.addAttribute("mergedDSD", mergeService.getMergedDSD(g1, g2));
        model.addAttribute("g1Desc", mergeService.getDatasetDescription(g1, auth));
        model.addAttribute("g2Desc", mergeService.getDatasetDescription(g2, auth));
        mergeService.createGenericMergedDataset(null, null);
        model.addAttribute("log", mergeService.getLog());
        model.addAttribute("svg", SVGService.generateSVG(mergeService.getLog()));

        return "merge";
    }

}
