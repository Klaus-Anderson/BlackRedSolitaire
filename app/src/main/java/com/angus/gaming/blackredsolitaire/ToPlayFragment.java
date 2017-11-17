package com.angus.gaming.blackredsolitaire;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ToPlayFragment extends Fragment {

	public ToPlayFragment() {
		// TODO Auto-generated constructor stub
	}

	OnValuesSetListener mCallback;

	// Container Activity must implement this interface
	public interface OnValuesSetListener {
		public void onValuesSet();
        public void fragmentManager();
		public void finishFragment();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// This makes sure that the container activity has implemented
		// the callback interface. If not, it throws an exception
		try {
			mCallback = (OnValuesSetListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnHeadlineSelectedListener");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_toplay, container,
				false);
		Button backButton = (Button) rootView.findViewById(R.id.backButton);
		backButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				mCallback.fragmentManager();
                ToPlayFragment.this.mCallback.finishFragment();
			}
			
		});
		
		mCallback.onValuesSet();

		return rootView;
	}

}
