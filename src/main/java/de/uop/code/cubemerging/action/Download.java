package de.uop.code.cubemerging.action;

import de.uop.code.cubemerging.service.MergeService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


/**
 * Enables the download of the currently merged dataset. The name parameter is added to the filename.
 */
@Controller
@RequestMapping("/download")
@SessionAttributes("mergeService")
public class Download {

    @RequestMapping(method = RequestMethod.GET)
    public HttpEntity<byte[]> getFile(@ModelAttribute MergeService mergeService, @RequestParam("name") String name) {

        String dataset = mergeService.getMergedDataset();
        byte[] documentBody = dataset.getBytes();

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.TEXT_PLAIN);
        header.set("Content-Disposition", "attachment; filename=" + name + "DatasetDump.n3");
        header.setContentLength(documentBody.length);

        return new HttpEntity<byte[]>(documentBody, header);
    }

}

