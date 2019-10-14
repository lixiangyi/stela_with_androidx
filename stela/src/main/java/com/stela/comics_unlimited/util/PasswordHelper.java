package com.stela.comics_unlimited.util;

import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import com.etiennelawlor.trestle.library.Span;
import com.etiennelawlor.trestle.library.Trestle;
import com.lxy.baselibs.utils.StringUtils;
import com.stela.comics_unlimited.R;

import java.util.ArrayList;
import java.util.List;

public class PasswordHelper {


    public static boolean initPassword(TextView tvErrorInfo, String password) {

        List<Span> spansPasswordError = new ArrayList<>();
        spansPasswordError.add(new Span.Builder("Minimum 6 characters,")
                .foregroundColor(tvErrorInfo.getContext(), password.length() < 6 ? R.color.red : R.color.color_73)
                .build());
        //【含有大写英文】true
        String regexUp = ".*[A-Z].*";
        spansPasswordError.add(new Span.Builder("1 Uppercase,")
                .foregroundColor(tvErrorInfo.getContext(), !password.matches(regexUp) ? R.color.red : R.color.color_73)
                .build());
        //【含有小写英文】true
        String regexlow = ".*[a-z].*";
        spansPasswordError.add(new Span.Builder("1 lowercase,")
                .foregroundColor(tvErrorInfo.getContext(), !password.matches(regexlow) ? R.color.red : R.color.color_73)
                .build());
        //【含有数字】true
        String regexNum = ".*[0-9].*";
        spansPasswordError.add(new Span.Builder("1 number")
                .foregroundColor(tvErrorInfo.getContext(), !password.matches(regexNum) ? R.color.red : R.color.color_73)
                .build());
        CharSequence formattedText2 = Trestle.getFormattedText(spansPasswordError);
        tvErrorInfo.setMovementMethod(LinkMovementMethod.getInstance());
        tvErrorInfo.setText(formattedText2);
        if (TextUtils.isEmpty(password) || !StringUtils.verifyPassword(password)) {
            return true;
        }
        return false;
    }

    public static String setPasswordSecret(String password) {
        try {
            password = RsaUtils.encrypt(password.getBytes(),
                    RsaUtils.loadPublicKey(RsaUtils.publicKey));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return password;
    }
}
