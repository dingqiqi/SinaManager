package com.lakala.appcomponent.sinaManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.sina.weibo.sdk.utils.Utility;

public class WBShareActivity extends Activity implements WbShareCallback {

    private WbShareHandler mWbShareHandler;

    private SharedModel mSharedModel;

    /**
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //目前第三方分享微博需要客户端的支持，请确保已安装官方微博客户端
        if (!WbSdk.isWbInstall(this)) {
            Toast.makeText(this, "请安装微博客户端!", Toast.LENGTH_SHORT).show();
            if (SinaManager.mSharedCallBack != null) {
                SinaManager.mSharedCallBack.onFail(new ErrorModel("-1", "请安装微博客户端"));
            }
            finish();
            return;
        }

        String mAppKey = getIntent().getStringExtra("appKey");
        String mRedirectUrl = getIntent().getStringExtra("redirectUrl");
        mSharedModel = getIntent().getParcelableExtra("shareData");

        if (mSharedModel == null) {
            mSharedModel = new SharedModel();
        }

        Context context = getApplicationContext();

        AuthInfo mAuthInfo = new AuthInfo(context, mAppKey, mRedirectUrl, "");
        WbSdk.install(context, mAuthInfo);


        mWbShareHandler = new WbShareHandler(this);
        mWbShareHandler.registerApp();
        mWbShareHandler.setProgressColor(0xff33b5e5);

        sendMultiMessage();
    }

//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        mWbShareHandler.doResultIntent(intent, this);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mWbShareHandler.doResultIntent(data, this);
    }


    /**
     * 第三方应用发送请求消息到微博，唤起微博分享界面。
     */
    private void sendMultiMessage() {
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();

        weiboMessage.textObject = getTextObj();

        weiboMessage.imageObject = getImageObj();

        mWbShareHandler.shareMessage(weiboMessage, false);
    }


    @Override
    public void onWbShareSuccess() {
        if (SinaManager.mSharedCallBack != null) {
            SinaManager.mSharedCallBack.onSuccess(true);
        }
        finish();
    }

    @Override
    public void onWbShareFail() {
        if (SinaManager.mSharedCallBack != null) {
            SinaManager.mSharedCallBack.onFail(new ErrorModel("-1", "分享失败"));
        }
        finish();
    }

    @Override
    public void onWbShareCancel() {
        finish();
    }

    /**
     * 获取分享的文本模板。
     */
    private String getSharedText() {
        return "这是一个很漂亮的小狗，朕甚是喜欢-_-!! #http://www.baidu.com";
    }

    /**
     * 创建文本消息对象。
     *
     * @return 文本消息对象。
     */
    private TextObject getTextObj() {
        TextObject textObject = new TextObject();
        textObject.text = mSharedModel.getText();
        textObject.title = "";
        textObject.actionUrl = "";
        return textObject;
    }

    /**
     * 创建图片消息对象。
     *
     * @return 图片消息对象。
     */
    private ImageObject getImageObj() {
        ImageObject imageObject = new ImageObject();

        String url = mSharedModel.getImageUrl();
        Bitmap bitmap;
        if (TextUtils.isEmpty(url)) {
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.share_default_icon);
        } else {
            bitmap = BitmapFactory.decodeFile(url);
        }

        imageObject.setImageObject(bitmap);
        return imageObject;
    }

    /**
     * 创建多媒体（网页）消息对象。
     *
     * @return 多媒体（网页）消息对象。
     */
    private WebpageObject getWebPageObj() {
        //https://github.com/sinaweibosdk/weibo_android_sdk  不让用了
        WebpageObject mediaObject = new WebpageObject();
        mediaObject.identify = Utility.generateGUID();
        mediaObject.title = "测试title";
        mediaObject.description = "测试描述";
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.share_default_icon);
        // 设置 Bitmap 类型的图片到视频对象里         设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
        mediaObject.setThumbImage(bitmap);
        mediaObject.actionUrl = "http://news.sina.com.cn/c/2013-10-22/021928494669.shtml";
        mediaObject.defaultText = "Webpage 默认文案";
        return mediaObject;
    }
}
