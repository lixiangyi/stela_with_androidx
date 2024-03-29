package com.stela.comics_unlimited.util.google_login;

import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.lxy.baselibs.app.BaseApplication;
import com.lxy.baselibs.base.BaseActivity;
import com.lxy.baselibs.utils.LogUtils;

public class GoogleLoginHelper {
    private static GoogleLoginInterface googleLoginInterFace;
    private static final int RC_SIGN_IN = 10010;
    private static GoogleSignInClient mGoogleSignInClient;


    public static void init(GoogleLoginInterface mGoogleLoginInterFace) {
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(BaseApplication.getContext(), gso);
        googleLoginInterFace = mGoogleLoginInterFace;
    }

    public static void signIn(BaseActivity baseActivity) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        baseActivity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public static void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private static void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            if (googleLoginInterFace != null) {
                googleLoginInterFace.GoogleLoginSuccess(account);
            }
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            LogUtils.w("lixiangyi", "signInResult:failed code=" + e.getStatusCode());
            if (googleLoginInterFace != null) {
                googleLoginInterFace.GoogleLoginSuccess(null);
            }
        }
    }


    public  interface GoogleLoginInterface {
        void GoogleLoginSuccess(GoogleSignInAccount account);
    }
}
