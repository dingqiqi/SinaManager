package com.lakala.appcomponent.sinaManager;

import android.app.Activity;
import android.content.Intent;

public class SinaManager {

    public static CallBack mCallBack;

    private String mAppKey;
    private String mRedirectUrl;

    /**
     * 初始化
     *
     * @param mAppKey     appKey
     * @param redirectUrl 授权回调地址
     */
    public SinaManager(String mAppKey, String redirectUrl) {
        this.mAppKey = mAppKey;
        this.mRedirectUrl = redirectUrl;
    }

    public void login(Activity activity, CallBack callBack) {
        mCallBack = callBack;

        Intent intent = new Intent(activity, RequestActivity.class);
        intent.putExtra("appKey", mAppKey);
        intent.putExtra("redirectUrl", mRedirectUrl);
        activity.startActivity(intent);
    }

    public interface CallBack {
        void onSuccess(Object result);

        void onFail(ErrorModel model);
    }
}
