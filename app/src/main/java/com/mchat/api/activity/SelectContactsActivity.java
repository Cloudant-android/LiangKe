package com.mchat.api.activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.mchat.api.LKApplication;
import com.mchat.api.R;
import com.mchat.api.hx.db.UserDao;
import com.mchat.api.lkinterface.FriendsService;
import com.mchat.api.model.FriendsListInfo;
import com.mchat.api.sqlitedb.UserInfoCacheSvc;

import java.util.ArrayList;

import retrofit2.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SelectContactsActivity extends Activity {
    private ListView ContactlistView;
    private Retrofit retrofit;
    private int page_count = 0;
    private int page =1;
    private ArrayList<String> gender = new ArrayList<>();
    private ArrayList<String> proferssion = new ArrayList<>();
    private ArrayList<String> username = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        retrofit = LKApplication.initRetrofit();
        setContentView(R.layout.activity_select_contacts);
        initView();
        request_data();
    }

    private void initView(){
        ContactlistView = (ListView) findViewById(R.id.selectcontactsList);
    }
    public void request_data(){
        getListData(page);
        if (page<=page_count){
            for (int i =page;i<=page_count;i++){
                getListData(i);
            }
        }
    }

    private void getListData(int page){
        FriendsService friendsService = retrofit.create(FriendsService.class);
        friendsService.FriendsList(LKApplication.getSign(),
                LKApplication.getTime(),
                LKApplication.getInstance().getToken(),LKApplication.getInstance().getUid(),page)
                .subscribeOn(Schedulers.newThread())//请求在新的线程中执行
                .observeOn(Schedulers.io())         //请求完成后在io线程中执行
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<FriendsListInfo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("error_friends",e.getMessage());

                    }

                    @Override
                    public void onNext(FriendsListInfo friendsListInfo) {
                        if (friendsListInfo.getCode()==1000){
                            page_count = friendsListInfo.getData().getTotal_rows();
                            for (FriendsListInfo.DataBean.ListBean listBean : friendsListInfo.getData().getList()){
                                UserDao userDao = new UserDao(SelectContactsActivity.this);
                                EaseUser easeUser = new EaseUser(listBean.getFriend_id());
                                easeUser.setAvatar(listBean.getLogo());
                                easeUser.setNick(listBean.getNickname());
                                EaseCommonUtils.setUserInitialLetter(easeUser);
                                userDao.saveContact(easeUser);
                                Log.d("listfriends",listBean.getFriend_id());
                            }
                        }

                    }
                });
    }




}
