package fr.ensisa.letaif;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.os.AsyncTask;

public class AddPoIViewModel extends AndroidViewModel {

    private AppDatabase appDatabase;

    public AddPoIViewModel (Application application){
        super(application);
        appDatabase = AppDatabase.getDatabase(this.getApplication());
    }

    public  void addPoI(final PoIModel poIModel){
        new addAsyncTask(appDatabase).execute(poIModel);
    }
    private static class addAsyncTask extends AsyncTask<PoIModel, Void, Void>{
        private AppDatabase db;

        addAsyncTask(AppDatabase appDatabase){
            db = appDatabase;
        }

        @Override
        protected Void doInBackground(final PoIModel... params){
            db.poiModel().addPoIModel(params[0]);
            return null;
        }
    }
}
