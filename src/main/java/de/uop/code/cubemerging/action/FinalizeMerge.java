package de.uop.code.cubemerging.action;

import de.uop.code.cubemerging.service.ContentTypeRdf;
import de.uop.code.cubemerging.service.MergeService;
import de.uop.code.cubemerging.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private StoreService storeService;

    @RequestMapping(method = RequestMethod.GET)
    public String handleForm(@ModelAttribute(value = "mergeService") MergeService mergeService, Model model, String label, String description) {
        mergeService.createGenericMergedDataset(label, description);
        String context = mergeService.generateContext();
        storeService.persist(mergeService.getMergedDataset(), ContentTypeRdf.N3, context);

        model.addAttribute("label", label);
        model.addAttribute("description", description);
        model.addAttribute("context", context);
        model.addAttribute("auth", mergeService.getAuth());

        return "finalize";
    }
}
