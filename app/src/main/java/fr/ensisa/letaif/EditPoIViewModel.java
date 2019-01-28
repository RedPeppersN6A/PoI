package fr.ensisa.letaif;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.os.AsyncTask;

public class EditPoIViewModel extends AndroidViewModel {

    private AppDatabase appDatabase;

    public EditPoIViewModel (Application application){
        super(application);
        appDatabase = AppDatabase.getDatabase(this.getApplication());
    }

    public  void editPoI(final PoIModel poIModel){
        new EditPoIViewModel.editAsyncTask(appDatabase).execute(poIModel);
    }
    private static class editAsyncTask extends AsyncTask<PoIModel, Void, Void> {
        private AppDatabase db;

        editAsyncTask(AppDatabase appDatabase){
            db = appDatabase;
        }

        @Override
        protected Void doInBackground(final PoIModel... params){
            db.poiModel().updatePoIModel((params[0]));
            return null;
        }
    }
}
