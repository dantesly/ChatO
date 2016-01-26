package com.m21438255.proyectosnapchat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.parse.ParseUser;

/**
 * none
 */
    public class MainActivity extends ActionBarActivity implements ActionBar.TabListener {
        static final int TAKE_PHOTO_REQUEST = 0;
        static final int TAKE_VIDEO_REQUEST = 1;
        static final int PICK_PHOTO_REQUEST = 2;
        static final int PICK_VIDEO_REQUEST = 3;
        static final int MEDIA_TYPE_IMAGE = 4;
        static final int MEDIA_TYPE_VIDEO = 5;

        //URI se usaría para web, Uri para archivos (ver documentación en internet)
        Uri mMediaUri = null;
        /**
         * The {@link android.support.v4.view.PagerAdapter} that will provide
         * fragments for each of the sections. We use a
         * {@link FragmentPagerAdapter} derivative, which will keep every
         * loaded fragment in memory. If this becomes too memory intensive, it
         * may be best to switch to a
         * {@link android.support.v4.app.FragmentStatePagerAdapter}.
         */
        SectionsPagerAdapter mSectionsPagerAdapter;

        /**
         * The {@link ViewPager} that will host the section contents.
         */
        ViewPager mViewPager;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            ParseUser currentUser = ParseUser.getCurrentUser();
            if (currentUser != null) {
                // Set up the action bar.
                final ActionBar actionBar = getSupportActionBar();
                actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

                // Create the adapter that will return a fragment for each of the three
                // primary sections of the activity.
                mSectionsPagerAdapter = new SectionsPagerAdapter(this,getSupportFragmentManager());

                // Set up the ViewPager with the sections adapter.
                mViewPager = (ViewPager) findViewById(R.id.pager);
                mViewPager.setAdapter(mSectionsPagerAdapter);

                // When swiping between different sections, select the corresponding
                // tab. We can also use ActionBar.Tab#select() to do this if we have
                // a reference to the Tab.
                mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        actionBar.setSelectedNavigationItem(position);
                    }
                });

                // For each of the sections in the app, add a tab to the action bar.
                for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
                    // Create a tab with text corresponding to the page title defined by
                    // the adapter. Also specify this Activity object, which implements
                    // the TabListener interface, as the callback (listener) for when
                    // this tab is selected.
                    actionBar.addTab(
                            actionBar.newTab()
                                    .setText(mSectionsPagerAdapter.getPageTitle(i))
                                    .setTabListener(this));
                }
            }else {
                cargarLogin();
            }
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            int fileSize;
            int FILE_SIZE_LIMIT = 1024 * 1024 * 10;
            if (resultCode == RESULT_OK){
                if(requestCode == PICK_PHOTO_REQUEST || requestCode == PICK_VIDEO_REQUEST) {
                    if (data != null) {
                        mMediaUri = data.getData();
                        InputStream objeto = null;
                        try {
                        objeto = getContentResolver().openInputStream(mMediaUri);
                        fileSize = objeto.available();
                            if(fileSize < FILE_SIZE_LIMIT){

                            }else{
                                Context context = getApplicationContext();
                                CharSequence text = "Excedido el tamaño";
                                int duration = Toast.LENGTH_SHORT;

                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();
                            }
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally{
                            try {
                                objeto.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        Intent enviaActividad = new Intent(this, RecipientsActivity.class);
                        enviaActividad.setData(mMediaUri);
                        startActivity(enviaActividad);

                    }else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Error de datos").setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                }else {
                    Intent mediaScantIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    mediaScantIntent.setData(mMediaUri);
                    sendBroadcast(mediaScantIntent);

                    Intent enviaActividad = new Intent(this, RecipientsActivity.class);
                    enviaActividad.setData(mMediaUri);
                    startActivity(enviaActividad);
                }

            }else{
                if(resultCode != RESULT_CANCELED){
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Error al salir").setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }

        }

        private Uri getOutputMediaFileUri(int mediaType){
            File mediaFile;
            String appname = MainActivity.this.getString(R.string.app_name);
            File mediaStorageDir = null;
            if(isExternalStorageAvailable()){
                switch (mediaType){
                    case MEDIA_TYPE_IMAGE:
                        mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES), appname);
                        break;
                    case MEDIA_TYPE_VIDEO:
                        mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_MOVIES), appname);
                        break;
                }

                if(!mediaStorageDir.exists()){
                    if(!mediaStorageDir.mkdirs()){
                       Log.d("MainActivity", "No se ha podido crear la carpeta");
                       return null;
                    }
                }

                Date now = new Date();
                String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", new Locale("es", "ES")).format(now);
                String path = mediaStorageDir.getPath() + File.separator;
                if(mediaType == MEDIA_TYPE_IMAGE){
                    mediaFile = new File(path + "IMG_" + timestamp + ".jpg");
                }else if(mediaType == MEDIA_TYPE_VIDEO){
                    mediaFile = new File(path + "VID_" + timestamp + ".mp4");
                }else{

                    return null;
                }

                return Uri.fromFile(mediaFile);
            }else
                return null;
        }

        private static boolean isExternalStorageAvailable() {
            String state = Environment.getExternalStorageState();

            if (state.equals(Environment.MEDIA_MOUNTED)){
                return true;
            }else{
                return false;
            }
        }

        private DialogInterface.OnClickListener mDialogListener(){
            DialogInterface.OnClickListener dialogListener =
                    new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch(which){
                                case 0:
                                    Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    mMediaUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                                    if(mMediaUri == null){
                                        Context context = getApplicationContext();
                                        CharSequence text = "Error de Almacenamiento";
                                        int duration = Toast.LENGTH_SHORT;

                                        Toast toast = Toast.makeText(context, text, duration);
                                        toast.show();
                                    }else{
                                        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mMediaUri);
                                    }
                                    startActivityForResult(takePhotoIntent, TAKE_PHOTO_REQUEST);

                                    break;
                                case 1:
                                    Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                                    mMediaUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);
                                    if(mMediaUri == null){
                                        Context context = getApplicationContext();
                                        CharSequence text = "Error de Almacenamiento";
                                        int duration = Toast.LENGTH_SHORT;

                                        Toast toast = Toast.makeText(context, text, duration);
                                        toast.show();
                                    }else{
                                        takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mMediaUri);
                                        takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10);
                                        takeVideoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
                                    }
                                    startActivityForResult(takeVideoIntent, TAKE_VIDEO_REQUEST);
                                    break;
                                case 2:
                                    Intent choosePhotoIntent = new Intent(Intent.ACTION_GET_CONTENT);
                                    choosePhotoIntent.setType("image/*");
                                    startActivityForResult(choosePhotoIntent, PICK_PHOTO_REQUEST);
                                    break;
                                case 3:
                                    Intent chooseVideoIntent = new Intent(Intent.ACTION_GET_CONTENT);
                                    chooseVideoIntent.setType("video/*");
                                    startActivityForResult(chooseVideoIntent, PICK_VIDEO_REQUEST);
                                    break;
                            }
                        }
                    };
            return dialogListener;
        }

        public void dialogCameraChoices(){

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setItems(R.array.camera_choices, mDialogListener());
            AlertDialog alert = builder.create();
            alert.show();
        }

        public void cargarLogin(){
            Intent intentLogin = new Intent(this, LoginActivity.class);
            intentLogin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intentLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intentLogin);
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            switch(id) {
                case R.id.action_logout:
                    ParseUser.logOut();
                    ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
                    cargarLogin();
                    break;
                case R.id.action_edit_friends:
                    Intent editFriends = new Intent(this, EditFriendsActivity .class);
                    startActivity(editFriends);
                    break;
                case R.id.action_camera:
                    dialogCameraChoices();
                }


            return super.onOptionsItemSelected(item);
        }

        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
            // When the given tab is selected, switch to the corresponding page in
            // the ViewPager.
            mViewPager.setCurrentItem(tab.getPosition());
        }

        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        }

        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        }

        /**
         * A placeholder fragment containing a simple view.
         */
        public static class PlaceholderFragment extends Fragment {
            /**
             * The fragment argument representing the section number for this
             * fragment.
             */
            private static final String ARG_SECTION_NUMBER = "section_number";

            /**
             * Returns a new instance of this fragment for the given section
             * number.
             */
            public static PlaceholderFragment newInstance(int sectionNumber) {
                PlaceholderFragment fragment = new PlaceholderFragment();
                Bundle args = new Bundle();
                args.putInt(ARG_SECTION_NUMBER, sectionNumber);
                fragment.setArguments(args);
                return fragment;
            }

            public PlaceholderFragment() {
            }

            @Override
            public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                     Bundle savedInstanceState) {
                View rootView = inflater.inflate(R.layout.fragment_main, container, false);
                return rootView;
            }
        }

    }
