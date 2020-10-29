package de.com.coronachecker.persistence.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Getter;
import lombok.Setter;

@Entity(tableName = "zipcode_table")
@Getter
@Setter
public class Zipcode {

    @PrimaryKey
    public int id;

    @ColumnInfo(name = "zipcode")
    @NonNull
    public String zipcode;

    @ColumnInfo(name = "county")
    @NonNull
    public String county;
}