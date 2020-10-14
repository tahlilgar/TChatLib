package com.tahlilgargroup.androidchatlibrary;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.microsoft.appcenter.analytics.Analytics;
import com.tahlilgargroup.commonlibrary.CommonClass;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

import static com.tahlilgargroup.androidchatlibrary.ChatClass.driverID;
import static com.tahlilgargroup.commonlibrary.CommonClass.DeviceProperty;



//نمایش عکس
@SuppressLint("Registered")
public class ActivityImage extends AppCompatActivity {

    public static TouchImageView img;
    Bitmap myBitmap=null;
    public static int ImagePos;
    String filename,uri;
    TextView downloadDoc;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        downloadDoc = findViewById(R.id.txtImgDownloadStatus);

        new CommonClass().askForPermission(ActivityImage.this, Manifest.permission.READ_EXTERNAL_STORAGE, CommonClass.WRITE_EXST);



        // اگر تصویر در دیوایس موجود است نمایش بده درغیر اینصورت دانلود کن و نمایش بده
        try {

            img=findViewById(R.id.bigImage);
            Bundle extras = getIntent().getExtras();
            //  byte[] byteArray = extras.getByteArray("picture");
            if (extras != null) {
                uri = extras.getString("picture");
            }
            if (uri != null) {
                filename = uri.substring(Objects.requireNonNull(uri).lastIndexOf("/") + 1);
            }
            //  bmp= BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

            File imgFile = null;
            if (uri != null) {
                imgFile = new File(uri);
            }

            if (imgFile != null) {
                if(imgFile.exists()){

                    myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    img.setImageBitmap(myBitmap);
                    downloadDoc.setText("ذخیره تصویر");



                }else {
    //////////////////////////////////////////////////////////try to download and save video
                    String urlAddress="";
                    List<Messages> messages = new DoCommand_MessageDB(ActivityChat.context).getListOfMessages("MsgPos = " + ImagePos + " and DriverID = '" + driverID + "' and OperatorID = '" + ActivityChat.id+"'");
                    String MsgID="";

                    String filename = uri.substring(Objects.requireNonNull(uri).lastIndexOf("/") + 1);


                    /*String URl= CommonClass.FilesURL +filename;
                    if(messages.size()!=0)
                        urlAddress=!messages.get(0).getHostPath().equals("")?messages.get(0).getHostPath():URl;

                    String root = Environment.getExternalStorageDirectory().toString();
                    File myDir = new File(*//*root + "/tahlilgar"*//*CommonClass.FilesPath);
                    myDir.mkdirs();

                    if (!myDir.exists()) {
                        myDir.mkdir();
                    }*/

                   // DownloadTask.isComplete = false;
                    if (isConnectingToInternet()) {
                        Download download=new Download();
                        if ( filename.length() != 0)
                            download.DownloadAPI(filename,downloadDoc,1);
                        // new DownloadTask(ActivityImage.this, downloadDoc, urlAddress,1);
                    }else
                        new CommonClass().ShowToast(ActivityImage.this, CommonClass.ToastMessages.Is_Disconnect, "");


                }
            }
        } catch (Exception e) {
            Analytics.trackEvent("ImageAC_" + "onCreate " + driverID + "_" + CommonClass.GetCurrentMDate() + "_"+DeviceProperty+"_"  + e.getMessage());
            Log.e("Error", Objects.requireNonNull(e.getMessage()));
            e.printStackTrace();
        }
       // img.setImageBitmap(bmp);


    }

    //Check if internet is present or not
    private boolean isConnectingToInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connectivityManager != null) {
            networkInfo = connectivityManager
                    .getActiveNetworkInfo();
        }
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    //ذخیره در گالری
    public void SaveImage(View view) {

        if (ExternalStorageUtil.isExternalStorageMounted()) {

            FileOutputStream fos = null;
           // img.setImageBitmap(myBitmap);
            File myDir = new File(/*root + "/tahlilgar"*/CommonClass.FilesPath);
            myDir.mkdirs();
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();

            File imgFile = new  File(uri);

            if(imgFile.exists()) {

                myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                img.setImageBitmap(myBitmap);
            }

            if(myBitmap!=null) {
                myBitmap.setHasAlpha(true);
                myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                if (!myDir.exists()) {
                    myDir.mkdir();
                }

                File myDirFile = new File(CommonClass.FilesPath+"/"+filename);
                try {
                    if(myDirFile.exists()){
                        myDirFile.delete();

                    }
                    myDirFile.createNewFile();
                } catch (IOException e) {
                    Analytics.trackEvent("ImageAC_" + "SaveImage1 " + driverID + "_" + CommonClass.GetCurrentMDate() + "_"+DeviceProperty+"_"  + e.getMessage());

                    e.printStackTrace();
                }
                try {
                    fos = new FileOutputStream(myDirFile);
                } catch (FileNotFoundException e) {
                    Analytics.trackEvent("ImageAC_" + "SaveImage2 " + driverID + "_" + CommonClass.GetCurrentMDate() + "_"+DeviceProperty+"_"  + e.getMessage());

                    e.printStackTrace();
                }
                try {
                    fos.write(bytes.toByteArray());
                    fos.flush();
                    fos.close();
                    Toast.makeText(this, "تصویر ذخیره شد", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    Analytics.trackEvent("ImageAC_" + "SaveImage3 " + driverID + "_" + CommonClass.GetCurrentMDate() + "_"+DeviceProperty+"_"  + e.getMessage());

                    e.printStackTrace();
                }

                addImageToGallery(/*root +"/tahlilgar/"*/CommonClass.FilesPath+"/"+filename, ActivityImage.this);

            }else {
                new CommonClass().ShowToast(ActivityImage.this,"فایل دانلود نشده!پس از اطمینان از برقراری ارتباط با اینترنت منتظر کامل شدن دانلود بمانید!",Toast.LENGTH_LONG);
            }

        }

    }

    public static void addImageToGallery(final String filePath, final Context context) {

        try {

            ContentValues values = new ContentValues();

            values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
            values.put(MediaStore.MediaColumns.DATA, filePath);

            context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        }catch (Exception e) {
            Analytics.trackEvent("ImageAC_" + "addImageToGallery " + driverID + "_" + CommonClass.GetCurrentMDate() + "_"+DeviceProperty+"_"  + e.getMessage());
        }
    }

    //وقتی به صفحه چت برمیگردیم تصویر دانلود شده در لیست چت نمایش داده شود
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try {
            List<Messages> messages = new DoCommand_MessageDB(ActivityChat.context).getListOfMessages("MsgPos = " + ImagePos + " and DriverID = '" + driverID + "' and OperatorID = '" + ActivityChat.id+"'");

            ActivityChat.msgDto = new ChatAppMsgDTO(messages.get(0).getIsSend()==1?ChatAppMsgDTO.IMG_TYPE_SENT:ChatAppMsgDTO.IMG_TYPE_RECEIVED,
                    CommonClass.FilesPath+"/"+messages.get(0).getMsgContent(),
                    "",
                    new CommonClass().PerisanNumber(messages.get(0).getMsgDate() + "\n" + messages.get(0).getMsgTime()), BigInteger.valueOf(Long.parseLong(messages.get(0).getMsgID())),messages.get(0).getIsEdited()==1);
            ActivityChat.msgDtoList.set(messages.get(0).getMsgPos(), ActivityChat.msgDto);
            ActivityChat.chatAppMsgAdapter.notifyItemChanged(messages.get(0).getMsgPos());

        }catch (Exception e) {
            Analytics.trackEvent("ImageAC_" + "onBackPressed " + driverID + "_" + CommonClass.GetCurrentMDate() + "_"+DeviceProperty+"_"  + e.getMessage());
        }
    }
}
