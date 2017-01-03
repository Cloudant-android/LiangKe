package com.mchat.api.lkinterface;

import com.mchat.api.model.UserInfo;

import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by CloudAnt on 2016/12/6.
 * 获取用户信息的接口
 */

public interface UserInfoService {
    @POST("/getMember")
    Observable<UserInfo> getUserInfo(
            @Header("sgin")String sgin,
            @Header("time")int time,
            @Header("token")String token,
            @Header("uid")String uid
    );
}
