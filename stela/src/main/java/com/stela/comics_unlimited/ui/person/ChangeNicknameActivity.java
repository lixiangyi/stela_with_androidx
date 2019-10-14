package com.stela.comics_unlimited.ui.person;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatTextView;

import com.lxy.baselibs.app.AppConstants;
import com.lxy.baselibs.rx.RxBus;
import com.lxy.baselibs.utils.DisplayUtils;
import com.lxy.baselibs.utils.StringUtils;
import com.stela.comics_unlimited.Base.BaseMvpActivity;
import com.stela.comics_unlimited.R;
import com.stela.comics_unlimited.app.DataStore;
import com.stela.comics_unlimited.data.entity.CommentsEntity;
import com.stela.comics_unlimited.data.entity.PersonEntity;
import com.stela.comics_unlimited.event.UserNotifyEvent;

import butterknife.BindView;
import butterknife.OnClick;

public class ChangeNicknameActivity extends BaseMvpActivity<ChangeNicknamePresenter> implements ChangeNicknameContract.View {

    @BindView(R.id.et_nick_name)
    EditText etNickName;
    @BindView(R.id.tv_select_nick_name)
    TextView tvSelectNickName;
    @BindView(R.id.tv_error_info)
    AppCompatTextView tvErrorInfo;
    @BindView(R.id.rl_error_info)
    RelativeLayout rlErrorInfo;
    @BindView(R.id.rl_bottom)
    RelativeLayout rlBottom;
    private int mType;
    private CommentsEntity mCommentsEntity;

    public static void start(Context context, int i, CommentsEntity commentsEntity) {
        Intent starter = new Intent(context, ChangeNicknameActivity.class);
        starter.putExtra(AppConstants.TYPE, i);
        starter.putExtra(AppConstants.COMMENTENTITY, commentsEntity);
        context.startActivity(starter);
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, ChangeNicknameActivity.class);
        context.startActivity(starter);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_change_nickname;
    }

    @Override
    protected void getIntent(Intent intent) {
        mType = intent.getIntExtra(AppConstants.TYPE, 0);
        mCommentsEntity = (CommentsEntity) intent.getSerializableExtra(AppConstants.COMMENTENTITY);
    }

    @Override
    protected void initView() {
        showBack();
        setTitle("Choose a Nickname");
        etNickName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (validate()) {
                    tvSelectNickName.setEnabled(true);
                } else {
                    tvSelectNickName.setEnabled(false);
                }
                if (TextUtils.isEmpty(etNickName.getText().toString().trim())) {
                    etNickName.setActivated(false);
                    rlErrorInfo.setVisibility(View.GONE);
                }
            }
        });
        etNickName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                etNickName.setActivated(false);
            }
        });
        etNickName.setHint(DataStore.getUserInfo().nickname);
    }

    @Override
    protected void initListener() {
        addLayoutListener(rlBottom, tvSelectNickName);
    }

    @Override
    protected void initData() {

    }

    private boolean validate() {
        String nickname = etNickName.getText().toString().trim();
        if (StringUtils.verifyNickName(nickname)) {
            return true;
        }
        return false;
    }

    @Override
    protected ChangeNicknamePresenter createPresenter() {
        return new ChangeNicknamePresenter();
    }


    @Override
    public void showError(String msg) {
        etNickName.setActivated(true);
        rlErrorInfo.setVisibility(View.VISIBLE);
        tvErrorInfo.setText(msg);
    }


    @OnClick(R.id.tv_select_nick_name)
    public void onViewClicked() {
        DisplayUtils.closeInputMethod(this, etNickName);
        new AlertDialog.Builder(this)
                .setTitle("Confirm Changes")
                .setMessage("Are you sure you want to change your nickname to [" + etNickName.getText().toString().trim() + "]? Nicknames can only be changed once per month.")
                .setPositiveButton("Yes, change it", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mPresenter.UpdataNickname(etNickName.getText().toString().trim());
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    public void updataSuccess(String s) {
        etNickName.setActivated(false);
        rlErrorInfo.setVisibility(View.GONE);
        PersonEntity userEntity = DataStore.getUserInfo();
        userEntity.nickname = s;
        userEntity.ifNickname = 1;
        DataStore.setUserInfo(userEntity);
        RxBus.getDefault().post(new UserNotifyEvent());
        if (mCommentsEntity != null) {
            ChooseAvatarActivity.start(this, mType, mCommentsEntity);
        }
        finish();
    }
}
