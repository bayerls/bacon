package de.uop.code.cubemerging.action;

import de.uop.code.cubemerging.domain.DatasetDescription;
import de.uop.code.cubemerging.service.MergeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Collections;
import java.util.List;


/**
 *  Provides a list of cubes. If auth is null, all public cubes will be returned, otherwise the cubes for this specific Mendeley ID are returned.
 */
@Controller
@RequestMapping("/select")
public class Select {

    @Autowired
    private MergeService mergeService;

    @RequestMapping(method = RequestMethod.GET)
    public String handleForm(Model model, String auth) {

        if (auth == null || auth.isEmpty()) {
            auth = "public";
        }

        List<DatasetDescription> list = mergeService.getDatasetDescriptions(auth);
        Collections.reverse(list);

        model.addAttribute("auth", auth);
        model.addAttribute("datasetDescriptions", list);

        return "select";
    }

}
