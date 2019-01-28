package fr.ensisa.letaif;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ViewActivity extends AppCompatActivity {

    LocationManager locationManager = null;
    private int etat;
    private String fournisseur;
    private TextView poiName;
    private TextView poiDescription;

    private MapView myOpenMapView;
    RotationGestureOverlay mRotationGestureOverlay;

    Geocoder geocoder;

    LocationListener ecouteurGPS = new LocationListener() {
        @Override
        public void onLocationChanged(Location localisation) {
            Toast.makeText(getApplicationContext(), fournisseur + " localisation", Toast.LENGTH_SHORT).show();
            String coordonnees = String.format("Latitude : %f - Longitude : %f\n", localisation.getLatitude(), localisation.getLongitude());
            String autres = String.format("Vitesse : %f - Altitude : %f - Cap : %f\n", localisation.getSpeed(), localisation.getAltitude(), localisation.getBearing());
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = new Date(localisation.getTime());

            String strLatitude = String.format("Latitude : %f", localisation.getLatitude());
            String strLongitude = String.format("Longitude : %f", localisation.getLongitude());

            myOpenMapView.getController().setCenter(new GeoPoint(localisation.getLatitude(), localisation.getLongitude()));
            myOpenMapView.setMapOrientation(localisation.getBearing());

            Marker tec = new Marker(myOpenMapView);
            tec.setPosition(new GeoPoint(localisation.getLatitude(), localisation.getLongitude()));
            tec.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            tec.setIcon(getResources().getDrawable(R.drawable.ic_marker_icon));
            tec.setTitle("TEC");
            myOpenMapView.getOverlays().add(tec);

            myOpenMapView.invalidate();

            List<Address> adresses = null;
            try
            {
                adresses = geocoder.getFromLocation(localisation.getLatitude(), localisation.getLongitude(), 1);
            }
            catch (IOException ioException)
            {
            } catch (IllegalArgumentException illegalArgumentException)
            {
            }

            if (adresses == null || adresses.size()  == 0)
            {
            }
            else
            {
                Address adresse = adresses.get(0);
                ArrayList<String> addressFragments = new ArrayList<>();

                for(int i = 0; i <= adresse.getMaxAddressLineIndex(); i++)
                {
                    addressFragments.add(adresse.getAddressLine(i));
                }
            }
        }

        @Override
        public void onProviderDisabled(String fournisseur) {
        }

        @Override
        public void onProviderEnabled(String fournisseur) {
        }

        @Override
        public void onStatusChanged(String fournisseur, int status, Bundle extras) {
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        Bundle data = getIntent().getExtras();
        final PoIModel poiModel = data.getParcelable("poiModel");

        etat = 0;
        poiName = (TextView) findViewById(R.id.tvPoIName);
        poiDescription = (TextView) findViewById(R.id.tvPoIDescription);
        geocoder = new Geocoder(ViewActivity.this,Locale.getDefault());

        poiName.setText(poiModel.getPoIName());
        poiDescription.setText(poiModel.getDescription());

        myOpenMapView = (MapView)findViewById(R.id.mapview);
        myOpenMapView.setClickable(true);
        myOpenMapView.getController().setZoom(18.0);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        myOpenMapView.setBottom(height);
        myOpenMapView.setRight(width);
        myOpenMapView.setTileSource(TileSourceFactory.MAPNIK);
        MapController mapController = (MapController) this.myOpenMapView.getController();
        mapController.setZoom(8);
        GeoPoint customMarker = new GeoPoint(poiModel.getLatitude(), poiModel.getLongitude());
        Marker tec = new Marker(myOpenMapView);
        tec.setPosition(new GeoPoint(customMarker.getLatitude(), customMarker.getLongitude()));
        tec.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        tec.setIcon(getResources().getDrawable(R.drawable.ic_marker_icon));
        tec.setTitle("TEC");
        myOpenMapView.getOverlays().add(tec);

        myOpenMapView.invalidate();
        mapController.animateTo(customMarker);

        if (locationManager == null)
        {
            locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            Criteria criteres = new Criteria();

            // la précision  : (ACCURACY_FINE pour une haute précision ou ACCURACY_COARSE pour une moins bonne précision)
            criteres.setAccuracy(Criteria.ACCURACY_FINE);

            // l'altitude
            criteres.setAltitudeRequired(true);

            // la direction
            criteres.setBearingRequired(true);

            // la vitesse
            criteres.setSpeedRequired(true);

            // la consommation d'énergie demandée
            criteres.setCostAllowed(true);
            criteres.setPowerRequirement(Criteria.POWER_MEDIUM);

            fournisseur = locationManager.getBestProvider(criteres, true);
            Log.d("GPS", "fournisseur : " + fournisseur);
        }

        if (fournisseur != null)
        {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                return;
            }

            Location localisation = locationManager.getLastKnownLocation(fournisseur);
            if(localisation != null)
            {
                ecouteurGPS.onLocationChanged(localisation);
            }
            locationManager.requestLocationUpdates(fournisseur, 15000, 10, ecouteurGPS);
        }


        mRotationGestureOverlay = new RotationGestureOverlay(myOpenMapView);
        mRotationGestureOverlay.setEnabled(true);
        myOpenMapView.setMultiTouchControls(true);
        myOpenMapView.getOverlays().add(this.mRotationGestureOverlay);
    }
}
