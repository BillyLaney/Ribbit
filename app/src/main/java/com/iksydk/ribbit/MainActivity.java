package com.iksydk.ribbit;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.parse.ParseAnalytics;
import com.parse.ParseUser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.ButterKnife;

public class MainActivity extends ActionBarActivity implements android.support.v7.app.ActionBar.TabListener
{

    public static final String TAG = MainActivity.class.getSimpleName();

    public static final int TAKE_PHOTO_REQUEST = 0;
    public static final int TAKE_VIDEO_REQUEST = 1;
    public static final int CHOOSE_PICTURE_REQUEST = 2;
    public static final int CHOOSE_VIDEO_REQUEST = 3;

    public static final int MEDIA_TYPE_IMAGE = 4;
    public static final int MEDIA_TYPE_VIDEO = 5;
    public static final int FILE_SIZE_LIMIT = 1 * 1024 * 1024 * 10; //10MB

    protected Uri mMediaUri;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
     * will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;
    protected DialogInterface.OnClickListener mDialogOnClickListener = new DialogInterface.OnClickListener()
    {
        @Override
        public void onClick(DialogInterface dialog, int which)
        {
            switch(which)
            {
                case 0: //take picture
                    Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    mMediaUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                    if(mMediaUri == null)
                    {
                        Toast.makeText(MainActivity.this, getString(R.string.error_external_storage_unavailable), Toast.LENGTH_LONG)
                                .show();
                    }
                    else
                    {
                        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mMediaUri);
                        startActivityForResult(takePhotoIntent, TAKE_PHOTO_REQUEST);
                    }

                    break;
                case 1: //take video
                    Intent videoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                    mMediaUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);
                    if(mMediaUri == null)
                    {
                        Toast.makeText(MainActivity.this, getString(R.string.error_external_storage_unavailable), Toast.LENGTH_LONG)
                                .show();
                    }
                    else
                    {
                        videoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mMediaUri);
                        videoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10);
                        videoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
                        startActivityForResult(videoIntent, TAKE_VIDEO_REQUEST);
                    }
                    break;
                case 2://choose picture
                    Intent choosePhotoIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    choosePhotoIntent.setType("image/*");
                    startActivityForResult(choosePhotoIntent, CHOOSE_PICTURE_REQUEST);
                    break;
                case 3://choose video
                    Toast.makeText(MainActivity.this, getString(R.string.video_file_size_warning), Toast.LENGTH_LONG).show();
                    Intent chooseVideoIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    chooseVideoIntent.setType("video/*");
                    startActivityForResult(chooseVideoIntent, CHOOSE_VIDEO_REQUEST);
                    break;
            }
        }

        private boolean isExternalStorageAvailable()
        {
            String state = Environment.getExternalStorageState();
            if(state.equals(Environment.MEDIA_MOUNTED))
            {
                return true;
            }
            else
            {
                return false;
            }
        }

        private Uri getOutputMediaFileUri(int mediaType)
        {
            // To be safe, you should check that the SDCard is mounted
            // using Environment.getExternalStorageState() before doing this.
            if(isExternalStorageAvailable())
            {
                String appName = MainActivity.this.getString(R.string.app_name);

                File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES), appName);
                // This location works best if you want the created images to be shared
                // between applications and persist after your app has been uninstalled.

                // Create the storage directory if it does not exist
                if(!mediaStorageDir.exists())
                {
                    if(!mediaStorageDir.mkdirs())
                    {
                        Log.d(TAG, "failed to create directory");
                        return null;
                    }
                }

                // Create a media file name
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
                File mediaFile;
                if(mediaType == MEDIA_TYPE_IMAGE)
                {
                    mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                            "IMG_" + timeStamp + ".jpg");
                }
                else if(mediaType == MEDIA_TYPE_VIDEO)
                {
                    mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                            "VID_" + timeStamp + ".mp4");
                }
                else
                {
                    return null;
                }

                Log.d(TAG, Uri.fromFile(mediaFile)
                        .toString());

                return Uri.fromFile(mediaFile);
            }
            else
            {
                return null;
            }
        }
    };

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        ParseAnalytics.trackAppOpened(getIntent());

        ParseUser currentUser = ParseUser.getCurrentUser();
        if(currentUser == null)
        {
            navigateToLogin();
        }
        else
        {
            Log.i(TAG, currentUser.getUsername());
        }

        // Set up the action bar.
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the app.
        mSectionsPagerAdapter = new SectionsPagerAdapter(this,
                getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager
                .setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener()
                {
                    @Override
                    public void onPageSelected(int position)
                    {
                        actionBar.setSelectedNavigationItem(position);
                    }
                });

        // For each of the sections in the app, add a tab to the action bar.
        for(int i = 0; i < mSectionsPagerAdapter.getCount(); i++)
        {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(actionBar.newTab()
                    .setText(mSectionsPagerAdapter.getPageTitle(i))
                    .setTabListener(this));
        }
    }

    private void navigateToLogin()
    {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int itemId = item.getItemId();

        switch(itemId)
        {
            case R.id.loginButton:
                ParseUser.logOut();
                navigateToLogin();
                break;
            case R.id.action_edit_friends:
                Intent intent = new Intent(this, EditFriendsActivity.class);
                startActivity(intent);
                break;
            case R.id.action_camera:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setItems(R.array.camera_choice, mDialogOnClickListener);
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(android.support.v7.app.ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction)
    {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(android.support.v7.app.ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction)
    {
    }

    @Override
    public void onTabReselected(android.support.v7.app.ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction)
    {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK)
        {
            if(requestCode == CHOOSE_PICTURE_REQUEST || requestCode == CHOOSE_VIDEO_REQUEST)
            {
                if(data == null)
                {
                    Toast.makeText(this, getString(R.string.general_error), Toast.LENGTH_LONG)
                            .show();
                }
                else
                {
                    mMediaUri = data.getData();

                    Log.i(TAG, "Mediate URL: " + mMediaUri);

                    if(requestCode == CHOOSE_VIDEO_REQUEST)
                    {
                        //make sure file is less than 10MB
                        int fileSize = 0;

                        InputStream inputStream = null;
                        try
                        {
                            inputStream = getContentResolver().openInputStream(mMediaUri);
                            fileSize = inputStream.available();
                        }
                        catch(FileNotFoundException e)
                        {
                            Toast.makeText(this, getString(R.string.error_opening_file), Toast.LENGTH_LONG)
                                    .show();
                            return;
                        }
                        catch(IOException e)
                        {
                            Toast.makeText(this, getString(R.string.error_opening_file), Toast.LENGTH_LONG)
                                    .show();
                            return;
                        }
                        finally
                        {
                            try
                            {
                                inputStream.close();
                            }
                            catch(IOException e)
                            {
                                //intentionally left blank
                                return;
                            }
                        }

                        if(fileSize >= FILE_SIZE_LIMIT)
                        {
                            Toast.makeText(this, getString(R.string.error_file_size_too_large), Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                }
            }
            else
            {
                //add to gallery
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                mediaScanIntent.setData(mMediaUri);
                sendBroadcast(mediaScanIntent);
            }

            Intent recipientsIntent = new Intent(this, RecipientsActivity.class);
            recipientsIntent.setData(mMediaUri);
            startActivity(recipientsIntent);
        }
        else if(resultCode != RESULT_CANCELED)
        {
            Toast.makeText(this, getString(R.string.general_error), Toast.LENGTH_LONG)
                    .show();
        }
    }
}
