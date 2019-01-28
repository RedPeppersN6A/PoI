package fr.ensisa.letaif;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnLongClickListener, View.OnClickListener{

    private PoIListViewModel viewModel;
    private RecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.rvPoI);
        recyclerViewAdapter = new RecyclerViewAdapter(new ArrayList<PoIModel>(), this,this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerViewAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        viewModel = ViewModelProviders.of(this).get(PoIListViewModel.class);

        viewModel.getPoIList().observe(MainActivity.this, new Observer<List<PoIModel>>(){
            @Override
            public void onChanged(@Nullable List<PoIModel> poIItem) {
                recyclerViewAdapter.addPoI(poIItem);
            }
        });

        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this) {
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                PoIModel poIModel = recyclerViewAdapter.getPoIModelAtPosition(position);
                viewModel.deleteItem(poIModel);
                Toast.makeText(MainActivity.this, "Successfully deleted a PoI", Toast.LENGTH_LONG).show();
            }
        };

        ItemTouchHelper itemTouchhelperDelete = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelperDelete.attachToRecyclerView(recyclerView);

        SwipeToSendEmailCallback swipeToSendEmailCallback = new SwipeToSendEmailCallback(this) {
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                PoIModel poIModel = recyclerViewAdapter.getPoIModelAtPosition(position);
                sendEmail(poIModel);
                Toast.makeText(MainActivity.this, "Successfully send a PoI by email", Toast.LENGTH_LONG).show();
            }
        };
        ItemTouchHelper itemTouchhelperSend = new ItemTouchHelper(swipeToSendEmailCallback);
        itemTouchhelperSend.attachToRecyclerView(recyclerView);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                intent.putExtra("poiItemCount",recyclerViewAdapter.getItemCount());
                startActivity(intent);
            }
        });
    }

    private void sendEmail(PoIModel poiModel) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");

        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "new Point of Interest to Visit");
        emailIntent.putExtra(Intent.EXTRA_TEXT, poiModel.getPoIName()+"\n Longitude : L "+poiModel.getLongitude()+"\n Latitude : l "+poiModel.getLatitude()+"\n Description : "+poiModel.getDescription());

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this,
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            viewModel.deleteAllItems();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onLongClick(View v) {
        /*PoIModel poIModel = (PoIModel) v.getTag();
        viewModel.deleteItem(poIModel);
        Toast.makeText(this,"Succesfully Deleted PoI", Toast.LENGTH_SHORT).show();
        return true;*/

        PoIModel poIModel = (PoIModel) v.getTag();
        Intent intent = new Intent(MainActivity.this, EditActivity.class);
        intent.putExtra("poiModel",poIModel);
        intent.putExtra("poiID",poIModel.id);
        startActivity(intent);
        return true;

    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {super.onActivityResult(requestCode, resultCode, data);}

    @Override
    public void onClick(View v){
        PoIModel poIModel = (PoIModel) v.getTag();
        Intent intent = new Intent(MainActivity.this, ViewActivity.class);
        intent.putExtra("poiModel",poIModel);
        startActivity(intent);
    }

}
