package com.pcjr.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.pcjr.R;
import com.pcjr.activity.AgreementActivity;
import com.pcjr.activity.PrivacyPolicyActivity;
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
import retrofit2.http.Path;

/**
 * 注册
 * Created by Mario on 2016/6/1.
 */
public class RegistFragment extends Fragment implements View.OnClickListener,Validator.ValidationListener
{

    public static final String TAG = RegistFragment.class.getSimpleName();

	private TextView login,syxy,ystk;
	private FragmentManager fragmentManager;
	private FragmentTransaction transaction;

    @NotEmpty(message="用户名不能为空")
    private EditText text_username;
    @NotEmpty(message="密码不能为空")
    @Password
    private EditText text_password;
    @NotEmpty(message="确认密码不能为空")
    @ConfirmPassword(message="两次密码不同")
    private EditText text_confirm_password;
    private EditText text_recommend;

    private Button but_regist;
    private Validator validator;
    private ProgressDialog dialog;

    private PersonFragment personFragment;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		fragmentManager = getActivity().getSupportFragmentManager();
		transaction = fragmentManager.beginTransaction();
        validator = new Validator(this);
        validator.setValidationListener(this);
		return inflater.inflate(R.layout.register, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
        personFragment = (PersonFragment) getParentFragment();
        dialog = new ProgressDialog(getActivity(), ProgressDialog.STYLE_SPINNER);
		login = (TextView) view.findViewById(R.id.login);
		syxy = (TextView) view.findViewById(R.id.syxy);
		ystk = (TextView) view.findViewById(R.id.ystk);

        text_username = (EditText) view.findViewById(R.id.username);
        text_password = (EditText) view.findViewById(R.id.password);
        text_confirm_password = (EditText) view.findViewById(R.id.text_confirm_password);
        text_recommend = (EditText) view.findViewById(R.id.txt_recommend);

		login.setOnClickListener(this);
		syxy.setOnClickListener(this);
		ystk.setOnClickListener(this);

        but_regist = (Button) view.findViewById(R.id.registbtn);
        but_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();
            }
        });
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.login:
                personFragment.getNavigator().showFragment(0,false,true);
				break;
			case R.id.syxy:
				startActivity(new Intent(getActivity(), AgreementActivity.class));
                getActivity().overridePendingTransition(R.anim.slide_up_in, R.anim.slide_up_out);
				break;
			case R.id.ystk:
                startActivity(new Intent(getActivity(), PrivacyPolicyActivity.class));
                getActivity().overridePendingTransition(R.anim.slide_up_in, R.anim.slide_up_out);
				break;
		}
	}

    @Override
    public void onValidationSucceeded() {
        String username = text_username.getText().toString().trim();
        String password = text_password.getText().toString().trim();
        String recommend = text_recommend.getText().toString().trim();
        ApiService service = RetrofitUtils.createApi(ApiService.class);
        Call<JsonObject> call = service.register(username,password,recommend);
        dialog.setMessage("正在提交...");
        dialog.show();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject json = response.body();
                    if (json.get("success").getAsBoolean()) {
                        TSnackbar snackbar = TSnackbar.make(getView(),json.get("message").getAsString(), TSnackbar.LENGTH_SHORT);
                        ColoredSnackbar.warning(snackbar).show();
                        personFragment.getNavigator().showFragment(0);
                    } else {
                        TSnackbar snackbar = TSnackbar.make(getView(),json.get("message").getAsString(), TSnackbar.LENGTH_SHORT);
                        ColoredSnackbar.warning(snackbar).show();
                    }
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getContext(),"网络异常",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
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
        return new RegistFragment();
    }
}
