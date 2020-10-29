package de.com.coronachecker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

import de.com.coronachecker.business.PersonService;
import de.com.coronachecker.persistence.entities.Person;
import de.com.coronachecker.view.PersonListAdapter;
import de.com.coronachecker.view.PersonViewModel;
import lombok.SneakyThrows;

public class MainActivity extends AppCompatActivity {

    private PersonViewModel mPersonViewModel;
    public static final int NEW_PERSON_ACTIVITY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final PersonListAdapter adapter = new PersonListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mPersonViewModel = new ViewModelProvider(this).get(PersonViewModel.class);
        mPersonViewModel.getAllPersons().observe(this, adapter::setPersons);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, NewPersonActivity.class);
            startActivityForResult(intent, NEW_PERSON_ACTIVITY_REQUEST_CODE);
        });
    }

    @SneakyThrows
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_PERSON_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Objects.requireNonNull(data);
            PersonService personService = new PersonService(this.getApplication(), data.getStringExtra(NewPersonActivity.EXTRA_REPLY_ZIPCODE));

            Person person = personService.execute().get();
            person.setName(data.getStringExtra(NewPersonActivity.EXTRA_REPLY_NAME));

            mPersonViewModel.insert(person);

        } else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }

}