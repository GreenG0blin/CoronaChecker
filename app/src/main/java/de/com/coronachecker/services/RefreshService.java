package de.com.coronachecker.services;

import android.os.AsyncTask;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import de.com.coronachecker.api.model.County;
import de.com.coronachecker.api.rest.RkiDataController;
import de.com.coronachecker.persistence.entities.Person;
import lombok.Builder;
import lombok.SneakyThrows;

@Builder
public class RefreshService extends AsyncTask<Void, Void, List<Person>> {

    private final List<Person> personList;

    private List<Person> refreshPersonList(List<Person> personList) throws JSONException {
        RkiDataController rkiDataController = new RkiDataController();
        List<County> countyList = rkiDataController.getRkiData();
        List<Person> newPersonList = new ArrayList<>();

        personList.forEach(person -> {
            Optional<County> countyForPerson = countyList.stream()
                    .filter(county -> person.getCounty().equals(county.getName()))
                    .findFirst();

            countyForPerson.ifPresent(county -> {
                person.setSevenDaysIncidence(county.getCasesPer100k());
                person.setStatus(county.getStatus());
                person.setLastUpdated(county.getLastUpdated());
                newPersonList.add(person);
            });
        });
        return newPersonList;
    }

    @SneakyThrows
    @Override
    protected List<Person> doInBackground(Void... voids) {
        return refreshPersonList(this.personList);
    }
}
