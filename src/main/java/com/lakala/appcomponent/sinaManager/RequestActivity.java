package com.lakala.appcomponent.sinaManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbAuthListener;
import com.sina.weibo.sdk.auth.WbConnectErrorMessage;
import com.sina.weibo.sdk.auth.sso.SsoHandler;

import java.util.HashMap;
import java.util.Map;

public class RequestActivity extends Activity {

    private SsoHandler mSsoHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String mAppKey = getIntent().getStringExtra("appKey");
        String mRedirectUrl = getIntent().getStringExtra("redirectUrl");

        Context context = getApplicationContext();

        AuthInfo mAuthInfo = new AuthInfo(context, mAppKey, mRedirectUrl, "");
        WbSdk.install(context, mAuthInfo);

        mSsoHandler = new SsoHandler(this);
        mSsoHandler.authorize(new SelfWbAuthListener());
    }

    private class SelfWbAuthListener implements WbAuthListener {
        @Override
        public void onSuccess(Oauth2AccessToken token) {
            Map<String, String> map = new HashMap<>();
            if (token.isSessionValid()) {
                map.put("code", token.getToken());
                map.put("phoneNum", token.getPhoneNum());
                map.put("refreshToken", token.getRefreshToken());
                map.put("uid", token.getUid());
                map.put("expiresTime", String.valueOf(token.getExpiresTime()));

                if (SinaManager.mCallBack != null) {
                    SinaManager.mCallBack.onSuccess(map);
                }
            } else {
                if (SinaManager.mCallBack != null) {
                    ErrorModel model = new ErrorModel();
                    model.setCode("-1");
                    model.setMsg("token已失效");
                    SinaManager.mCallBack.onFail(model);
                }
            }

            finish();
        }

        @Override
        public void cancel() {
            finish();
        }

        @Override
        public void onFailure(WbConnectErrorMessage errorMessage) {
            if (SinaManager.mCallBack != null) {
                ErrorModel model = new ErrorModel();
                model.setCode(errorMessage.getErrorCode());
                model.setMsg(errorMessage.getErrorMessage());
                SinaManager.mCallBack.onFail(model);
            }
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }
}
