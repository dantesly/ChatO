package com.m21438255.proyectosnapchat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity implements ActionBar.TabListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    public static final int TAKE_PHOTO_REQUEST=0;
    public static final int TAKE_VIDEO_REQUEST=1;
    public static final int PICK_PHOTO_REQUEST=2;
    public static final int PICK_VIDEO_REQUEST=3;

    public static final int MEDIA_TYPE_IMAGE=1;
    public static final int MEDIA_TYPE_VIDEO=2;
    public static final int FILE_SIZE_LIMIT=1024*1024*10;//10MB

    protected Uri mMediaUri;
    DialogInterface.OnClickListener mDialogListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case 0://Hacer foto
                    Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    mMediaUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                    if (mMediaUri==null){
                        //display an error
                        Toast.makeText(MainActivity.this, R.string.storage_Error, Toast.LENGTH_LONG).show();
                    } else {
                        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mMediaUri);
                        startActivityForResult(takePhotoIntent, TAKE_PHOTO_REQUEST);
                    }


                    break;
                case 1://Hacer video
                    Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                    mMediaUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);
                    if (mMediaUri==null){
                        //display an error
                        Toast.makeText(MainActivity.this, R.string.storage_Error, Toast.LENGTH_LONG).show();
                    } else {
                        takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mMediaUri);
                        takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT,10);
                        takeVideoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY,0);
                        startActivityForResult(takeVideoIntent, TAKE_VIDEO_REQUEST);

                    }
                    break;
                case 2://Elegir foto
                    Intent choosePhotoIntent = new Intent (Intent.ACTION_GET_CONTENT);
                    choosePhotoIntent.setType("image/*");
                    startActivityForResult(choosePhotoIntent, PICK_PHOTO_REQUEST);
                    break;
                case 3://Elegir video
                    Intent chooseVideoIntent = new Intent (Intent.ACTION_GET_CONTENT);
                    chooseVideoIntent.setType("video/*");
                    Toast.makeText(MainActivity.this, "Tamaño máximo 10MB", Toast.LENGTH_LONG).show();
                    startActivityForResult(chooseVideoIntent, PICK_VIDEO_REQUEST);
                    break;
            }
        }

        private Uri getOutputMediaFileUri(int mediaType) {
            if(isExternalStorageAvailable()){
                //String appname = MainActivity.this.getString(R.string.app_name);
                //File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),appname);
                //Obtener el Uri
                //1. Obtener el directorio del almacenamiento externo

                String appname= MainActivity.this.getString(R.string.app_name);
                File mediaStorageDir = null;

                switch (mediaType) {
                    case MEDIA_TYPE_IMAGE:
                        mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),appname);

                        break;
                    case MEDIA_TYPE_VIDEO:
                        mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES),appname);
                        break;
                }
                //2. Crear nuestro subdirectorio
                if (! mediaStorageDir.exists()){
                    if (! mediaStorageDir.mkdirs()){
                        Log.e(TAG, "Error al crear el directorio");
                        return null;
                    }
                }

                //3. Crear un nombre del fichero
                //4. Crear un objeto File con el nombre del fichero
                File mediaFile;
                Date now = new Date ();
                String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", new Locale("es", "ES")).format(now);
                String path = mediaStorageDir.getPath()  + File.separator;
                if (mediaType==MEDIA_TYPE_IMAGE) {
                    mediaFile = new File(path+ "IMG_" + timestamp + ".jpg");
                } else if(mediaType==MEDIA_TYPE_VIDEO){
                    mediaFile = new File(path+ "VID_" + timestamp + ".mp4");
                } else {
                    return null;
                }
                Log.d(TAG, "Fichero: "+Uri.fromFile(mediaFile));
                //5. Devolver el Uri del fichero

                return Uri.fromFile(mediaFile);
            } else {
                return null;
            }
        }
        private boolean isExternalStorageAvailable(){
            String state = Environment.getExternalStorageState();
            if(state.equals(Environment.MEDIA_MOUNTED)){
                return true;
            } else{
                return false;
            }

        }
    };
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
            // do stuff with the user
            Log.i(TAG, currentUser.getUsername());
        } else {
            // show the signup or login screen
            abrirLogin();
        }
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
    }

    private void abrirLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            if (requestCode== PICK_PHOTO_REQUEST || requestCode == PICK_VIDEO_REQUEST){
                if (data==null){
                    Toast.makeText(MainActivity.this, "Ha ocurrido un error", Toast.LENGTH_LONG).show();
                }else{
                    mMediaUri=data.getData();
                }
                Log.i(TAG, "Media Uri "+mMediaUri);
                if (requestCode == PICK_VIDEO_REQUEST){
                    int filesize=0;
                    InputStream inputStream= null;
                    try {
                        inputStream = getContentResolver().openInputStream(mMediaUri);
                        filesize = inputStream.available();
                    }catch (FileNotFoundException e){
                        Toast.makeText(MainActivity.this, "Hay un error con el fichero", Toast.LENGTH_LONG).show();
                        return;
                    }
                    catch (IOException e){
                        Toast.makeText(MainActivity.this, "Hay un error con el fichero", Toast.LENGTH_LONG).show();
                        return;
                    }
                    finally {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            /*En blanco*/
                        }
                    }
                    if (filesize >= FILE_SIZE_LIMIT){
                        Toast.makeText(MainActivity.this, "Video demasiado grande", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
            }else {
                //add it to the gallery
                Intent mediaScantIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                mediaScantIntent.setData(mMediaUri);
                sendBroadcast(mediaScantIntent);
            }

            Intent recipientsIntent = new Intent(this,RecipientsActivity.class);
            recipientsIntent.setData(mMediaUri);
            String fileType;
            if (requestCode == PICK_PHOTO_REQUEST || requestCode == TAKE_PHOTO_REQUEST){
                fileType=ParseConstants.TYPE_IMAGE;
            }else{
                fileType=ParseConstants.TYPE_VIDEO;
            }
            recipientsIntent.putExtra(ParseConstants.KEY_FILE_TYPE, fileType);
            startActivity(recipientsIntent);

        }else if (resultCode!=RESULT_CANCELED){
            Toast.makeText(this, "Ha ocurrido un error", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch(id){
            case R.id.action_logout:
                ParseUser.logOut();
                abrirLogin();
                break;

            case R.id.action_edit_friends:
                Intent intent = new Intent (this, EditFriendsActivity.class);
                startActivity(intent);
                break;

            case R.id.action_camera:
                dialogCameraChoices();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void dialogCameraChoices() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(R.array.camera_choices, mDialogListener);
        AlertDialog dialog = builder.create();
        dialog.show();

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


}
