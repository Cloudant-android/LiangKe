package com.mchat.api.lkinterface;

import com.mchat.api.model.SetUserInfo;

import java.io.File;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by CloudAnt on 2016/12/6.
 * 修改用户信息的接口
 */

public interface SetUserInfoService {
    @Multipart
    @POST("/updateMember")
    Observable<SetUserInfo> setUserinfo(
            @Header("sign")String sign,
            @Header("time")int time,
            @Header("token")String token,
            @Header("uid")String uid,
            @Part("gender")RequestBody  gender,
            @Part("profession")RequestBody  profession,
            @Part("nickname")RequestBody  nickname,
            @Part List<MultipartBody.Part> file
    );

}
