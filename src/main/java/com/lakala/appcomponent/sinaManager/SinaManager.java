package com.lakala.appcomponent.sinaManager;

import android.app.Activity;
import android.content.Intent;

public class SinaManager {

    public static CallBack mCallBack;

    public static CallBack mSharedCallBack;

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

        Intent intent = new Intent(activity, LoginActivity.class);
        intent.putExtra("appKey", mAppKey);
        intent.putExtra("redirectUrl", mRedirectUrl);
        activity.startActivity(intent);
    }

    public void share(final Activity activity, final SharedModel model, CallBack callBack) {
        mSharedCallBack = callBack;
//        login(activity, new CallBack() {
//            @Override
//            public void onSuccess(Object result) {
//                Intent intent = new Intent(activity, WBShareActivity.class);
//                intent.putExtra("appKey", mAppKey);
//                intent.putExtra("redirectUrl", mRedirectUrl);
//                intent.putExtra("shareData", model);
//                activity.startActivity(intent);
//            }
//
//            @Override
//            public void onFail(ErrorModel model) {
//                if (mCallBack != null) {
//                    mCallBack.onFail(model);
//                }
//            }
//        });

        Intent intent = new Intent(activity, WBShareActivity.class);
        intent.putExtra("appKey", mAppKey);
        intent.putExtra("redirectUrl", mRedirectUrl);
        intent.putExtra("shareData", model);
        activity.startActivity(intent);
    }

    public interface CallBack {
        void onSuccess(Object result);

        void onFail(ErrorModel model);
    }
}
