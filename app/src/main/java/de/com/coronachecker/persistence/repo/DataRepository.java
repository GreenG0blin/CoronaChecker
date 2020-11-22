package de.com.coronachecker.persistence.repo;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.Optional;

import de.com.coronachecker.persistence.Database;
import de.com.coronachecker.persistence.dao.PersonDao;
import de.com.coronachecker.persistence.dao.ZipcodeDAO;
import de.com.coronachecker.persistence.entities.Person;

public class DataRepository {

    private final PersonDao mPersonDao;
    private final ZipcodeDAO mZipcodeDao;
    private final LiveData<List<Person>> mAllPersons;

    public DataRepository(Application application) {
        Database db = Database.getDatabase(application);
        mPersonDao = db.personDao();
        mZipcodeDao = db.zipcodeDAO();
        mAllPersons = mPersonDao.getAll();
    }

    public LiveData<List<Person>> getAllPersons() {
        return mAllPersons;
    }

    public void insert(Person person) {
        Database.databaseWriteExecutor.execute(() -> {
            mPersonDao.insert(person);
        });
    }

    public void update(Person person) {
        Database.databaseWriteExecutor.execute(() -> {
            mPersonDao.updatePerson(person);
        });
    }

    public Optional<String> getCountyByZipcode(String zipcode) {
        return mZipcodeDao.findCountyByZipcode(zipcode);
    }

    public void delete(Person person) {
        Database.databaseWriteExecutor.execute(() -> {
            mPersonDao.delete(person);
        });
    }
}
