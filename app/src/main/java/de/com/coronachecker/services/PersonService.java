package de.com.coronachecker.services;

import android.app.Application;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import de.com.coronachecker.R;
import de.com.coronachecker.activities.NewPersonActivity;
import de.com.coronachecker.api.model.County;
import de.com.coronachecker.api.rest.RkiDataController;
import de.com.coronachecker.persistence.entities.Person;
import de.com.coronachecker.persistence.repo.DataRepository;
import lombok.Builder;
import lombok.SneakyThrows;

@Builder
public class PersonService extends AsyncTask<Application, Void, Optional<Person>> {

    private final DataRepository dataRepository;
    private final Intent data;

    private Person createPersonByInput(DataRepository dataRepository, Intent data) throws JSONException {
        Objects.requireNonNull(data);
        Objects.requireNonNull(dataRepository);

        Optional<County> specificCounty = Optional.empty();

        Optional<String> county = dataRepository.getCountyByZipcode(data.getStringExtra(NewPersonActivity.EXTRA_REPLY_ZIPCODE));

        RkiDataController rkiDataController = new RkiDataController();
        List<County> countyList = rkiDataController.getRkiData();

        if (county.isPresent()) {
            specificCounty = countyList.stream()
                    .filter(c -> c.getName().equals(county.get()))
                    .findFirst();
        }

        return specificCounty.map(value -> Person.builder()
                .name(data.getStringExtra(NewPersonActivity.EXTRA_REPLY_NAME))
                .county(value.getName())
                .sevenDaysIncidence(value.getCasesPer100k())
                .status(value.getStatus())
                .lastUpdated(value.getLastUpdated())
                .build()).orElse(null);
    }

    @SneakyThrows
    @Override
    protected Optional<Person> doInBackground(Application... applications) {
        return Optional.ofNullable(createPersonByInput(this.dataRepository, this.data));
    }
}
