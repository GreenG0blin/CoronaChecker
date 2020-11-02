package de.com.coronachecker.persistence.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import de.com.coronachecker.persistence.entities.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(tableName = "person_table")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Person {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "name")
    @NonNull
    public String name;

    @ColumnInfo(name = "status")
    @NonNull
    public Status status;

    @ColumnInfo(name = "7-days-incidence")
    @NonNull
    public float sevenDaysIncidence;

    @ColumnInfo(name = "county")
    @NonNull
    public String county;

    @ColumnInfo(name = "last_updated")
    @NonNull
    public String lastUpdated;
}
