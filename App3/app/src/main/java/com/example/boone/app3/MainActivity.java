package com.example.boone.app3;


import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.support.v4.app.ActivityCompat;
import android.widget.TextView;
import android.widget.Toast;

import com.mindorks.paracamera.Camera;
import com.tylersuehr.chips.ChipsInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {

//Variables
    //Scroll
    private int position = 0;
    private int max;
    private ArrayList<View> scrolls = new ArrayList<>();
    Button button_next, button_previous;
    private String[] scrolls_title_array = {"Add Product Images","Suggested Prices","Sell The Item"};

    //Recycle view
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();
    private Camera camera;

    // Variables to test list item screen !!!
    private Button listItemBtn;

    long mImageTakenTime;
    private boolean logo;
    private Clarifia clarifia;
    //Full screen image
    private ImageView imageView;
    public  ArrayList<String> bitmap_array = new ArrayList<>();
    private LinearLayout linearLayout;

    //Other
    private int n_o_images = 0;
    private int FULL_SCREEN_RESULT=200;
    private LinearLayout.LayoutParams lp;
    private LinearLayout parent, bottom;
    private Ebay_Client_Credential_API ebay_client_credential_api = new Ebay_Client_Credential_API();

    public OkHttpClient client = new OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build();

    //Function
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Calling activity_main.xml file (not calling any other xml file)
        setContentView(R.layout.activity_main);

        Log.d("Testing_","start");

        //this.setTitle("Hello");

        clarifia = new Clarifia(this);

        ChipsInputLayout chipsInputLayout = findViewById(R.id.chips_input);
        chipsInputLayout.setMaxHeight(350);

        scrolls.add(findViewById(R.id.CameraScroll));
        scrolls.add(findViewById(R.id.OnlineItemsScroll));
        scrolls.add(findViewById(R.id.RetailPlatformScroll));


        // Code to test list item screen START !!!
        listItemBtn = findViewById(R.id.listItemBtnMain);
        listItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToListItemActivity();
            }
        });

        // END !!!

        ImageScrollSetup();
        RecyclerViewSetup();

        SetupScroll();
        SetScrollView();


//        *******************************************************************
//        **************************** API CALLS ****************************

        // Create a new object from HttpLoggingInterceptor
//        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//
//        // Add Interceptor to HttpClient
//        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
//
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://10.0.2.2:8000/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .client(client) // Set HttpClient to be used by Retrofit
//                .build();
//
//        BackendDatabaseApi dbApi = retrofit.create(BackendDatabaseApi.class);
//
//        String userName = "admin";
//        String password = "CircEx";
//
//        String base = userName + ":" + password;
//
//        String authHeader = "Basic " + Base64.encodeToString(base.getBytes(), Base64.NO_WRAP);
//
//        Call<List<Book>> getBooks = dbApi.getBooks(authHeader);


//        *******************************************************************
//        ****************** CODE TO RETRIEVE BOOKS *************************
//
//        getBooks.enqueue(new Callback<List<Book>>() {
//            @Override
//            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
//                if(!response.isSuccessful()){
//                    Log.d("ERROR CODE:- ", "" + response.code());
//                    return;
//                }
//
//                List<Book> bookList = response.body();
//
//                Log.d("***** BOOK LIST ******", "");
//                for(Book book: bookList){
//                    Log.d("Book:- ", book.getTitle());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Book>> call, Throwable t) {
//                Log.d("ERROR:- ", t.getMessage());
//            }
//        });


