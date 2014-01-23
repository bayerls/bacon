package de.uop.code.cubemerging.action;

import de.uop.code.cubemerging.SameAsResult;
import de.uop.code.cubemerging.service.BalloonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Use the balloon service to compute equivalent concepts bases on sameAs relationships.
 * Returns all pairs of equal concepts. A pair consists of a concept from the first and a concept from the second list.
 */
@Controller
@RequestMapping("/same-as")
public class SameAs {

    @Autowired
    private BalloonService balloonService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<SameAsResult> handleForm(@RequestParam ArrayList<String> conceptList1, @RequestParam ArrayList<String> conceptList2) {
        List<SameAsResult> sameAsResultList = new LinkedList<SameAsResult>();

        for (String c1 : conceptList1) {
            for (String c2 : conceptList2) {
                if (balloonService.isSameAs(c1, c2)) {
                    SameAsResult sameAsResult = new SameAsResult();
                    sameAsResult.setC1(c1);
                    sameAsResult.setC2(c2);
                    sameAsResult.setEqual(true);
                    sameAsResultList.add(sameAsResult);
                }
            }
        }

        return sameAsResultList;
    }

}
