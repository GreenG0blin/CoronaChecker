package de.com.coronachecker.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

import de.com.coronachecker.R;

public class NewPersonActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY_NAME = "de.com.coronachecker.personsql.REPLY_NAME";
    public static final String EXTRA_REPLY_ZIPCODE = "de.com.coronachecker.personsql.REPLY_ZIPCODE";

    private TextInputLayout mEditPersonNameView;
    private TextInputLayout mEditPersonZipcodeView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_person);
        mEditPersonNameView = findViewById(R.id.edit_name);
        mEditPersonZipcodeView = findViewById(R.id.edit_plz);

        final Button button = findViewById(R.id.button_save);

        button.setOnClickListener(view -> {
            Intent replyIntent = new Intent();
            if (TextUtils.isEmpty(mEditPersonNameView.getEditText().getText()) || TextUtils.isEmpty(mEditPersonZipcodeView.getEditText().getText())) {
                setResult(RESULT_CANCELED, replyIntent);
            } else {
                String name = mEditPersonNameView.getEditText().getText().toString();
                String zipcode = mEditPersonZipcodeView.getEditText().getText().toString();

                replyIntent.putExtra(EXTRA_REPLY_NAME, name);
                replyIntent.putExtra(EXTRA_REPLY_ZIPCODE, zipcode);
                setResult(RESULT_OK, replyIntent);
            }
            finish();
        });
    }

}