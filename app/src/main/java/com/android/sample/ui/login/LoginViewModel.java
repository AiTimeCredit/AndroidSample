package com.android.sample.ui.login;

import android.app.Application;
import android.os.CountDownTimer;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.android.common.http.BaseObserver;
import com.android.common.http.ResponseEntity;
import com.android.common.mvvm.BaseViewModel;
import com.android.common.utils.SafetyHandler;
import com.android.common.utils.ToastUtil;
import com.android.sample.R;
import com.android.sample.entity.Agreement;
import com.android.sample.entity.LoginRecord;
import com.android.sample.entity.VerifyCode;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.adapters.TextViewBindingAdapter;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;

/**
 * 登录ViewModel
 */
public class LoginViewModel extends BaseViewModel<LoginModel> implements SafetyHandler.Delegate {

    public ObservableField<String> phoneNum = new ObservableField<>();
    public ObservableField<String> verifyCode = new ObservableField<>();
    public ObservableField<String> verifyButtonText = new ObservableField<>();
    public ObservableField<String> actionBtnText = new ObservableField<>();
    public ObservableBoolean waitingCode = new ObservableBoolean(false);

    public ObservableBoolean buttonEnable = new ObservableBoolean();
    public ObservableBoolean isAgreementsChecked = new ObservableBoolean(true);
    public ObservableField<List<Agreement>> agreements = new ObservableField<>();
    public MutableLiveData<Boolean> finish = new MutableLiveData<>();
    public MutableLiveData<Boolean> loading = new MutableLiveData<>();
    public MutableLiveData<Boolean> hideSoftInput = new MutableLiveData<>();
    public TextViewBindingAdapter.AfterTextChanged afterTextChanged = s -> refreshButtonState();
    private SafetyHandler handler = SafetyHandler.create(this);

    private CountDownTimer countDownTimer;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        actionBtnText.set(getString(R.string.login));
        verifyButtonText.set(getString(R.string.verify_code_get));
        countDownTimer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long l) {
                verifyButtonText.set(String.format(getString(R.string.verify_code_count_down), l / 1000));
            }

            @Override
            public void onFinish() {
                waitingCode.set(false);
                verifyButtonText.set(getString(R.string.verify_code_get));
            }
        };

        // 用户协议
        List<Agreement> agreements = new ArrayList<>(2);
        agreements.add(new Agreement(getString(R.string.agreement_login), "https://www.baidu.com/"));
        agreements.add(new Agreement(getString(R.string.agreement_privacy), "https://www.baidu.com/"));
        this.agreements.set(agreements);
    }

    public void performViewClicks(View view) {
        int id = view.getId();
        if (id == R.id.login_verify_code) {                     // 获取短信验证码
            getVerifyCode();
        } else if (id == R.id.login_submit) {                   // 登录
            gotoLogin();
        }
    }

    private void getVerifyCode() {
        if (waitingCode.get()) {
            return;
        }
        String phone = phoneNum.get();
        if (phone == null || TextUtils.isEmpty(phone)) {
            Toast.makeText(getApplication(), R.string.user_auth_tips_input_phone, Toast.LENGTH_SHORT).show();
            return;
        }
        phone = phone.trim();
        if (TextUtils.isEmpty(phone) || phone.length() < 10) {
            Toast.makeText(getApplication(), R.string.user_auth_tips_error_phone, Toast.LENGTH_SHORT).show();
            return;
        }
        getModel()
                .sendSmsCode(phone)
                .subscribe(new BaseObserver<ResponseEntity<VerifyCode>>() {
                    @Override
                    protected void onStart() {
                        super.onStart();
                        loading.setValue(true);
                    }

                    @Override
                    protected void doOnNext(@NonNull ResponseEntity<VerifyCode> entity) {
                        loading.setValue(false);
                        if (entity.isSuccessful()) {
                            VerifyCode verifyCode = entity.getData();
                            if (verifyCode == null || "1".equals(verifyCode.getStatusCode())) {
                                waitingCode.set(true);
                                countDownTimer.start();
                                ToastUtil.showToast(R.string.verify_code_send_success);
                                return;
                            }
                            if ("2".equals(verifyCode.getStatusCode()) && !TextUtils.isEmpty(verifyCode.getBase64Img())) {
                                ToastUtil.showToast("需要图形验证码，请更换输入的手机号");
                            }
                        } else {
                            ToastUtil.showToast(entity.getDesc());
                        }
                    }

                    @Override
                    protected void doOnError(@NonNull Throwable t) {
                        loading.setValue(false);
                        ToastUtil.showToast(t.getMessage());
                    }
                });
    }

    /**
     * 点击登录
     */
    private void gotoLogin() {
        String phone = phoneNum.get();
        if (phone == null || phone.isEmpty()) {
            Toast.makeText(getApplication(), R.string.user_auth_tips_input_phone, Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(phone.trim()) || phone.length() < 10) {
            Toast.makeText(getApplication(), R.string.user_auth_tips_error_phone, Toast.LENGTH_SHORT).show();
            return;
        }
        phone = phone.trim();
        if (TextUtils.isEmpty(phone) || phone.length() < 10) {
            Toast.makeText(getApplication(), R.string.user_auth_tips_input_smscode, Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(verifyCode.get())) {
            ToastUtil.showToast(getString(R.string.user_auth_tips_input_smscode));
            return;
        }

        // 收起软键盘
        if (!isAgreementsChecked.get()) {
            String message = getString(R.string.agreement_message) + Agreement.getAgreementsString(getApplication(), agreements.get());
            Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show();
            return;
        }
        getModel()
                .loginWithQuick(phone, verifyCode.get())
                .subscribe(new BaseObserver<ResponseEntity<LoginRecord>>() {
                    @Override
                    protected void onStart() {
                        super.onStart();
                        loading.setValue(true);
                    }
                    @Override
                    protected void doOnNext(@NonNull ResponseEntity<LoginRecord> entity) {
                        loading.setValue(false);
                        if (entity.isSuccessful()) {
                            ToastUtil.showToast(phoneNum.get() + " " + getString(R.string.login_successful));
                            finish.setValue(true);
                        } else {
                            ToastUtil.showToast(entity.getDesc());
                        }
                    }

                    @Override
                    protected void doOnError(@NonNull Throwable t) {
                        loading.setValue(false);
                        ToastUtil.showToast(t.getMessage());
                    }
                });
    }

    @Override
    public void handleMessage(@NonNull Message msg) {

    }

    /**
     * 根据输入情况实时更新Button状态
     */
    private void refreshButtonState() {
        if (!TextUtils.isEmpty(phoneNum.get()) && !TextUtils.isEmpty(verifyCode.get())) {
            buttonEnable.set(true);
        } else {
            buttonEnable.set(false);
        }
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        super.onDestroy(owner);
        handler.clear();
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
        List<Agreement> agreements = this.agreements.get();
        if (agreements != null) {
            agreements.clear();
        }
    }
}
