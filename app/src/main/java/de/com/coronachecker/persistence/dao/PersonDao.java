package de.com.coronachecker.persistence.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import de.com.coronachecker.persistence.entities.Person;

@Dao
public interface PersonDao {

    @Query("SELECT * FROM person_table")
    LiveData<List<Person>> getAll();

    @Insert
    void insert(Person person);

    @Update
    void updatePerson(Person... person);

    @Delete
    void delete(Person... persons);
}
