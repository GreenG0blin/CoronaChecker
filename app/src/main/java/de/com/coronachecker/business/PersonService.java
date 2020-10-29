package de.com.coronachecker.business;

import android.app.Application;
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
import java.util.Optional;

import de.com.coronachecker.api.County;
import de.com.coronachecker.persistence.entities.Person;
import de.com.coronachecker.persistence.repo.DataRepository;
import lombok.SneakyThrows;

public class PersonService extends AsyncTask<Void, Void, Person> {

    private final Application application;
    private final String zipcode;

    public PersonService(Application application, String zipcode) {
        this.application = application;
        this.zipcode = zipcode;
    }

    public Person mapZipcodeToPerson() throws JSONException {
        Person person = new Person();

        DataRepository dataRepository = new DataRepository(application);
        String county = dataRepository.getCountyByZipcode(zipcode);
        float casesPer100k;

        List<County> countyList = getRkiData();
        Optional<County> specificCounty = countyList.stream()
                .filter(c -> c.getName().equals(county))
                .findFirst();


        if (specificCounty.isPresent()) {
            person.setCounty(specificCounty.get().getName());
            casesPer100k = Float.parseFloat(specificCounty.get().getCasesPer100k());
            person.setSevenDaysIncidence(casesPer100k);

            if (casesPer100k > 50) {
                person.setStatus(de.com.coronachecker.persistence.entities.Status.RED);
            }
            if (casesPer100k > 30 && casesPer100k < 50) {
                person.setStatus(de.com.coronachecker.persistence.entities.Status.YELLOW);
            }
            if (casesPer100k < 30) {
                person.setStatus(de.com.coronachecker.persistence.entities.Status.GREEN);
            }
        }

        return person;
    }

    private List<County> getRkiData() throws JSONException {
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
    protected Person doInBackground(Void... voids) {
        return mapZipcodeToPerson();
    }
}
