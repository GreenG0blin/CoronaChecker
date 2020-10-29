package de.com.coronachecker.view;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import de.com.coronachecker.persistence.entities.Person;
import de.com.coronachecker.persistence.repo.DataRepository;

public class PersonViewModel extends AndroidViewModel {

    private DataRepository dataRepository;
    private LiveData<List<Person>> mAllPersons;

    public PersonViewModel(@NonNull Application application) {
        super(application);
        dataRepository = new DataRepository(application);
        mAllPersons = dataRepository.getAllPersons();
    }

    public LiveData<List<Person>> getAllPersons() {
        return mAllPersons;
    }

    public void insert(Person person) {
        dataRepository.insert(person);
    }

}
