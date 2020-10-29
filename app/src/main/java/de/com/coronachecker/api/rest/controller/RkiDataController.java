package de.com.coronachecker.api.rest.controller;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

import de.com.coronachecker.api.County;
import lombok.SneakyThrows;


public class RkiDataController extends AsyncTask<County,Void,List<County>> {

    public List<County> getRkiData() throws JSONException {
        final String url = "https://services7.arcgis.com/mOBPykOjAyBO2ZKk/arcgis/rest/services/RKI_Landkreisdaten/FeatureServer/0/query";

        HttpHeaders headers = new HttpHeaders();
        RestTemplate restTemplate = new RestTemplate();
        List<County> countyList = new ArrayList<>();

        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

        UriComponents uriBuilder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("where", "1=1")
                .queryParam("outFields", "GEN", "cases7_per_100k")
                .queryParam("returnGeometry", "false")
                .queryParam("f", "json")
                .build();

        HttpEntity<?> entity = new HttpEntity<>(headers);

        HttpEntity<String> response = restTemplate.exchange(
                uriBuilder.toUriString(),
                HttpMethod.GET,
                entity,
                String.class);

        JSONObject responseJSON = new JSONObject(response.getBody());
        JSONArray jsonArray = responseJSON.getJSONArray("features");

        for (int i = 0; i < jsonArray.length(); i++) {

            countyList.add(County.builder()
                    .casesPer100k(jsonArray.getJSONObject(i).getJSONObject("attributes").getString("cases7_per_100k"))
                    .name(jsonArray.getJSONObject(i).getJSONObject("attributes").getString("GEN"))
                    .build());
        }


        return countyList;
    }

    @SneakyThrows
    @Override
    protected List<County> doInBackground(County... counties) {
        return this.getRkiData();
    }
}