package de.com.coronachecker.view;

import android.app.Application;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import de.com.coronachecker.persistence.entities.Person;
import de.com.coronachecker.persistence.repo.DataRepository;
import de.com.coronachecker.services.PersonService;
import de.com.coronachecker.services.RefreshService;

public class PersonViewModel extends AndroidViewModel {

    private DataRepository dataRepository;

    public PersonViewModel(@NonNull Application application) {
        super(application);
        dataRepository = new DataRepository(application);
    }

    public LiveData<List<Person>> getAllPersons() {
        return dataRepository.getAllPersons();
    }

    public Optional<Person> insertFromInput(Intent data) throws InterruptedException, ExecutionException {
        PersonService personService = PersonService.builder()
                .data(data)
                .dataRepository(dataRepository)
                .build();

        Optional<Person> person = personService.execute().get();

        person.ifPresent(p -> dataRepository.insert(p));

        return person;
    }

    public void refreshData() throws ExecutionException, InterruptedException {

        LiveData<List<Person>> personList = dataRepository.getAllPersons();
        RefreshService refreshService = RefreshService.builder()
                .personList(personList.getValue())
                .build();

        List<Person> newPersonList = refreshService.execute().get();

        newPersonList.forEach(person -> dataRepository.update(person));

    }

}
