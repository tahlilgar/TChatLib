package com.tahlilgargroup.androidchatlibrary;

import android.Manifest;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.microsoft.appcenter.analytics.Analytics;
import com.tahlilgargroup.commonlibrary.CommonClass;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.tahlilgargroup.androidchatlibrary.ChatClass.appCode;
import static com.tahlilgargroup.androidchatlibrary.ChatClass.context;
import static com.tahlilgargroup.androidchatlibrary.ChatClass.driverID;
import static com.tahlilgargroup.commonlibrary.CommonClass.DeviceProperty;



public class Download {
    public void DownloadAPI(final String fileName, final TextView DownloadStatus, final int messageType) {
        try {

            new CommonClass().ShowWaitingDialog(ChatClass.context,context.getString(R.string.Downloading));
            APIService service =
                    ServiceGenerator.GetCommonClient().create(APIService.class);
            Call<ResponseBody> call2 = service.DownloadFile(/*0, 0,*/ fileName,appCode);
            call2.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@Nullable Call<ResponseBody> call, @Nullable Response<ResponseBody> response) {

                    if (response != null && response.isSuccessful()) {
                        // String ext = response.headers().get("type");
                        name = fileName;

                        PathName = CommonClass.FilesPath + "/" + name;
                        //  PathName=MainActivity.context.getExternalFilesDir(null) + File.separator + UUID.randomUUID()+"."+ext;
                        // MainActivity.ComObj.ShowToast(MainActivity.context, "شروع دانلود فایل "+pos+" از تیکت شماره " +ticketFileName.getTicketID()+"...", Toast.LENGTH_SHORT);
                        DownloadStatus.setText(context.getString(R.string.Downloading));


                        // MainActivity.ComObj.ShowToast(MainActivity.context,response.body()+"s", Toast.LENGTH_LONG);
                        if (writeResponseBodyToDisk(response.body(), DownloadStatus, messageType)) {
                            new CommonClass().CancelWaitingDialog();

                           /* Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(PathName));
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                            Intent chooser = Intent.createChooser(intent, "Open with");
                            if (intent.resolveActivity(MainActivity.context.getPackageManager()) != null)
                                MainActivity.context.startActivity(chooser);
                            else
                                Toast.makeText(MainActivity.context, "فایل بار گذاری شد اما برنامه مناسب برای باز کردن فایل پیدا نشد!", Toast.LENGTH_LONG).show();*/


                        } else {
                            new CommonClass().CancelWaitingDialog();

                            try {
                                DownloadStatus.setText(context.getString(R.string.DownloadFailed));


                            } catch (Exception ignored) {

                            }


                        }

                    } else {

                        new CommonClass().CancelWaitingDialog();

                        int errMsg = 0;
                        if (response != null) {
                            errMsg = response.raw().code();
                        }
                        if (errMsg != 0) {
                            new CommonClass().ShowToast(ChatClass.context, new CommonClass().ErrorMessages(errMsg, ChatClass.context), Toast.LENGTH_SHORT);
                        } else {
                            if (response != null) {
                                new CommonClass().ShowToast(ChatClass.context, response.raw().message(), Toast.LENGTH_SHORT);
                            }
                        }

                        Analytics.trackEvent("Download_" + "DownloadAPI " + driverID + "_" + CommonClass.GetCurrentMDate() + "_" + DeviceProperty + "_" + errMsg);

                        DownloadStatus.setText(context.getString(R.string.DownloadFailed));


                    }


                }




                @Override
                public void onFailure(@Nullable Call<ResponseBody> call, @Nullable Throwable t) {
                    new CommonClass().CancelWaitingDialog();

                    if (t != null) {
                        if (ChatClass.context != null)
                            new CommonClass().ShowToast(ChatClass.context, CommonClass.ToastMessages.Network_Problem, t.getMessage());
                    }

                }
            });
        } catch (Exception ex) {
            new CommonClass().CancelWaitingDialog();
            if (ChatClass.context != null) {
                String d = ex.getMessage();
                new CommonClass().ShowToast(ChatClass.context, new CommonClass().ErrorMessages(11,ChatClass.context) + d, Toast.LENGTH_SHORT);

            }
        }

    }

    String PathName;
    String root;
    String name;

    Toast mToast;


    //ذخیره فایل دانلود شده
    @SuppressLint("SetTextI18n")
    private boolean writeResponseBodyToDisk(ResponseBody body, TextView DownloadStatus, int messageType) {
        new CommonClass().askForPermission(ChatClass.context, Manifest.permission.WRITE_EXTERNAL_STORAGE, CommonClass.WRITE_EXST);

        //ساخت یا ورود به دایرکتوری

        FileOutputStream fos = null;
        File myDir = new File(CommonClass.FilesPath);
        myDir.mkdirs();
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        if (!myDir.exists()) {
            myDir.mkdir();
        }


        try {
            //string type without .


            File futureStudioIconFile = new File(PathName);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);


                int N = 2;


                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    double f = (double) (fileSizeDownloaded / (Math.pow(1024, 2)));
                    double d = f * Math.pow(10, N);
                    int i = (int) d;
                    double f2 = i / Math.pow(10, N);

                    double f1 = (double) (fileSize / (Math.pow(1024, 2)));
                    double d1 = f1 * Math.pow(10, N);
                    int i1 = (int) d1;
                    double f3 = i1 / Math.pow(10, N);


                    DownloadStatus.setText(f2 + context.getString(R.string.DownloadFrom) + f3);


                    if (fileSize == fileSizeDownloaded) {
                        new CommonClass().CancelWaitingDialog();

                        //Toast.makeText(MainActivity.context, MainActivity.ComObj.PerisanNumber(" فایل " + pos1 + " از تیکت شماره " + ticketFileName.getTicketID() + " در پوشه tahlilgar ذخیره شد"), Toast.LENGTH_SHORT).show();
                        if (messageType == 2) {
                            ActivityVideo.myVideoView.setVideoPath(futureStudioIconFile.getPath());
                            DownloadStatus.setText(context.getString(R.string.DownloadCompleted));//If Download completed then change button text
                            break;

                        } else if (messageType == 1) {
                            File imgFile = new File(futureStudioIconFile.getPath());
                            if (imgFile.exists()) {
                                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                                ActivityImage.img.setImageBitmap(myBitmap);
                            }
                            DownloadStatus.setText(context.getString(R.string.DownloadCompleted));//If Download completed then change button text
                            break;

                        } else if (messageType == 3) {
                            ChatAppMsgAdapter chatAppMsgAdapter = new ChatAppMsgAdapter(null);
                            chatAppMsgAdapter.mediaPlayer = MediaPlayer.create(ActivityChat.context, Uri.parse(futureStudioIconFile.getPath()));
                            chatAppMsgAdapter.mediaPlayer.start();
                            String voceDuration = chatAppMsgAdapter.mediaPlayer.getDuration() + "";
                            int t = Integer.parseInt(voceDuration);
                            int s = t / 1000;
                            int m = s / 60;
                            while (s >= 60) {
                                s -= 60;
                            }
                            DownloadStatus.setText(m + ":" + s);//If Download completed then change button text
                            break;

                        }
                    }


                }
                // Log.d("", "file download: " + fileSizeDownloaded + " of " + fileSize);


                outputStream.flush();
                new CommonClass().CancelWaitingDialog();

                return true;
            }
            catch (IOException e) {
                new CommonClass().CancelWaitingDialog();

                return false;
            } finally {
                new CommonClass().CancelWaitingDialog();

                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }


        }
        catch (IOException e) {
            new CommonClass().CancelWaitingDialog();

            return false;
        }
    }
}
