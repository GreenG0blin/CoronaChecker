package de.com.coronachecker.persistence;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.com.coronachecker.R;
import de.com.coronachecker.config.converter.StatusConverter;
import de.com.coronachecker.persistence.dao.PersonDao;
import de.com.coronachecker.persistence.dao.ZipcodeDAO;
import de.com.coronachecker.persistence.entities.Person;
import de.com.coronachecker.persistence.entities.Zipcode;
import lombok.SneakyThrows;

@Database(entities = {Person.class, Zipcode.class}, version = 11)
@TypeConverters({StatusConverter.class})
public abstract class PersonDatabase extends RoomDatabase {

    public abstract PersonDao personDao();
    public abstract ZipcodeDAO zipcodeDAO();

    private static volatile PersonDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;

    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static PersonDatabase getDatabase(final Context context) {

        if (INSTANCE == null) {

            synchronized (PersonDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            PersonDatabase.class, "CoronaChecker")
                            .addCallback(new RoomDatabase.Callback() {
                                @SneakyThrows
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    InputStream insertsStream = context.getResources().openRawResource(R.raw.zipcode);
                                    BufferedReader insertReader = new BufferedReader(new InputStreamReader(insertsStream));
                                    while (insertReader.ready()) {
                                        String insertStmt = insertReader.readLine();
                                        db.execSQL(insertStmt);
                                    }
                                }
                            })
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
