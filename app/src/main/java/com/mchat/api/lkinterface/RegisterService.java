package com.mchat.api.lkinterface;

import com.mchat.api.model.RegisterInfo;


import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by CloudAnt on 2016/12/6.
 * 注册时的接口
 */

public interface RegisterService {

    @POST("/register")
    Observable<RegisterInfo> register(
            @Header("sign")String sign,
            @Header("time")int time,
            @Query("username")String username,
            @Query("password")String password
    );
}