//        *******************************************************************
//        ********************* CODE TO ADD BOOK ****************************
//
//        Book book = new Book(2);
//        Call<Book> addBook = dbApi.addBook(authHeader, book);
//
//        addBook.enqueue(new Callback<Book>() {
//            @Override
//            public void onResponse(Call<Book> call, Response<Book> response) {
//                if(!response.isSuccessful()){
//                    Log.d("ERROR CODE:- ", "" + response.code());
//                    return;
//                }
//
//                Log.d("SUCCESS", "Book added successfully!!!");
//            }
//
//            @Override
//            public void onFailure(Call<Book> call, Throwable t) {
//                Log.d("ERROR:- ", t.getMessage());
//            }
//        });
//
//        ************************** API CALLS ******************************
//        *******************************************************************


    }

    // Code to test list item screen START !!!
    private void moveToListItemActivity() {
        Intent intent = new Intent(MainActivity.this, ListItem.class);
        startActivity(intent);
    }
    // Code to test list item screen START !!!


    //Scrolls
    private void SetupScroll() {
        max = scrolls.size() - 1;

        button_next = findViewById(R.id.Next);
        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position < max) {
                    position += 1;
                    SetScrollView();
                }
            }
        });

        button_previous = findViewById(R.id.Previous);
        button_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (0 < position) {
                    position -= 1;
                    SetScrollView();
                }
            }
        });

    }

    private void SetScrollView() {
        //button visibility
        button_previous.setVisibility(View.VISIBLE);
        button_next.setVisibility(View.VISIBLE);
        button_next.setText("NEXT");

        TextView scroll_title = findViewById(R.id.scroll_title);
        scroll_title.setText(scrolls_title_array[position]);

        if (position == 0) {
            button_previous.setVisibility(View.INVISIBLE);
        }
        if (position == max) {
            button_next.setText("DONE");
        }

        //Views visibility
        for (View v : scrolls) {
            v.setVisibility(View.GONE);
            Log.d("times", String.valueOf(position));

        }
        scrolls.get(position).setVisibility(View.VISIBLE);

    }

    private void ImageScrollSetup() {

        //Setup button
        View add_picture = findViewById(R.id.add_photo);

        add_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!=
                        PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                } else if(n_o_images==0||n_o_images==2){
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Take a picture of the product")
                            .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    TakePictures();
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {}
                            })
                            .show();
                }else if(n_o_images==1){
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Take a picture of the logo")
                            .setMessage("Just take a picture of the product if there is no logo")
                            .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    TakePictures();
                                    logo = true;

                                }
                            })
                            .setNegativeButton("There is no logo", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    TakePictures();
                                    logo = false;
                                }
                            })
                            .show();
                }else{
                    TakePictures();
                }
            }
        });

    }


    //Taking Images
    private void TakePictures(){
        mImageTakenTime = System.currentTimeMillis();

        camera = new Camera.Builder()
                .resetToCorrectOrientation(true)// it will rotate the camera bitmap to the correct orientation from meta data
                .setTakePhotoRequestCode(1)
                .setDirectory("pics")
                .setName("ali_" + System.currentTimeMillis())
                .setImageFormat(Camera.IMAGE_JPEG)
                .setCompression(75)
                .setImageHeight(1000)// it will try to achieve this height as close as possible maintaining the aspect ratio;
                .build(MainActivity.this);
        try {

            camera.takePicture();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void AddImage(final String bitmap) {
        n_o_images++;

        linearLayout = findViewById(R.id.image_scroll);
        imageView = new ImageView(this);
        HorizontalScrollView CameraScroll = findViewById(R.id.CameraScroll);

        imageView.setAdjustViewBounds(true);
        final float s = getResources().getDisplayMetrics().density;
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, (int) (s * 100));
        int margin = (int) s * 10;
        lp.setMargins(margin, margin, margin, margin);
        imageView.setLayoutParams(lp);

        Bitmap bitmap_decoded = ImageProcessing.decodeFile(bitmap);
        imageView.setImageBitmap(bitmap_decoded);

        if (n_o_images == 2 && logo==true) {
            clarifia.GetLogos(bitmap_decoded);
        } else {
            clarifia.GetTags(bitmap_decoded);
            ebay_client_credential_api.SearchByImage(bitmap_decoded,this,0);

        }

        linearLayout.addView(imageView, linearLayout.getChildCount() - 1);
        CameraScroll.fullScroll(View.FOCUS_RIGHT);

        bitmap_array.add(bitmap);
        Log.d("StringPath", bitmap);
        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FullScreenImage.class);
                intent.putExtra("bitmap", bitmap);
                intent.putExtra("index", 1);
                startActivityForResult(intent,FULL_SCREEN_RESULT);

            }
        });
    }

    public void RemoveImage(final String bitmap){
        int position = bitmap_array.indexOf(bitmap);
        bitmap_array.remove(position);
        linearLayout.removeViewAt(position);

    }

    private void clear_added_gallery_image() {


        String[] projection = {MediaStore.Images.ImageColumns.SIZE,
                MediaStore.Images.ImageColumns.DISPLAY_NAME,
                MediaStore.Images.ImageColumns.DATA,
                BaseColumns._ID, MediaStore.Images.ImageColumns.DATE_ADDED};
        final String imageOrderBy = MediaStore.Images.Media._ID + " DESC";
        final String selection = MediaStore.Images.Media.DATE_TAKEN + " > " + mImageTakenTime;
        //// intialize the Uri and the Cursor, and the current expected size.
        Cursor c = null;
        Uri u = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        c = getContentResolver().query(u, projection, selection, null, imageOrderBy);
        if (null != c && c.moveToFirst()) {
            ContentResolver cr = getContentResolver();
            cr.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    BaseColumns._ID + "=" + c.getString(3), null);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == Camera.REQUEST_TAKE_PHOTO) {
            //Get Image
            String bitmap = null;
            try {
                bitmap = camera.getCameraBitmapPath();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (bitmap != null) {
                AddImage(bitmap);
                clear_added_gallery_image();
                if(n_o_images >2){
                    Toast.makeText(this, "Check if the autogenerated info are correct", Toast.LENGTH_LONG).show();
                    Toast.makeText(this, "The other text boxes don't have to filled yet", Toast.LENGTH_LONG).show();
                    Toast.makeText(this, "Press \"NEXT\" if you are ready", Toast.LENGTH_LONG).show();

                }
            } else {
                Toast.makeText(this.getApplicationContext(), "Picture not taken!", Toast.LENGTH_LONG).show();
            }
        }
        if(requestCode== FULL_SCREEN_RESULT && resultCode == RESULT_OK){
            RemoveImage(data.getStringExtra("Bitmap"));
        }
    }


    //Selling
    public void FBDialogue(View view) {
        Facebook_pop_up fb = new Facebook_pop_up();
        fb.SetUp(this,bitmap_array);
    }


    Ebay_Authorisation_Code_API ebay_authorisation_code_api = new Ebay_Authorisation_Code_API(this);
    public void startSignIn(View view) {
        ebay_authorisation_code_api.startSignIn(0);
    }
    @Override
    protected void onResume() {
        super.onResume();
        ebay_authorisation_code_api.ResumeCode(getIntent());
    }


    //System
    private void RecyclerViewSetup() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = findViewById(R.id.OnlineItemsScroll);
        recyclerView.setLayoutManager(layoutManager);


    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        parent = findViewById(R.id.parent);
        bottom = findViewById(R.id.bottom);

        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            parent.setOrientation(LinearLayout.HORIZONTAL);
            lp = new LinearLayout.LayoutParams(0, FrameLayout.LayoutParams.MATCH_PARENT, 1.0f);
            bottom.setLayoutParams(lp);


        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            parent.setOrientation(LinearLayout.VERTICAL);
            lp = new LinearLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, 0, 1.0f);
            bottom.setLayoutParams(lp);

        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.booksBtn) {

        }
        /*if(id== R.id.clear){
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Clear All Input")
                    .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            recreate();

                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {}
                    })
                    .show();
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }




}


