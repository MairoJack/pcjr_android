package com.pcjr.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
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

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.pcjr.R;

import java.util.List;


public class LoginFragment extends Fragment implements View.OnClickListener,Validator.ValidationListener
{

	private TextView reg,forget;
	private FragmentManager fragmentManager;
	private FragmentTransaction transaction;
    @NotEmpty(message="用户名不能为空")
    private EditText text_username;
    @NotEmpty(message="密码不能为空")
    private EditText text_password;
    @NotEmpty(message="验证码不能为空")
    private EditText text_checkcode;

    private Button but_login;

    private Validator validator;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		fragmentManager = getActivity().getSupportFragmentManager();
		transaction = fragmentManager.beginTransaction();
        validator = new Validator(this);
        validator.setValidationListener(this);
		return inflater.inflate(R.layout.login, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		reg = (TextView) view.findViewById(R.id.reg);
		forget = (TextView) view.findViewById(R.id.forget);
        text_username = (EditText) view.findViewById(R.id.username);
        text_password = (EditText) view.findViewById(R.id.password);
        text_checkcode = (EditText) view.findViewById(R.id.checkcode);

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
		switch (v.getId()){
			case R.id.reg:
                transaction.setCustomAnimations(R.anim.push_left_in,R.anim.push_left_out);
				transaction.remove(this).add(R.id.id_content,new RegistFragment());
				break;
			case R.id.forget:
                transaction.setCustomAnimations(R.anim.slide_up_in,R.anim.slide_up_out);
				transaction.remove(this).add(R.id.id_content,new ForgetFragment());
				break;
		}
        transaction.commit();
	}

    @Override
    public void onValidationSucceeded() {
        Toast.makeText(getActivity(), "Yay! we got it right!", Toast.LENGTH_SHORT).show();
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
}
