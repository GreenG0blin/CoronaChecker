package de.com.coronachecker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class NewPersonActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY_NAME = "de.com.coronachecker.personsql.REPLY_NAME";
    public static final String EXTRA_REPLY_ZIPCODE = "de.com.coronachecker.personsql.REPLY_ZIPCODE";

    private EditText mEditPersonNameView;
    private EditText mEditPersonZipcodeView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_person);
        mEditPersonNameView = findViewById(R.id.edit_name);
        mEditPersonZipcodeView = findViewById(R.id.edit_plz);

        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(view -> {
            Intent replyIntent = new Intent();
            if (TextUtils.isEmpty(mEditPersonNameView.getText()) || TextUtils.isEmpty(mEditPersonZipcodeView.getText())) {
                setResult(RESULT_CANCELED, replyIntent);
            } else {
                String name = mEditPersonNameView.getText().toString();
                String zipcode = mEditPersonZipcodeView.getText().toString();

                replyIntent.putExtra(EXTRA_REPLY_NAME, name);
                replyIntent.putExtra(EXTRA_REPLY_ZIPCODE, zipcode);
                setResult(RESULT_OK, replyIntent);
            }
            finish();
        });
    }

}