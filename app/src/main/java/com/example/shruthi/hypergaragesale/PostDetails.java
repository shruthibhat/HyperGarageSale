package com.example.shruthi.hypergaragesale;

import android.app.Fragment;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class PostDetails extends AppCompatActivity implements OnMapReadyCallback {

    private EditText postTitle;
    private EditText postPrice;
    private EditText postDesc;
    private ImageView postImage;
    private MapView mapView;
    GoogleMap map;

    private GoogleMap googleMap;
    private String pLocation="Sunnyvale,CA";
    private Double pLatitude=37.37;
    private Double pLongitude=-122.03;

    private SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_details);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        //This was added to change the status bar color ,if API is less than 21 it is default grey color

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        postTitle = (EditText) findViewById(R.id.postTitleText);
        postDesc = (EditText) findViewById(R.id.postDescText);
        postPrice = (EditText) findViewById(R.id.postPriceText);
        postImage =(ImageView) findViewById(R.id.postImgPreview);
        //The details of the particular item selected in the list which is sent along with the intent is extracted below
        Bundle bundle=getIntent().getExtras();
        postTitle.setText(bundle.getString("title"));
        postDesc.setText(bundle.getString("desc"));
        postPrice.setText(bundle.getString("price"));
        Uri path=Uri.parse(bundle.getString("image"));
        postImage.setImageURI(path);
        if(bundle.getString("location")!=null) {
            pLocation = bundle.getString("location");
            pLatitude = Double.parseDouble(bundle.getString("latitude"));
            pLongitude = Double.parseDouble(bundle.getString("longitude"));
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

//Added to support the back button on the toolbar
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        LatLng place=new LatLng(pLatitude,pLongitude);
        googleMap.addMarker(new MarkerOptions().position(place)
                .title(pLocation));
     //   googleMap.moveCamera(CameraUpdateFactory.newLatLng(place));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place,20.0f));
        googleMap.getUiSettings().setZoomControlsEnabled(true);
    }

}

