package de.uop.code.cubemerging.action;


import de.uop.code.cubemerging.service.MergeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * Forwards to the store.jsp, while maintaining the session.
 */
@Controller
@RequestMapping("/prepare-store")
@SessionAttributes({"mergeService"})
public class PrepareStore {

    @Autowired
    private MergeService mergeService;

    @RequestMapping(method = RequestMethod.GET)
    public String handleForm(Model model) {
        return "store";
    }

}
