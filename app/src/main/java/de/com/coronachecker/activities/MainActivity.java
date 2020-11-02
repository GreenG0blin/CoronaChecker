package de.com.coronachecker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import de.com.coronachecker.R;
import de.com.coronachecker.services.PersonService;
import de.com.coronachecker.services.RefreshService;
import de.com.coronachecker.persistence.entities.Person;
import de.com.coronachecker.view.PersonListAdapter;
import de.com.coronachecker.view.PersonViewModel;
import lombok.SneakyThrows;

public class MainActivity extends AppCompatActivity {

    private PersonViewModel mPersonViewModel;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    public static final int NEW_PERSON_ACTIVITY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSwipeRefreshLayout = findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @SneakyThrows
            @Override
            public void onRefresh() {
                mPersonViewModel.refreshData();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

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

        Optional<Person> result = Optional.empty();

        if (requestCode == NEW_PERSON_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Objects.requireNonNull(data);
                result = mPersonViewModel.insertFromInput(data);
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }

        if(!result.isPresent()) {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.invalid_zipcode,
                    Toast.LENGTH_LONG).show();
        }
    }

}