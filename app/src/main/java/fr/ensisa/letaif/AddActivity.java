package fr.ensisa.letaif;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import java.util.Date;

public class AddActivity extends AppCompatActivity {
        private EditText poINameEditText;
        private EditText poICreatorEditText;
        private EditText poILongitudeFloat;
        private EditText poILatitudeFloat;
        private EditText poIDescriptionText;
        private RatingBar poIRate;

        private AddPoIViewModel addPoIViewModel;

        public static final int PICK_IMAGE = 1;

        public void onClickBtn(View v){
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
        }

        @Override
        protected void onCreate(Bundle saveInstanceState){
            super.onCreate(saveInstanceState);
            setContentView(R.layout.activity_add);

            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            poINameEditText = findViewById(R.id.poIName);
            poICreatorEditText = findViewById(R.id.poICreatorName);
            poILongitudeFloat = findViewById(R.id.longitude);
            poILatitudeFloat = findViewById(R.id.latitude);
            poIDescriptionText = findViewById(R.id.poIDescription);
            poIRate = (RatingBar) findViewById(R.id.poIRate);

            final Integer numbersOfPoI = getIntent().getIntExtra("poiItemCount",1);

            addPoIViewModel = ViewModelProviders.of(this).get(AddPoIViewModel.class);

            FloatingActionButton fab = findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (poINameEditText.getText() == null || poICreatorEditText.getText() == null || poILatitudeFloat.getText() == null || poILongitudeFloat.getText() == null)
                        Toast.makeText(AddActivity.this, "Missing fields", Toast.LENGTH_SHORT).show();
                    else {
                        addPoIViewModel.addPoI(new PoIModel(
                                numbersOfPoI+1,
                                poINameEditText.getText().toString(),
                                poICreatorEditText.getText().toString(),
                                Float.parseFloat(poILongitudeFloat.getText().toString()),
                                Float.parseFloat(poILatitudeFloat.getText().toString()),
                                poIDescriptionText.getText().toString(),
                                poIRate.getRating(),
                                new Date()
                        ));
                        finish();
                        Toast.makeText(AddActivity.this,"Successfully Added your PoI", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
}
