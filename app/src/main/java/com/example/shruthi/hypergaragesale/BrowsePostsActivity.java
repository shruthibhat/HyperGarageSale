package com.example.shruthi.hypergaragesale;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Movie;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;



import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static android.icu.text.UnicodeSet.CASE;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;
import static com.example.shruthi.hypergaragesale.R.id.fabBrowse;
import static com.example.shruthi.hypergaragesale.R.id.imgPreview;


public class BrowsePostsActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private PostsAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_posts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.browse_toolbar);
        setSupportActionBar(toolbar);
        //This was added to change the status bar color ,if API is less than 21 it is default grey color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        mRecyclerView = (RecyclerView) findViewById(R.id.posts_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
       


        PostsDbHelper mDbHelper = new PostsDbHelper(this);
        db = mDbHelper.getReadableDatabase();

        mAdapter = new PostsAdapter(getDataSet());
        mRecyclerView.setAdapter(mAdapter);


        //This was added to support the tapping of the item so that it can display all the details of that particular post
        mRecyclerView.addOnItemTouchListener(
                new RecyclerTouchListener(getApplicationContext(), new RecyclerTouchListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                       // startActivity(new Intent(getApplicationContext(), PostDetails.class));
                        //The details needed to be displayed on click of the item is sent along with intent so that it can be
                        //extracted by the PostDetails Activity
                        Intent postDetailsIntent=new Intent(getApplicationContext(),PostDetails.class);
                        postDetailsIntent.putExtra("image",getDataSet().get(position).getmImage());
                        postDetailsIntent.putExtra("title",getDataSet().get(position).getmTitle());
                        postDetailsIntent.putExtra("price",getDataSet().get(position).getmPrice());
                        postDetailsIntent.putExtra("desc",getDataSet().get(position).getmDesc());

                        if(getDataSet().get(position).getmLocation()!=null)
                        {
                            postDetailsIntent.putExtra("location",getDataSet().get(position).getmLocation());
                            postDetailsIntent.putExtra("latitude",getDataSet().get(position).getmLatitude());
                            postDetailsIntent.putExtra("longitude",getDataSet().get(position).getmLongitude());
                        }
                        startActivity(postDetailsIntent);

                    }
                })
        );

        FloatingActionButton fab = (FloatingActionButton) findViewById(fabBrowse);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });






    }
        @Override
        protected void onResume () {
            super.onResume();
            mAdapter.notifyDataSetChanged();
        }

    //COLUMN_NAME_IMAGE and COLUMN_NAME_DESCRIPTION was added so that it could be displayed in the PostDetails page
        private ArrayList<BrowsePosts> getDataSet () {

            String[] projection = {
                    Posts.PostEntry.COLUMN_NAME_TITLE,
                    Posts.PostEntry.COLUMN_NAME_PRICE,
                    Posts.PostEntry.COLUMN_NAME_IMAGE,
                    Posts.PostEntry.COLUMN_NAME_DESCRIPTION,
                    Posts.PostEntry.COLUMN_NAME_LATITUDE,
                    Posts.PostEntry.COLUMN_NAME_LONGITUDE,
                    Posts.PostEntry.COLUMN_NAME_LOCATION,

            };

            // How you want the results sorted in the resulting Cursor
            String sortOrder =
                    Posts.PostEntry.COLUMN_NAME_PRICE + " DESC";

            Cursor cursor = db.query(
                    Posts.PostEntry.TABLE_NAME,  // The table to query
                    projection,                               // The columns to return
                    null,                                     // The columns for the WHERE clause
                    null,                                     // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder                                 // The sort order
            );

//COLUMN_NAME_IMAGE and COLUMN_NAME_DESCRIPTION was added so that it could be displayed in the PostDetails page
            ArrayList<BrowsePosts> browsePosts = new ArrayList<BrowsePosts>();
            if (cursor.moveToFirst()) {
                do {
                    browsePosts.add(new BrowsePosts(
                            cursor.getString(cursor.getColumnIndex(Posts.PostEntry.COLUMN_NAME_TITLE)),
                            cursor.getString(cursor.getColumnIndex(Posts.PostEntry.COLUMN_NAME_PRICE)),
                            cursor.getString(cursor.getColumnIndex(Posts.PostEntry.COLUMN_NAME_IMAGE)),
                            cursor.getString(cursor.getColumnIndex(Posts.PostEntry.COLUMN_NAME_DESCRIPTION)),
                            cursor.getString(cursor.getColumnIndex(Posts.PostEntry.COLUMN_NAME_LATITUDE)),
                            cursor.getString(cursor.getColumnIndex(Posts.PostEntry.COLUMN_NAME_LONGITUDE)),
                            cursor.getString(cursor.getColumnIndex(Posts.PostEntry.COLUMN_NAME_LOCATION))
                            ));
                } while (cursor.moveToNext());
            }

            return browsePosts;
        }


        //This was created to support searchview in the tool bar.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.options_menu, menu);

        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setIconifiedByDefault(false);

       searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
           @Override
           public boolean onQueryTextSubmit(String query) {

               return false;
           }

           @Override
           public boolean onQueryTextChange(String newText) {

               mAdapter.getFilter().filter(newText);

               return true;
           }
       });


        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        return super.onOptionsItemSelected(item);
    }

}
