package com.tahlilgargroup.androidchatlibrary;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.microsoft.appcenter.analytics.Analytics;
import com.tahlilgargroup.commonlibrary.CommonClass;

import java.io.File;
import java.util.List;
import java.util.Objects;

import static com.tahlilgargroup.androidchatlibrary.ChatClass.driverID;
import static com.tahlilgargroup.commonlibrary.CommonClass.DeviceProperty;



@SuppressLint("Registered")
public class ActivityVideo extends AppCompatActivity {
    public static VideoView myVideoView;
    private int position = 0;
    public static int videoPos;
    private ProgressDialog progressDialog;
    private MediaController mediaControls;
    public static Handler handler;
    public static Runnable handlerTask;
    TextView downloadDoc;

    //نمایش فیلم در صورت وجود در حافظه و در غیر اینصورت دانلود و نمایش
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        try {

            //set the media controller buttons
            if (mediaControls == null) {
                mediaControls = new MediaController(this);
            }

            //initialize the VideoView
            myVideoView = (VideoView) findViewById(R.id.bigVideo);

            downloadDoc = findViewById(R.id.txtDownloadStatus);


            try {
                //set the media controller in the VideoView
                myVideoView.setMediaController(mediaControls);

                //set the uri of the video to be played


                Bundle extras = getIntent().getExtras();
                String uri = null;
                if (extras != null) {
                    uri = extras.getString("Video");
                }

                File f = null;
                if (uri != null) {
                    f = new File(uri);
                }
                if (f != null) {
                    if (f.exists()) {
                        myVideoView.setVideoPath(uri);
                        downloadDoc.setText("");

                    } else {
                        //////////////////////////////////////////////////////////try to download and save video
                        List<Messages> messages = new DoCommand_MessageDB(ActivityChat.context).getListOfMessages("MsgPos = " + videoPos + " and DriverID = '" + ChatClass.driverID + "' and OperatorID = '" + ActivityChat.id + "'");

                        String filename = uri.substring(Objects.requireNonNull(uri).lastIndexOf("/") + 1);

                        if (isConnectingToInternet()) {
                            Download download = new Download();
                            if ( filename.length() != 0)
                                download.DownloadAPI( filename, downloadDoc, 2);
                            // new DownloadTask(ActivityVideo.this, downloadDoc, urlAddress, 2);

                        } else
                            new CommonClass().ShowToast(ActivityVideo.this, CommonClass.ToastMessages.Is_Disconnect, "");


                    }
                }
            } catch (Exception e) {
                Analytics.trackEvent("VideoAC_" + "onCreate " + driverID + "_" + CommonClass.GetCurrentMDate() + "_"+DeviceProperty+"_"  + e.getMessage());

                Log.e("Error", Objects.requireNonNull(e.getMessage()));
                e.printStackTrace();
            }

            myVideoView.requestFocus();
            //we also set an setOnPreparedListener in order to know when the video file is ready for playback
            myVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                public void onPrepared(MediaPlayer mediaPlayer) {
                    // close the progress bar and play the video
                    // progressDialog.dismiss();
                    //if we have a position on savedInstanceState, the video playback should start from here
                    myVideoView.seekTo(position);
                    if (position == 0) {
                        myVideoView.start();
                    } else {
                        //if we come from a resumed activity, video playback will be paused
                        myVideoView.pause();
                    }
                }
            });
        }catch (Exception e)
        {
            Analytics.trackEvent("VideoAC_" + "onCreate2 " + driverID + "_" + CommonClass.GetCurrentMDate() + "_"+DeviceProperty+"_"  + e.getMessage());

        }
    }


    //Open downloaded folder
    private void openDownloadedFolder() {
        //First check if SD Card is present or not
        if (new CommonClass.CheckForSDCard().isSDCardPresent()) {

            //Get Download Directory File
            File apkStorage = new File(
                    /*Environment.getExternalStorageDirectory() + "/"
                            + "tahlilqar"*/CommonClass.FilesPath);

            //If file is not present then display Toast
            if (!apkStorage.exists())
                Toast.makeText(ActivityVideo.this, "Right now there is no directory. Please download some file first.", Toast.LENGTH_SHORT).show();

            else {

                //If directory is present Open Folder

                /** Note: Directory will open only if there is a app to open directory like File Manager, etc.  **/

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                Uri uri = Uri.parse(/*Environment.getExternalStorageDirectory().getPath()
                        + "/" + "tahlilqar"*/CommonClass.FilesPath);
                intent.setDataAndType(uri, "file/*");
                startActivity(Intent.createChooser(intent, "Open Download Folder"));
            }

        } else
            Toast.makeText(ActivityVideo.this, "Oops!! There is no SD Card.", Toast.LENGTH_SHORT).show();

    }

    //Check if internet is present or not
    private boolean isConnectingToInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connectivityManager != null) {
            networkInfo = connectivityManager
                    .getActiveNetworkInfo();
        }
        return networkInfo != null && networkInfo.isConnected();
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        //we use onSaveInstanceState in order to store the video playback position for orientation change
       try {

           savedInstanceState.putInt("Position", myVideoView.getCurrentPosition());
           myVideoView.pause();
       }catch (Exception e)
       {
           Analytics.trackEvent("VideoAC_" + "onSaveInstanceState " + driverID + "_" + CommonClass.GetCurrentMDate() + "_"+DeviceProperty+"_"  + e.getMessage());

       }
    }

    @Override
    public void onRestoreInstanceState(@Nullable Bundle savedInstanceState) {
        try {

            if (savedInstanceState != null) {
                super.onRestoreInstanceState(savedInstanceState);
            }
            if (savedInstanceState != null) {
                position = savedInstanceState.getInt("Position");
            }
            myVideoView.seekTo(position);
        }catch (Exception e)
        {
            Analytics.trackEvent("VideoAC_" + "onRestoreInstanceState " + driverID + "_" + CommonClass.GetCurrentMDate() + "_"+DeviceProperty+"_"  + e.getMessage());

        }
    }
}
