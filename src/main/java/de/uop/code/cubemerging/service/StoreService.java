package de.uop.code.cubemerging.service;

import de.uop.code.cubemerging.util.Properties;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@Service
public class StoreService {

    private final static String CONTEXT_URI = "?context-uri=";

    public String persist(String content, ContentTypeRdf contentType, String context) {
        //System.out.println(content);
        HttpClient httpClient = HttpClients.createDefault();


        HttpPost httpPost = new HttpPost(Properties.getInstance().getStoreageServiceProvider() + CONTEXT_URI + context);

        httpPost.setHeader("Content-Type", contentType.getContentTypeRdf());

        try {
            httpPost.setEntity(new StringEntity(content));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        HttpResponse response = null;
        String result = "";
        try {
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            result = IOUtils.toString(entity.getContent(), "UTF-8");
            EntityUtils.consume(entity);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            httpPost.releaseConnection();
        }

        return result;
    }

}
