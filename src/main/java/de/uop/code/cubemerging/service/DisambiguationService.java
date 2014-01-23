package de.uop.code.cubemerging.service;

import de.uop.code.cubemerging.util.Properties;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Service
public class DisambiguationService {

//    {
//        "documentUri": null,
//            "surfaceFormsToDisambiguate": [
//        {
//            "selectedText":"influenza",
//                "context":"",
//                "position": []
//        }
//        ]
//    }


    private final static String REQUEST_PREFIX = "{\"documentUri\": null, \"surfaceFormsToDisambiguate\": [{\"selectedText\":\"";
    private final static String REQUEST_SUFFIX = "\", \"context\":\"\",\"position\": []}]}";

    public String getDisambiguation(String surfaceForm) {
        String body = REQUEST_PREFIX + surfaceForm + REQUEST_SUFFIX;

        HttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(Properties.getInstance().getDisambiguationServiceProvider());
        post.setHeader("accept", "application/json");
        post.setHeader("content-type", "application/json");
        StringEntity stringEntity = null;

        try {
            stringEntity = new StringEntity(body);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        post.setEntity(stringEntity);
        HttpResponse response = null;

        try {
            response = client.execute(post);
        } catch (IOException e) {
            e.printStackTrace();
        }

        HttpEntity entity = response.getEntity();

        String result = "";
        try {
            result = IOUtils.toString(entity.getContent(), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            EntityUtils.consume(entity);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }


}
