package com.pcjr.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidadvance.topsnackbar.TSnackbar;
import com.google.gson.JsonObject;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.pcjr.R;
import com.pcjr.common.Constant;
import com.pcjr.plugins.ColoredSnackbar;
import com.pcjr.plugins.FragmentNavigator;
import com.pcjr.service.ApiService;
import com.pcjr.utils.RetrofitUtils;
import com.pcjr.utils.SharedPreferenceUtil;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginFragment extends Fragment implements View.OnClickListener, Validator.ValidationListener {

    public static final String TAG = LoginFragment.class.getSimpleName();

    private TextView reg, forget;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    @NotEmpty(message = "用户名不能为空")
    private EditText text_username;
    @NotEmpty(message = "密码不能为空")
    private EditText text_password;

    private Button but_login;

    private Validator validator;

    private ProgressDialog dialog;

    private PersonFragment personFragment;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentManager = getActivity().getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        validator = new Validator(this);
        validator.setValidationListener(this);

        return inflater.inflate(R.layout.login, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        personFragment = (PersonFragment) getParentFragment();
        dialog = new ProgressDialog(getActivity(), ProgressDialog.STYLE_SPINNER);
        reg = (TextView) view.findViewById(R.id.reg);
        forget = (TextView) view.findViewById(R.id.forget);
        text_username = (EditText) view.findViewById(R.id.username);
        text_password = (EditText) view.findViewById(R.id.password);

        but_login = (Button) view.findViewById(R.id.loginbtn);
        but_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();
            }
        });
        reg.setOnClickListener(this);
        forget.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reg:
                personFragment.getNavigator().showFragment(1,true,true);
                break;
            case R.id.forget:
                personFragment.getNavigator().showFragment(2,true,true);
                break;
        }
    }

    @Override
    public void onValidationSucceeded() {
        final String username = text_username.getText().toString().trim();
        final String password = text_password.getText().toString().trim();
        ApiService service = RetrofitUtils.createApi(ApiService.class);
        Call<JsonObject> call = service.getAccessToken("password",username, password, "1", "123");
        dialog.setMessage("正在登录...");
        dialog.show();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject json = response.body();
                    if (json.get("access_token") != null) {
                        SharedPreferenceUtil spu = new SharedPreferenceUtil(getContext(),Constant.FILE);
                        String accessToken = json.get("access_token").getAsString();
                        String refreshToken = json.get("refresh_token").getAsString();
                        Constant.access_token = accessToken;
                        Constant.isLogin = true;
                        spu.setUsername(username);
                        spu.setPassword(password);
                        spu.setAccessToken(accessToken);
                        spu.setRefresToken(refreshToken);
                        spu.setIsFirst(false);
                        dialog.dismiss();
                        personFragment.getNavigator().showFragment(3,true,true);
                    } else {
                        //Snackbar.make(getView(),"dsds",Snackbar.LENGTH_SHORT).show();
                        Toast.makeText(getActivity(), json.get("status_code").toString() + ":" + json.get("message").toString(), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    dialog.dismiss();
                    TSnackbar snackbar = TSnackbar.make(getView(),"用户名或密码错误", TSnackbar.LENGTH_SHORT);
                    ColoredSnackbar.warning(snackbar).show();
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getContext(),"网络异常",Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(getActivity());

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
            }
        }
    }

    public static Fragment newInstance(String text) {
        return new LoginFragment();
    }
}
