package com.mchat.api.lkinterface;

import com.mchat.api.model.LoginInfo;


import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by CloudAnt on 2016/12/6.
 * 登录时的接口
 *
 */

public interface LoginService {
    @POST("/login")
    Observable<LoginInfo> login(
            @Header("sign")String sign,
            @Header("time") int time,
            @Query("username")String username,
            @Query("password")String password
    );

}
