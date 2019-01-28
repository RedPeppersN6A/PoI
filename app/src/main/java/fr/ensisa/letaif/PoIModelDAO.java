package fr.ensisa.letaif;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.Update;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
@TypeConverters(DateConverter.class)
public interface PoIModelDAO {

    @Query("select * from PoIModel")
    LiveData<List<PoIModel>> getAllPoIItems();


    @Query("select * from PoIModel where id = :id")
    PoIModel getPoIbyId(String id);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    void updatePoIModel(PoIModel poIModel);

    @Insert(onConflict = REPLACE)
    void addPoIModel(PoIModel poIModel);

    @Delete
    void deletePoIModel(PoIModel poIModel);

    @Query("delete from PoIModel")
    void deleteAllPoIModel();
}
