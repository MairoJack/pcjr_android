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
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.pcjr.R;

import java.util.List;


public class ForgetFragment extends Fragment implements View.OnClickListener,Validator.ValidationListener
{

	private TextView login,tel;
	private FragmentManager fragmentManager;
	private FragmentTransaction transaction;

	@NotEmpty(message="用户名不能为空")
	private EditText text_username;
	@NotEmpty(message="手机号不能为空")
	private EditText text_phone;
	@NotEmpty(message="验证码不能为空")
	private EditText text_checkcode;

	private Button but_send;
	private Button but_find;

	private Validator validator;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		fragmentManager = getActivity().getSupportFragmentManager();
		transaction = fragmentManager.beginTransaction();
		validator = new Validator(this);
		validator.setValidationListener(this);
		return inflater.inflate(R.layout.forget, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		login = (TextView) view.findViewById(R.id.login);
		tel = (TextView) view.findViewById(R.id.tel);

        text_username = (EditText) view.findViewById(R.id.username);
        text_phone = (EditText) view.findViewById(R.id.phone);
        text_checkcode = (EditText) view.findViewById(R.id.checkcode);

		login.setOnClickListener(this);

        but_send = (Button) view.findViewById(R.id.sendbtn);
        but_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();
            }
        });

        but_find = (Button) view.findViewById(R.id.findbtn);
        but_find.setOnClickListener(new View.OnClickListener() {
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
                transaction.setCustomAnimations(R.anim.slide_down_in,R.anim.slide_down_out);
                transaction.remove(this).add(R.id.id_content, new LoginFragment());
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
