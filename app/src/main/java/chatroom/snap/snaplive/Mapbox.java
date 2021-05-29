package chatroom.snap.snaplive;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.MapboxMapOptions;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.util.HashMap;

import chatroom.snap.snaplive.global.AppBack;
import chatroom.snap.snaplive.global.Global;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textAnchor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textField;

public class Mapbox extends AppCompatActivity {


    private GoogleMap mMap;

    //data
    String fromS, avaS, nameS, MidS, lat, lng;
    //firebase
    FirebaseAuth mAuth;
    MapView mapView;
    DatabaseReference mData;
String SOURCE_ID = "SOURCE_ID",LAYER_ID="LAYER_ID",ICON_ID="ICON_ID";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Global.currentactivity = this;
        mAuth = FirebaseAuth.getInstance();
        com.mapbox.mapboxsdk.Mapbox.getInstance(this,getString(R.string.mapbox_access_token));
        mData = FirebaseDatabase.getInstance().getReference(Global.USERS);
        if (getIntent() != null) {
            Intent intent = getIntent();
            lat = intent.getExtras().getString("lat");
            lng = intent.getExtras().getString("lng");
            fromS = intent.getExtras().getString("from");
            MidS = intent.getExtras().getString("Mid");
            avaS = intent.getExtras().getString("ava");
            nameS = intent.getExtras().getString("name");

            MapboxMapOptions options = MapboxMapOptions.createFromAttributes(this,null);
            //Setting cam postion
            com.mapbox.mapboxsdk.camera.CameraPosition cameraPosition = new com.mapbox.mapboxsdk.camera.CameraPosition.Builder()
                    .target(new com.mapbox.mapboxsdk.geometry.LatLng(Double.parseDouble(lat), Double.parseDouble(lng)))
                    .zoom(15)
                    .build();
            options.camera(cameraPosition);
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            mapView = new MapView(this,options);
            mapView.onCreate(savedInstanceState);
            Feature feature = Feature.fromGeometry(Point.fromLngLat(Double.parseDouble(lng),Double.parseDouble(lat)));

            mapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(@NonNull MapboxMap mapboxMap) {

                    mapboxMap.setStyle(new Style.Builder()
                            .fromUri(Style.MAPBOX_STREETS)
                                    .withImage(ICON_ID, BitmapFactory.decodeResource(
                                            Mapbox.this.getResources(), R.drawable.mapbox_marker_icon_default))
                                    .withSource(new GeoJsonSource(SOURCE_ID,feature
                                            )).withLayer(new SymbolLayer(LAYER_ID, SOURCE_ID)
                                    .withProperties(
                                            iconImage(ICON_ID),
                                            iconAllowOverlap(true),
                                            iconIgnorePlacement(true)
                                            ,textField(nameS)
                                    )

                            ),new Style.OnStyleLoaded() {
                        @Override
                        public void onStyleLoaded(@NonNull Style style) {

                            // Map is set up and the style has loaded. Now you can add data or make other map adjustments
                        }
                    });


                }
            });
            setContentView(mapView);


        }else{

            setContentView(R.layout.activity_map);

        }


    }
//
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
//        mMap.addMarker(new MarkerOptions().position(sydney).title(nameS));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//        CameraPosition cameraPosition = new CameraPosition.Builder()
//                .target(sydney)
//                .zoom(17).build();
//        //Zoom in and animate the camera.
//        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//    }


    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        Global.currentactivity = this;
        AppBack myApp = (AppBack) this.getApplication();
        if (myApp.wasInBackground) {
            //init data
            java.util.Map<String, Object> map = new HashMap<>();
            map.put(Global.Online, true);
            if(mAuth.getCurrentUser() != null)
                mData.child(mAuth.getCurrentUser().getUid()).updateChildren(map);
            Global.local_on = true;
            //lock screen
            ((AppBack) getApplication()).lockscreen(((AppBack) getApplication()).shared().getBoolean("lock", false));
        }

        myApp.stopActivityTransitionTimer();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        ((AppBack) this.getApplication()).startActivityTransitionTimer();
        Global.currentactivity = null;
    }

}
