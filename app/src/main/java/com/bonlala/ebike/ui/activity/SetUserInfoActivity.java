package com.bonlala.ebike.ui.activity;




import com.bonlala.ebike.R;
import com.bonlala.ebike.app.AppActivity;
import com.bonlala.ebike.dialog.JumpDialogView;
import com.bonlala.ebike.http.api.user.InitUserInfoApi;
import com.bonlala.ebike.http.api.user.JumpApi;
import com.bonlala.ebike.http.api.user.LoginPresent;
import com.bonlala.ebike.http.api.user.LoginView;
import com.bonlala.ebike.http.api.user.OnGetUserInfoListener;
import com.bonlala.ebike.ui.HomeActivity;
import com.bonlala.ebike.ui.fragment.UserBasicFragment;
import com.bonlala.ebike.utils.MmkvUtils;
import com.google.gson.Gson;
import com.hjq.http.EasyHttp;
import com.hjq.toast.ToastUtils;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import timber.log.Timber;

/**
 * Created by Admin
 * Date 2022/3/26
 */
public class SetUserInfoActivity extends AppActivity implements LoginView {


    private FragmentManager fragmentManager;

    JumpDialogView jumpDialogView;

    private OnGetUserInfoListener getUserInfoListener;

    public void setGetUserInfoListener(OnGetUserInfoListener getUserInfoListener) {
        this.getUserInfoListener = getUserInfoListener;
    }

    private InitUserInfoApi.UserInfoData userInfoData;


    private LoginPresent loginPresent;

    //完善用户默认信息
    private JumpApi.DefaultUserInfo defaultUserInfo = new JumpApi.DefaultUserInfo();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_info_set_layout;
    }

    @Override
    protected void initView() {

        loginPresent = new LoginPresent();
        loginPresent.attachView(this);

        if(jumpDialogView == null)
            jumpDialogView = new JumpDialogView(this,R.style.edit_AlertDialog_style);
        if(getActivity() == null || getActivity().isFinishing())
            return;
        jumpDialogView.show();
        jumpDialogView.setCancelable(false);
        jumpDialogView.setOnJumpDialogListener(new JumpDialogView.OnJumpDialogListener() {
            @Override
            public void jumpClick() {
                jumpDialogView.dismiss();
                initDefaultInfo(0x01);
//                startActivity(HomeActivity.class);
//                finish();
            }

            @Override
            public void continueClick() {
                initDefaultInfo(0x02);
            }
        });
    }

    private void initDefaultInfo(int code){
        JumpApi.DefaultUserInfo userInfo = new JumpApi.DefaultUserInfo();
        userInfo.setSkip(true);
        String str = new Gson().toJson(userInfo);
        loginPresent.initPresentDefaultUserInfo(this,new JumpApi(),str,code);
    }


    public JumpApi.DefaultUserInfo getDefaultUserInfo(){
        return defaultUserInfo;
    }

    public void setDefaultUserInfo(JumpApi.DefaultUserInfo defaultUserInfo) {
        this.defaultUserInfo = defaultUserInfo;
    }

    @Override
    protected void initData() {

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.userFragmentLayout,UserBasicFragment.getInstance());
        fragmentTransaction.commit();

        //loginPresent.getLoginInfoData(this,new GetLoginInfoApi(),0x00);
        loginPresent.getPresentDefaultUserInfo(this,new InitUserInfoApi(),0x00);
    }



    public InitUserInfoApi.UserInfoData getUserInfoData(){
        if(userInfoData == null)
            return null;
        return userInfoData;
    }


    @Override
    public void onSuccessData(Object data, int tagCode) {
        if(data == null)
            return;
        if(tagCode == 0x00){
            InitUserInfoApi.UserInfoData tmpB = (InitUserInfoApi.UserInfoData) data;
            if(tmpB.getCadenceRegion() == null){
                userInfoData = new InitUserInfoApi.UserInfoData(tmpB.getUserId(),tmpB.getNickname());
            }else{
                 userInfoData = (InitUserInfoApi.UserInfoData) data;
            }
            Timber.e("-----用户登录的信息="+new Gson().toJson(data));
            if(getUserInfoListener != null){
                getUserInfoListener.getUserInfoData(userInfoData);

            }

        }

        if(tagCode == 0x01){
              startActivity(HomeActivity.class);
                finish();
        }


    }

    @Override
    public void onFailedData(String error) {

    }

    @Override
    public void not200CodeMsg(String msg) {
        ToastUtils.show(msg);
    }
}
