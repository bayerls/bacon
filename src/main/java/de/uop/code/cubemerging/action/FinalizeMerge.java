package de.uop.code.cubemerging.action;

import de.uop.code.cubemerging.service.MergeService;
import de.uop.code.cubemerging.service.StoreService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * Uses the StoreService to persist the cube.
 */
@Controller
@RequestMapping("/finalize-merge")
@SessionAttributes({"mergeService"})
public class FinalizeMerge {

    @RequestMapping(method = RequestMethod.GET)
    public String handleForm(@ModelAttribute(value = "mergeService") MergeService mergeService, Model model, String label, String description) {
        mergeService.createGenericMergedDataset(label, description);
        StoreService storeService = new StoreService();
        String context = mergeService.generateContext();
        storeService.storeDump(mergeService.getMergedDataset(), context);

//        System.out.println(mergeService.getMergedDataset());

        model.addAttribute("label", label);
        model.addAttribute("description", description);
        model.addAttribute("context", context);
        model.addAttribute("auth", mergeService.getAuth());

        return "finalize";
    }
}
