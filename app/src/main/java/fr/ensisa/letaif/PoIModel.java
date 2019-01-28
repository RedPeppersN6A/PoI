package fr.ensisa.letaif;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.media.Image;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.Date;

@Entity
public class PoIModel implements Parcelable {

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public PoIModel createFromParcel(Parcel in) {
            return new PoIModel(in);
        }

        public PoIModel[] newArray(int size) {
            return new PoIModel[size];
        }
    };


    @PrimaryKey(autoGenerate = true)
    public int id;
    private String poIName;
    private String poICreatorName;
    private float longitude;
    private float latitude;
    //private Image[] poiimages;
    private String description;
    private float note;
    @TypeConverters(DateConverter.class)
    private Date poICreationDate;

    public PoIModel(int id, @NonNull String poIName, @NonNull String poICreatorName, float longitude, float latitude, /*Image[] poiImages,*/ String description, float note, @NonNull Date poICreationDate) {
        this.id = id;
        this.poIName = poIName;
        this.poICreatorName = poICreatorName;
        this.longitude = longitude;
        this.latitude = latitude;
        //this.poiimages = poiImages;
        this.description = description;
        this.note = note;
        this.poICreationDate = poICreationDate;
    }

    public String getPoIName() {
        return poIName;
    }

    public void setPoIName(String poIName) {
        this.poIName = poIName;
    }

    public String getPoICreatorName() {
        return poICreatorName;
    }

    public void setPoICreatorName(String poICreatorName) {
        this.poICreatorName = poICreatorName;
    }

    public float getLongitude(){
        return this.longitude;
    }
    public float getLatitude(){
        return this.latitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }
/*public Image[] getPoiimages() {
        return poiimages;
    }

    public void setPoiimages(Image[] poiimages) {
        this.poiimages = poiimages;
    }*/

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getNote() {
        return note;
    }

    public void setNote(int note) {
        this.note = note;
    }

    public Date getPoICreationDate() {
        return poICreationDate;
    }

    public void setPoICreationDate(Date poICreationDate) {
        this.poICreationDate = poICreationDate;
    }

    public PoIModel(Parcel in){
        this.poIName = in.readString();
        this.poICreatorName = in.readString();
        this.longitude = in.readFloat();
        this.latitude = in.readFloat();
        //this.poiimages = poiImages;
        this.description = in.readString();
        this.note = in.readFloat();
        this.poICreationDate = new Date(in.readLong());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.poIName);
        dest.writeString(this.poICreatorName);
        dest.writeFloat(this.longitude);
        dest.writeFloat(this.latitude);
        dest.writeString(this.description);
        dest.writeFloat(note);
        dest.writeString(poICreationDate.toString());
    }
}
