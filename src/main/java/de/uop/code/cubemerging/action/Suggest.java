package de.uop.code.cubemerging.action;

import de.uop.code.cubemerging.CompareAlgorithm;
import de.uop.code.cubemerging.service.MergeService;
import de.uop.code.cubemerging.service.SuggestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Compute the list of ranked and suggested cubes. Similarity algorithm is chosen depending on the compare parameter.
 */
@Controller
@RequestMapping("/suggest")
public class Suggest {

    @Autowired
    private MergeService mergeService;

    @Autowired
    private SuggestionService suggestionService;

    @RequestMapping(method = RequestMethod.GET)
    public String handleForm(Model model, String graph, String auth, String compare) {

        CompareAlgorithm compareAlgorithm = CompareAlgorithm.FULL_STRUCTURE;

        if ("fullStructure".equals(compare)) {
            compareAlgorithm = CompareAlgorithm.FULL_STRUCTURE;
        } else if ("dimensionsOnly".equals(compare)) {
            compareAlgorithm = CompareAlgorithm.DIMENSIONS_ONLY;
        }

        model.addAttribute("cubeInfo", mergeService.getCubeInfo(graph, auth));
        model.addAttribute("rankedItems", suggestionService.getSuggestionList(graph, auth, compareAlgorithm));
        model.addAttribute("compareAlgorithm", compareAlgorithm);
        model.addAttribute("auth", auth);

        return "suggest";
    }
}
