package com.example.shruthi.hypergaragesale;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;
import java.io.IOException;
import java.util.List;
import java.util.Locale;


import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
import static com.example.shruthi.hypergaragesale.R.id.MyCoordinatorLayout;



public class MainActivity extends AppCompatActivity implements LocationListener,TextToSpeech.OnInitListener {
    private CoordinatorLayout myCoordinatorLayout;
    private Button PostButton;
    private SQLiteDatabase db;
    private ContentValues values;

    private EditText titleText;
    private EditText descText;
    private EditText priceText;
    private TextView txtLat;



    private Uri photoURI;
    private Uri selectedImage;
    private String imageToSave;
    private ImageView imgPreview;
    private Button btnPicture;
    private Button btnGallary;
    private String userChoosenTask;
    private String price;

    static final int REQUEST_TAKE_PHOTO = 1;
    static final int PICK_IMAGE_REQUEST = 2;



    //Location

    protected LocationManager locationManager;
    private double latitude;
    private double longitude;
    private String place;


    //Text to speach

    TextToSpeech tts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myCoordinatorLayout = (CoordinatorLayout) findViewById(
                MyCoordinatorLayout);
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

        titleText = (EditText) findViewById(R.id.TitleText);
        descText = (EditText) findViewById(R.id.DescriptionText);
        priceText = (EditText) findViewById(R.id.PriceText);
        txtLat = (TextView) findViewById(R.id.test);

        tts=new TextToSpeech(this, this);

        // Gets the data repository in write mode
        PostsDbHelper mDbHelper = new PostsDbHelper(this);
        db = mDbHelper.getWritableDatabase();


        imgPreview=(ImageView) findViewById(R.id.imgPreview);
       // btnPicture=(Button) findViewById(R.id.camera);
        btnGallary=(Button)findViewById(R.id.pickPhoto);

