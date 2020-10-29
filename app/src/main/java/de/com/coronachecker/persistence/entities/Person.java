package de.com.coronachecker.persistence.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Getter;
import lombok.Setter;

@Entity(tableName = "person_table")
@Getter
@Setter
public class Person {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "name")
    @NonNull
    public String name;

    @ColumnInfo(name = "status")
    public Status status;

    @ColumnInfo(name = "7-days-incidence")
    public Float sevenDaysIncidence;

    @ColumnInfo(name = "county")
    public String county;
}
