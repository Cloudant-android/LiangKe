package com.mchat.api.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMContactListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.util.EMLog;
import com.mchat.api.R;
import com.mchat.api.activity.base.BaseActivity;
import com.mchat.api.fragment.ContactFragment;
import com.mchat.api.fragment.ConversationFragment;
import com.mchat.api.fragment.LifeFragment;
import com.mchat.api.fragment.MeFragment;
import com.mchat.api.hx.Constant;
import com.mchat.api.hx.DemoHelper;
import com.mchat.api.hx.activity.ChatActivity;
import com.mchat.api.hx.activity.GroupsActivity;
import com.mchat.api.hx.db.InviteMessgeDao;
import com.mchat.api.hx.utils.PermissionsManager;
import com.mchat.api.hx.utils.PermissionsResultAction;
import com.mchat.api.view.DragPointView;

import java.util.ArrayList;
import java.util.List;
@SuppressLint("NewApi")
public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener,
		View.OnClickListener,
		DragPointView.OnDragListencer {
	protected static final String TAG = "MainActivity";
	public static ViewPager mViewPager;
	private List<Fragment> mFragment = new ArrayList<>();
	private ImageView moreImage, mImageChats, mImageContact, mImageFind, mImageMe, mMineRed;
	private TextView mTextChats, mTextContact, mTextFind, mTextMe;
	private DragPointView mUnreadNumView;
	private DragPointView mFriendsNumView;
	private ImageView mSearchImageView;
	private ConversationFragment conversationFragment;
	private ContactFragment contactFragment;
	private InviteMessgeDao inviteMessgeDao;
	private long firstClick = 0;
	private long secondClick = 0;
	private BroadcastReceiver internalDebugReceiver;
	private android.app.AlertDialog.Builder conflictBuilder;
	private android.app.AlertDialog.Builder accountRemovedBuilder;
	private boolean isConflictDialogShow;
	private boolean isAccountRemovedDialogShow;
	// 账号在别处登录
	public boolean isConflict = false;
	// 账号被移除
	private boolean isCurrentAccountRemoved = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setTranslucentStatus();
		initView();
		changeTextViewColor();
		changeSelectedTabState(0);
		initMainViewPager();
	}

	@Override
	protected void initView() {
		RelativeLayout chatRLayout = (RelativeLayout) findViewById(R.id.seal_chat);
		RelativeLayout contactRLayout = (RelativeLayout) findViewById(R.id.seal_contact_list);
		RelativeLayout foundRLayout = (RelativeLayout) findViewById(R.id.seal_find);
		RelativeLayout mineRLayout = (RelativeLayout) findViewById(R.id.seal_me);
		mImageChats = (ImageView) findViewById(R.id.tab_img_chats);
		mImageContact = (ImageView) findViewById(R.id.tab_img_contact);
		mImageFind = (ImageView) findViewById(R.id.tab_img_find);
		mImageMe = (ImageView) findViewById(R.id.tab_img_me);
		mTextChats = (TextView) findViewById(R.id.tab_text_chats);
		mTextContact = (TextView) findViewById(R.id.tab_text_contact);
		mTextFind = (TextView) findViewById(R.id.tab_text_find);
		mTextMe = (TextView) findViewById(R.id.tab_text_me);
		mViewPager = (ViewPager) findViewById(R.id.main_viewpager);
		mUnreadNumView = (DragPointView) findViewById(R.id.seal_num);
		mFriendsNumView = (DragPointView) findViewById(R.id.friends_num);
		inviteMessgeDao = new InviteMessgeDao(this);
		chatRLayout.setOnClickListener(this);
		contactRLayout.setOnClickListener(this);
		foundRLayout.setOnClickListener(this);
		mineRLayout.setOnClickListener(this);
		mUnreadNumView.setOnClickListener(this);
		mUnreadNumView.setDragListencer(this);
	}
	private void initMainViewPager() {
		mFragment.add(new ConversationFragment());
		mFragment.add(new ContactFragment());
		mFragment.add(new LifeFragment());
		mFragment.add(new MeFragment());
		FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
			@Override
			public Fragment getItem(int position) {
				return mFragment.get(position);
			}

			@Override
			public int getCount() {
				return mFragment.size();
			}

			@Override
			public int getItemPosition(Object object) {
				if(mFragment!=null&&mFragment.isEmpty()&&mFragment.contains(object)){
					return POSITION_NONE;
				}
				return POSITION_UNCHANGED;
			}
		};
		mViewPager.setAdapter(fragmentPagerAdapter);
		mViewPager.setOffscreenPageLimit(4);
		mViewPager.setOnPageChangeListener(this);
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

	}

	@Override
	public void onPageSelected(int position) {
		changeTextViewColor();
		changeSelectedTabState(position);
	}

	@Override
	public void onPageScrollStateChanged(int state) {

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.seal_chat:
				if (mViewPager.getCurrentItem() == 0) {
					if (firstClick == 0) {
						firstClick = System.currentTimeMillis();
					} else {
						secondClick = System.currentTimeMillis();
					}
					Log.i("MainActivity", "time = " + (secondClick - firstClick));
					if (secondClick - firstClick > 0 && secondClick - firstClick <= 800) {
						//未读数量
						firstClick = 0;
						secondClick = 0;
					} else if (firstClick != 0 && secondClick != 0) {
						firstClick = 0;
						secondClick = 0;
					}
				}
				mViewPager.setCurrentItem(0, false);
				break;
			case R.id.seal_contact_list:
				mViewPager.setCurrentItem(1, false);
				break;
			case R.id.seal_find:
				mViewPager.setCurrentItem(2, false);
				break;
			case R.id.seal_me:
				mViewPager.setCurrentItem(3, false);
				break;
		}
	}

	@Override
	public void onDragOut() {

	}
	private void changeTextViewColor() {
		mImageChats.setBackgroundDrawable(getResources().getDrawable(R.mipmap.tab_chat));
		mImageContact.setBackgroundDrawable(getResources().getDrawable(R.mipmap.tab_contacts));
		mImageFind.setBackgroundDrawable(getResources().getDrawable(R.mipmap.tab_found));
		mImageMe.setBackgroundDrawable(getResources().getDrawable(R.mipmap.tab_me));
		mTextChats.setTextColor(Color.parseColor("#abadbb"));
		mTextContact.setTextColor(Color.parseColor("#abadbb"));
		mTextFind.setTextColor(Color.parseColor("#abadbb"));
		mTextMe.setTextColor(Color.parseColor("#abadbb"));
	}

	private void changeSelectedTabState(int position) {
		switch (position) {
			case 0:
				mTextChats.setTextColor(Color.parseColor("#0099ff"));
				mImageChats.setBackgroundDrawable(getResources().getDrawable(R.mipmap.tab_chat_hover));
				break;
			case 1:
				mTextContact.setTextColor(Color.parseColor("#0099ff"));
				mImageContact.setBackgroundDrawable(getResources().getDrawable(R.mipmap.tab_contacts_hover));
				break;
			case 2:
				mTextFind.setTextColor(Color.parseColor("#0099ff"));
				mImageFind.setBackgroundDrawable(getResources().getDrawable(R.mipmap.tab_found_hover));
				break;
			case 3:
				mTextMe.setTextColor(Color.parseColor("#0099ff"));
				mImageMe.setBackgroundDrawable(getResources().getDrawable(R.mipmap.tab_me_hover));
				break;
		}
	}

	private void isDialogshow(Bundle savedInstanceState){
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			String packageName = getPackageName();
			PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
			if (!pm.isIgnoringBatteryOptimizations(packageName)) {
				Intent intent = new Intent();
				intent.setAction(android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
				intent.setData(Uri.parse("package:" + packageName));
				startActivity(intent);
			}
		}
		//make sure activity will not in background if user is logged into another device or removed
		if (savedInstanceState != null && savedInstanceState.getBoolean(Constant.ACCOUNT_REMOVED, false)) {
			DemoHelper.getInstance().logout(false,null);
			finish();
			startActivity(new Intent(this, LoginActivity.class));
			return;
		} else if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false)) {
			finish();
			startActivity(new Intent(this, LoginActivity.class));
			return;
		}
		if (getIntent().getBooleanExtra(Constant.ACCOUNT_CONFLICT, false) && !isConflictDialogShow) {
			showConflictDialog();
		} else if (getIntent().getBooleanExtra(Constant.ACCOUNT_REMOVED, false) && !isAccountRemovedDialogShow) {
			showAccountRemovedDialog();
		}
	}
	@TargetApi(23)
	private void requestPermissions() {
		PermissionsManager.getInstance().requestAllManifestPermissionsIfNecessary(this, new PermissionsResultAction() {
			@Override
			public void onGranted() {
//				Toast.makeText(MainActivity.this, "All permissions have been granted", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onDenied(String permission) {
				//Toast.makeText(MainActivity.this, "Permission " + permission + " has been denied", Toast.LENGTH_SHORT).show();
			}
		});
	}
	/**
	 * 显示帐号在别处登录dialog
	 */
	private void showConflictDialog() {
		isConflictDialogShow = true;
		DemoHelper.getInstance().logout(false, null);
		String st = getResources().getString(R.string.Logoff_notification);
		if (!MainActivity.this.isFinishing()) {
			// clear up global variables
			try {
				if (conflictBuilder == null)
					conflictBuilder = new android.app.AlertDialog.Builder(MainActivity.this);
				conflictBuilder.setTitle(st);
				conflictBuilder.setMessage(R.string.connect_conflict);
				conflictBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						conflictBuilder = null;
						finish();
						Intent intent = new Intent(MainActivity.this, LoginActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent);
					}
				});
				conflictBuilder.setCancelable(false);
				conflictBuilder.create().show();
				isConflict = true;
			} catch (Exception e) {
				EMLog.e(TAG, "---------color conflictBuilder error" + e.getMessage());
			}

		}

	}

	/**
	 * 帐号被移除的dialog
	 */
	private void showAccountRemovedDialog() {
		isAccountRemovedDialogShow = true;
		DemoHelper.getInstance().logout(false, null);
		String st5 = getResources().getString(R.string.Remove_the_notification);
		if (!MainActivity.this.isFinishing()) {
			// clear up global variables
			try {
				if (accountRemovedBuilder == null)
					accountRemovedBuilder = new android.app.AlertDialog.Builder(MainActivity.this);
				accountRemovedBuilder.setTitle(st5);
				accountRemovedBuilder.setMessage(R.string.em_user_remove);
				accountRemovedBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						accountRemovedBuilder = null;
						finish();
						startActivity(new Intent(MainActivity.this, LoginActivity.class));
					}
				});
				accountRemovedBuilder.setCancelable(false);
				accountRemovedBuilder.create().show();
				isCurrentAccountRemoved = true;
			} catch (Exception e) {
				EMLog.e(TAG, "---------color userRemovedBuilder error" + e.getMessage());
			}

		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			moveTaskToBack(true);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void back(View view) {
		super.back(view);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putBoolean("isConflict", isConflict);
		outState.putBoolean(Constant.ACCOUNT_REMOVED, isCurrentAccountRemoved);
		super.onSaveInstanceState(outState);
	}


	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if (intent.getBooleanExtra(Constant.ACCOUNT_CONFLICT, false) && !isConflictDialogShow) {
			showConflictDialog();
		} else if (intent.getBooleanExtra(Constant.ACCOUNT_REMOVED, false) && !isAccountRemovedDialogShow) {
			showAccountRemovedDialog();
		}
	}


	@Override
	protected void onResume() {
		super.onResume();
		DemoHelper sdkHelper = DemoHelper.getInstance();
		updateUnreadLabel();
		sdkHelper.popActivity(this);

	}

	@Override
	protected void onStop() {
		DemoHelper sdkHelper = DemoHelper.getInstance();
		sdkHelper.popActivity(this);
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		if (conflictBuilder != null) {
			conflictBuilder.create().dismiss();
			conflictBuilder = null;
		}

		try {
			unregisterReceiver(internalDebugReceiver);
		} catch (Exception e) {
		}

	}
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
										   @NonNull int[] grantResults) {
		PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
	}
	public void updateUnreadLabel() {
		int count = getUnreadMsgCountTotal();
		if (count > 0) {
			mUnreadNumView.setText(String.valueOf(count));
			mUnreadNumView.setVisibility(View.VISIBLE);
		}else if(count==0){
			mUnreadNumView.setVisibility(View.GONE);
		}
	}
	/**
	 * 获取未读消息数
	 *
	 * @return
	 */
	public int getUnreadMsgCountTotal() {
		int unreadMsgCountTotal = 0;
		int chatroomUnreadMsgCount = 0;
		unreadMsgCountTotal = EMClient.getInstance().chatManager().getUnreadMsgsCount();
		for (EMConversation conversation : EMClient.getInstance().chatManager().getAllConversations().values()) {
			if (conversation.getType() == EMConversation.EMConversationType.ChatRoom)
				chatroomUnreadMsgCount = chatroomUnreadMsgCount + conversation.getUnreadMsgCount();
		}
		return unreadMsgCountTotal - chatroomUnreadMsgCount;
	}
}
