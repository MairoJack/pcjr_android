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


public class RegistFragment extends Fragment implements View.OnClickListener
{

	private TextView login,syxy,ystk;
	private FragmentManager fragmentManager;
	private FragmentTransaction transaction;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		fragmentManager = getActivity().getSupportFragmentManager();
		transaction = fragmentManager.beginTransaction();
		return inflater.inflate(R.layout.register, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		login = (TextView) view.findViewById(R.id.login);
		syxy = (TextView) view.findViewById(R.id.syxy);
		ystk = (TextView) view.findViewById(R.id.ystk);
		login.setOnClickListener(this);
		syxy.setOnClickListener(this);
		ystk.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.login:
                transaction.setCustomAnimations(R.anim.push_right_in,R.anim.push_right_out);
				transaction.remove(this).add(R.id.id_content,new LoginFragment());
				break;
			case R.id.syxy:
				transaction.remove(this).add(R.id.id_content,new LoginFragment());
				break;
			case R.id.ystk:
				transaction.remove(this).add(R.id.id_content,new LoginFragment());
				break;
		}
        transaction.commit();
	}
}