         // Checking camera availability
        if (!isDeviceSupportCamera()) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! Your device doesn't support camera",
                    Toast.LENGTH_LONG).show();
            // will close the app if the device does't have camera
            finish();
        }

        //Check for permission for camera,read and write to external storage during runtime
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED  ) {

            btnGallary.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION }, 0);

        }


        btnGallary.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                getLocation();
                //To give an option to click picture or select from gallery
                selectImage();
            }
        });

       /* btnPicture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                captureImage();
            }
        });*/


    }


    void getLocation()
    {
        try {

            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);


        }
        catch(SecurityException e)
        {
            e.printStackTrace();
        }

    }

    private void showSnackBar(View v) {
        if (v == null) {
            Snackbar.make(findViewById(MyCoordinatorLayout), R.string.new_post_snackbar,
                    Snackbar.LENGTH_SHORT).show();
        } else {
            Snackbar.make(v, R.string.new_post_snackbar,
                    Snackbar.LENGTH_SHORT).show();
        }
    }

    private void addPost() {
        price="$"+priceText.getText().toString();


        if(place!=null){

            values = new ContentValues();
            values.put(Posts.PostEntry.COLUMN_NAME_TITLE, titleText.getText().toString());
            values.put(Posts.PostEntry.COLUMN_NAME_DESCRIPTION, descText.getText().toString());
            values.put(Posts.PostEntry.COLUMN_NAME_PRICE, price);
            values.put(Posts.PostEntry.COLUMN_NAME_IMAGE, imageToSave);
            values.put(Posts.PostEntry.COLUMN_NAME_LATITUDE,Double.toString(latitude));
            values.put(Posts.PostEntry.COLUMN_NAME_LONGITUDE,Double.toString(longitude));
            values.put(Posts.PostEntry.COLUMN_NAME_LOCATION,place);
        }
        else{
            values = new ContentValues();
            values.put(Posts.PostEntry.COLUMN_NAME_TITLE, titleText.getText().toString());
            values.put(Posts.PostEntry.COLUMN_NAME_DESCRIPTION, descText.getText().toString());
            values.put(Posts.PostEntry.COLUMN_NAME_PRICE, price);
            values.put(Posts.PostEntry.COLUMN_NAME_IMAGE, imageToSave);
        }



            // Insert the new row, returning the primary key value of the new row
            long newRowId;
            newRowId = db.insert(
                    Posts.PostEntry.TABLE_NAME,
                    null,
                    values);



               String toSpeak="Thank you for posting ad for " +titleText.getText().toString();

              speak(toSpeak);
            // Done adding new entry into database, navigate user back to browsing screen
            startActivity(new Intent(this, BrowsePostsActivity.class));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_actions, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        EditText descText = (EditText) findViewById(R.id.DescriptionText);
        EditText priceText = (EditText) findViewById(R.id.PriceText);
        EditText titleText = (EditText) findViewById(R.id.TitleText);
        switch (item.getItemId()) {
            case R.id.add: {
                String postTitle = titleText.getText().toString();
                String postPrice = priceText.getText().toString();
                String postdesc = descText.getText().toString();
                // Create a new map of values, where column names are the keys
                if (postTitle.matches("") || postPrice.matches("") || postdesc.matches("") || imageToSave==null)
                {
                    Toast.makeText(getApplicationContext(),
                            "All fields and photo is required!", Toast.LENGTH_SHORT)
                            .show();
                }
                else {
                    showSnackBar(null);
                    addPost();
                }
            }
            case R.id.reset: {
                descText.setText("");
                priceText.setText("");
                titleText.setText("");
                imgPreview.setImageResource(0);
                imageToSave=null;

            }

        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Checking device has camera hardware or not
     * */
    private boolean isDeviceSupportCamera() {
        if (getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

//Method to capture the image
    private void captureImage()
    {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            //Using content resolver to  store the image in the media and photouri gives the uri of the image
            photoURI=getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,new ContentValues());
            //photoURI = FileProvider.getUriForFile(getApplicationContext(), "com.example.shruthi.myapplication.fileprovider", photoFile);
            //setResult(Activity.RESULT_OK,takePictureIntent);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);

        }

    }

//method to select picture from gallery
    private void pickFromGallary()
    {
        Intent pickIntent= new Intent();
      //  pickIntent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
     //   pickIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        // Show only images, no videos or anything else
        pickIntent.setType("image/*");
        pickIntent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        pickIntent.setFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        pickIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        // Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(pickIntent, "Select Picture"), PICK_IMAGE_REQUEST);
    }


    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take Photo")) {
                    userChoosenTask ="Take Photo";

                        captureImage();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask ="Choose from Library";

                        pickFromGallary();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // if the result is capturing Image
        if (requestCode == REQUEST_TAKE_PHOTO) {

            if (resultCode == RESULT_OK ) {

               /* Toast.makeText(getApplicationContext(),
                        "Image Saved!", Toast.LENGTH_SHORT)
                        .show();*/
                //Using content resolver to get the image that is stored in the phone
                String[] fileColumn = { MediaStore.Images.Media.DATA };
                Cursor cursor = getContentResolver().query(photoURI,
                        fileColumn, null, null, null);
                String contentPath = null;
                if (cursor.moveToFirst()) {
                    contentPath = cursor.getString(cursor
                            .getColumnIndex(fileColumn[0]));

                    Bitmap bmp = BitmapFactory.decodeFile(contentPath);
                    ImageView img = (ImageView) findViewById(R.id.imgPreview);
                    img.setImageBitmap(bmp);
                    //this variable will help in storing the path of the image to the sqlite database
                    imageToSave=photoURI.toString();
                }

            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        }
        //if the the photo to be added is from gallery
        else if(requestCode == PICK_IMAGE_REQUEST)
        {
            if(resultCode==RESULT_OK)
            {
                //This variable gives the photo that is selected in the gallery
                selectedImage = data.getData();
                this.getContentResolver().takePersistableUriPermission(selectedImage, Intent.FLAG_GRANT_READ_URI_PERMISSION);
              //  int takeFlags = data.getFlags();
             //   takeFlags &= (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
             //   ContentResolver resolver = this.getContentResolver();
             //   resolver.takePersistableUriPermission(selectedImage,takeFlags);




                /*               String[] filePathColumn = { MediaStore.Images.Media.DATA };
                System.out.println("filepathColumn"+filePathColumn.toString());
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();
                imgPreview.setImageBitmap(BitmapFactory.decodeFile(picturePath));*/

                try {


                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    imgPreview.setImageBitmap(bitmap);
                    //this variable helps to store the path to the sqlite database(notice the uri is converted to string before storing)
                    //since the path is stored as string and not absolute path there could be chance the image may not be found based on this
                    //from the memory when the external storage is ejected and then mounted(not in nexus but other device)---added by shruthi
                    imageToSave=selectedImage.toString();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            else if(requestCode==RESULT_CANCELED)
            {
                Toast.makeText(getApplicationContext(),
                        "User cancelled image selection", Toast.LENGTH_SHORT)
                        .show();
            }
            else
            {
                Toast.makeText(getApplicationContext(),
                        "Failure to select a picture!", Toast.LENGTH_SHORT)
                        .show();
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(),
                    "Please Try again!", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    //when the option for runtime permission is selected it will be redirected here
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                btnGallary.setEnabled(true);
            }
        }

    }

    public void onLocationChanged (Location location) {

        txtLat = (TextView) findViewById(R.id.test);
       // txtLat.setText("Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());

        latitude = location.getLatitude();
        longitude = location.getLongitude();

        List<Address> addresses = null;
        String address="",city="",state="",postalCode="";
        String fullAddress="";
        Geocoder gc = new Geocoder(this, Locale.getDefault());
        try {
            addresses = gc.getFromLocation(latitude, longitude, 1);
            address = addresses.get(0).getAddressLine(0);
            // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            city = addresses.get(0).getLocality();
            state = addresses.get(0).getAdminArea();
            postalCode = addresses.get(0).getPostalCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
        fullAddress=address+","+city+","+state+","+postalCode;
        txtLat.setText(fullAddress);
        place=fullAddress;
    }

    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
    }


    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }


    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
    }

    @Override
    public void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    private void speak(String text){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }else{
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }


   /* private void listen(){
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something");

        try {
            startActivityForResult(i, 100);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(MainActivity.this, "Your device doesn't support Speech Recognition", Toast.LENGTH_SHORT).show();
        }
    }*/



    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }
    }
}