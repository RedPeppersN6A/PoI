package fr.ensisa.letaif;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import java.util.Date;

public class EditActivity extends AppCompatActivity {

    private EditText poINameEditText;
    private EditText poICreatorEditText;
    private EditText poILongitudeFloat;
    private EditText poILatitudeFloat;
    private EditText poIDescriptionText;
    private RatingBar poIRate;

    private EditPoIViewModel editPoIViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        poINameEditText = findViewById(R.id.poIName);
        poICreatorEditText = findViewById(R.id.poICreatorName);
        poILongitudeFloat = findViewById(R.id.longitude);
        poILatitudeFloat = findViewById(R.id.latitude);
        poIDescriptionText = findViewById(R.id.poIDescription);
        poIRate = (RatingBar) findViewById(R.id.poIRate);

        Bundle data = getIntent().getExtras();
        final PoIModel poiModel = data.getParcelable("poiModel");
        final Integer poiID = data.getInt("poiID");

        poINameEditText.setText(poiModel.getPoIName());
        poICreatorEditText.setText(poiModel.getPoICreatorName());
        poILongitudeFloat.setText(String.valueOf(poiModel.getLongitude()));
        poILatitudeFloat.setText(String.valueOf(poiModel.getLatitude()));
        poIDescriptionText.setText(poiModel.getDescription());
        poIRate.setRating(poiModel.getNote());

        editPoIViewModel = ViewModelProviders.of(this).get(EditPoIViewModel.class);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (poINameEditText.getText() == null)
                    Toast.makeText(EditActivity.this, "Missing fields", Toast.LENGTH_SHORT).show();
                else {
                    editPoIViewModel.editPoI(
                            new PoIModel(
                                    poiID,
                            poINameEditText.getText().toString(),
                            poICreatorEditText.getText().toString(),
                            Float.parseFloat(poILongitudeFloat.getText().toString()),
                            Float.parseFloat(poILatitudeFloat.getText().toString()),
                            poIDescriptionText.getText().toString(),
                            poIRate.getRating(),
                            new Date()
                            ));
                    finish();
                    Toast.makeText(EditActivity.this,"Successfully Edit", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
