package de.com.coronachecker.config.converter;

import androidx.room.TypeConverter;

import de.com.coronachecker.persistence.entities.Status;

public class StatusConverter {

    @TypeConverter
    public static String fromEnumToString(Status status) {
        switch (status) {
            case RED: return "RED";
            case YELLOW: return "YELLOW";
            default: return "GREEN";
        }
    }

    @TypeConverter
    public static Status fromStringToEnum(String string) {
        switch (string) {
            case "RED": return Status.RED;
            case "YELLOW": return Status.YELLOW;
            default: return Status.GREEN;
        }
    }
}
