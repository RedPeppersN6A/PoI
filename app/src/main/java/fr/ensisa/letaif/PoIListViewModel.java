package fr.ensisa.letaif;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.util.List;

public class PoIListViewModel extends AndroidViewModel {

    private final LiveData<List<PoIModel>> poIList;

    private AppDatabase appDatabase;

    public PoIListViewModel(@NonNull Application application) {
        super(application);

        appDatabase = AppDatabase.getDatabase(this.getApplication());
        poIList = appDatabase.poiModel().getAllPoIItems();
    }

    public LiveData<List<PoIModel>> getPoIList(){
        return poIList;
    }

    public void deleteItem(PoIModel poIModel){ new deleteAsyncTask(appDatabase).execute(poIModel); }

    public void deleteAllItems() { new allDeleteAsyncTask(appDatabase).execute(); }

    private static class deleteAsyncTask extends AsyncTask<PoIModel, Void, Void> {

        private AppDatabase db;

        deleteAsyncTask(AppDatabase appDatabase) {
            db = appDatabase;
        }

        @Override
        protected Void doInBackground(final PoIModel... params) {
            db.poiModel().deletePoIModel(params[0]);
            return null;
        }

    }

    private static class allDeleteAsyncTask extends  AsyncTask<PoIModel, Void, Void> {

        private AppDatabase db;

        public allDeleteAsyncTask(AppDatabase appDatabase) { db=appDatabase; }

        @Override
        protected Void doInBackground(PoIModel... poIModels) {
            db.poiModel().deleteAllPoIModel();
            return null;
        }
    }
}
