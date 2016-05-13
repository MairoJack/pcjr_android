package com.pcjr.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pcjr.R;


public class ForgetFragment extends Fragment implements View.OnClickListener
{

	private TextView login,tel;
	private FragmentManager fragmentManager;
	private FragmentTransaction transaction;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		fragmentManager = getActivity().getSupportFragmentManager();
		transaction = fragmentManager.beginTransaction();
		return inflater.inflate(R.layout.forget, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		login = (TextView) view.findViewById(R.id.login);
		tel = (TextView) view.findViewById(R.id.tel);
		login.setOnClickListener(this);
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
}
