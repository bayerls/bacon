package de.uop.code.cubemerging.service;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@Service
public class BalloonService {

    public boolean isSameAs(String uri1, String uri2) {

        if (uri1.equals(uri2)) {
            return true;
        } else {
            BalloonSameAsResponse same1 = getCluster(uri1);
            BalloonSameAsResponse same2 = getCluster(uri2);

            return isSameConcept(same1, same2);
        }
    }

    private boolean isSameConcept(BalloonSameAsResponse same1, BalloonSameAsResponse same2) {
        boolean result = false;

        if (same1.getSameAs() == null || same2.getSameAs() == null) {
            return false;
        }

        for (String concept : same1.getSameAs()) {
            if (same2.getSameAs().contains(concept)) {
                result = true;
                break;
            }
        }

        return result;
    }

    private BalloonSameAsResponse getCluster(String uri) {
        HttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(Properties.getInstance().getSameAsServiceProvider());
        List <NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("url", uri));

        try {
            post.setEntity(new UrlEncodedFormEntity(nvps));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

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

        ObjectMapper objectMapper = new ObjectMapper();
        BalloonSameAsResponse balloonSameAsResponse = null;

        try {
            balloonSameAsResponse = objectMapper.readValue(result, BalloonSameAsResponse.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return balloonSameAsResponse;
    }

}
