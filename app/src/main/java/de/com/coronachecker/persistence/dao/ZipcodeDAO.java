package de.com.coronachecker.persistence.dao;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;
import java.util.Optional;

import de.com.coronachecker.persistence.entities.Zipcode;

@Dao
public interface ZipcodeDAO {

    @Query("SELECT county FROM zipcode_table WHERE zipcode = :zipcode")
    Optional<String> findCountyByZipcode(String zipcode);
}
