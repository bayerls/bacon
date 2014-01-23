package de.uop.code.cubemerging.action;

import de.uop.code.cubemerging.service.DisambiguationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Calls the disambiguation service for a given surfaceForm.
 */
@Controller
@RequestMapping("/disambiguate")
public class Disambiguation {

    @Autowired
    private DisambiguationService disambiguationService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public String handleForm(String surfaceForm) {
        return disambiguationService.getDisambiguation(surfaceForm);
    }
}
