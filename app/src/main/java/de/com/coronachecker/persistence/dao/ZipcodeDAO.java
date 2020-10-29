package de.com.coronachecker.persistence.dao;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import de.com.coronachecker.persistence.entities.Zipcode;

@Dao
public interface ZipcodeDAO {

    @Query("DELETE FROM zipcode_table")
    void deleteAll();

    @Query("SELECT county FROM zipcode_table WHERE zipcode = :zipcode")
    String findCountyByZipcode(String zipcode);

    @Query("SELECT * FROM zipcode_table")
    List<Zipcode> findAll();
}
