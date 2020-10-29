package de.com.coronachecker.persistence.repo;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import de.com.coronachecker.persistence.PersonDatabase;
import de.com.coronachecker.persistence.dao.PersonDao;
import de.com.coronachecker.persistence.dao.ZipcodeDAO;
import de.com.coronachecker.persistence.entities.Person;

public class DataRepository {

    private PersonDao mPersonDao;
    private ZipcodeDAO mZipcodeDao;
    private LiveData<List<Person>> mAllPersons;

    public DataRepository(Application application) {
        PersonDatabase db = PersonDatabase.getDatabase(application);
        mPersonDao = db.personDao();
        mZipcodeDao = db.zipcodeDAO();
        mAllPersons = mPersonDao.getAll();
    }

    public LiveData<List<Person>> getAllPersons() {
        return mAllPersons;
    }

    public void insert(Person person) {
        PersonDatabase.databaseWriteExecutor.execute(() -> {
            mPersonDao.insert(person);
        });
    }

    public String getCountyByZipcode(String zipcode) {
        return mZipcodeDao.findCountyByZipcode(zipcode);
    }
}
