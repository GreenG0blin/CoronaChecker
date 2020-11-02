package de.com.coronachecker.api.rest;

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

import de.com.coronachecker.api.model.County;


public class RkiDataController {

    public List<County> getRkiData() throws JSONException {
        final String url = "https://services7.arcgis.com/mOBPykOjAyBO2ZKk/arcgis/rest/services/RKI_Landkreisdaten/FeatureServer/0/query";

        HttpHeaders headers = new HttpHeaders();
        RestTemplate restTemplate = new RestTemplate();
        List<County> countyList = new ArrayList<>();

        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

        UriComponents uriBuilder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("where", "1=1")
                .queryParam("outFields", "GEN", "cases7_per_100k", "last_update")
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

            County county = County.builder()
                    .casesPer100k(Float.parseFloat(jsonArray.getJSONObject(i).getJSONObject("attributes").getString("cases7_per_100k")))
                    .name(jsonArray.getJSONObject(i).getJSONObject("attributes").getString("GEN"))
                    .lastUpdated(jsonArray.getJSONObject(i).getJSONObject("attributes").getString("last_update"))
                    .build();

            if (county.getCasesPer100k() > 50) {
                county.setStatus(de.com.coronachecker.persistence.entities.enums.Status.RED);
            }
            if (county.getCasesPer100k() > 30 && county.getCasesPer100k() < 50) {
                county.setStatus(de.com.coronachecker.persistence.entities.enums.Status.YELLOW);
            }
            if (county.getCasesPer100k() < 30) {
                county.setStatus(de.com.coronachecker.persistence.entities.enums.Status.GREEN);
            }
            countyList.add(county);
        }

        return countyList;
    }
}