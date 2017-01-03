package com.mchat.api.lkinterface;

import com.mchat.api.model.FriendsListInfo;

import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by CloudAnt on 2016/12/20.
 * 获取好友列表
 */

public interface FriendsService {
    @POST("/getFriends")
    Observable<FriendsListInfo> FriendsList(
            @Header("sign")String sign,
            @Header("time")int time,
            @Header("token")String token,
            @Header("uid")String uid,
            @Query("page")int page
    );



}
