package de.uop.code.cubemerging.service;

import de.uop.code.cubemerging.util.Properties;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class StoreService {

    private final Logger logger = LoggerFactory.getLogger(StoreService.class);

    public String storeDump(String content, String context) {
        String result = "error occured";

        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(Properties.getInstance().getStoreageServiceProvider());

        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("content", content));
        nvps.add(new BasicNameValuePair("context", context));

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nvps));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            HttpResponse response = httpClient.execute(httpPost);
            try {
                logger.info(response.getStatusLine().toString());
                HttpEntity entity = response.getEntity();
                result = IOUtils.toString(entity.getContent(), "UTF-8");
                EntityUtils.consume(entity);
            } finally {
                httpPost.releaseConnection();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

}
