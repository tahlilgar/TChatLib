package com.tahlilgargroup.androidchatlibrary;


import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;


public interface APIService {


    @POST("Common/ChatApi/GetChatList")
    @Headers("apiPass: r77FtM&b7m8Se%*4")
    Call<ArrayList<Server_Message>> GetMessages(@Body DistributionChatParam distributionChatParam);


    @POST("Common/ChatApi/GetFile")
    @Headers("apiPass: r77FtM&b7m8Se%*4")
    Call<ResponseBody> DownloadFile(@Query("ID") String ID);/////////////////////////////////////////////////////

    @Multipart
    @POST("Common/ChatApi/ChatIUD")
    @Headers("apiPass: r77FtM&b7m8Se%*4")
    Call<List<String>> ChatIUD(
            @Part("ChatParam") ChatIUDModel chatIUDModel
            , @Part MultipartBody.Part file);/////////////////////////////////////////////////////


//    @Multipart
//    @POST("Common/ChatApi/UploadFile")
//    @Headers("apiPass: r77FtM&b7m8Se%*4")
//    Call<String> uploadMulFiles(@Part MultipartBody.Part file,@Query("AppCode") int AppCode);//////////////////////////////////////////////////////
//
}
