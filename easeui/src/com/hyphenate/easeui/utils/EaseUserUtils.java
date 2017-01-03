package com.hyphenate.easeui.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.controller.EaseUI.EaseUserProfileProvider;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.widget.EaseImageView;

public class EaseUserUtils {
    
    static EaseUserProfileProvider userProvider;
    
    static {
        userProvider = EaseUI.getInstance().getUserProfileProvider();
    }
    
    /**
     * get EaseUser according username
     * @param username
     * @return
     */
    public static EaseUser getUserInfo(String username){
        if(userProvider != null)
            return userProvider.getUser(username);
        
        return null;
    }
    
    /**
     * set user avatar
     * @param username
     */
    public static void setUserAvatar(Context context, String username, ImageView imageView){
    	EaseUser user = getUserInfo(username);
        if(user != null && user.getAvatar() != null){
            try {
                int avatarResId = Integer.parseInt(user.getAvatar());
                Glide.with(context).load(avatarResId).into(imageView);
            } catch (Exception e) {
                //use default avatar
                Glide.with(context).load(user.getAvatar()).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.ease_default_avatar).into(imageView);
            }
        }else{
            Glide.with(context).load(R.drawable.ease_default_avatar).into(imageView);
        }
    }

    /**
     * set user's nickname
     */
    public static void setUserNick(String username,TextView textView){
        if(textView != null){
        	EaseUser user = getUserInfo(username);
        	if(user != null && user.getNick() != null){
        		textView.setText(user.getNick());
        	}else{
        		textView.setText(username);
        	}
        }
    }
  private  class GlideCircleTransform extends BitmapTransformation {
      public GlideCircleTransform(Context context) {
          super(context);
      }

      @Override
      protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
          return circleCrop(pool, toTransform);
      }

      private Bitmap circleCrop(BitmapPool pool, Bitmap toTransform) {
          if (toTransform == null) return null;
          int size = Math.min(toTransform.getWidth(), toTransform.getHeight());
          int x = (toTransform.getWidth() - size) / 2;
          int y = (toTransform.getHeight() - size) / 2;

          // TODO this could be acquired from the pool too
          Bitmap squared = Bitmap.createBitmap(toTransform, x, y, size, size);

          Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
          if (result == null) {
              result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
          }

          Canvas canvas = new Canvas(result);
          Paint paint = new Paint();
          paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
          paint.setAntiAlias(true);
          float r = size / 2f;
          canvas.drawCircle(r, r, r, paint);
          return result;
      }

      @Override
      public String getId() {
          return getClass().getName();
      }
    }

}
