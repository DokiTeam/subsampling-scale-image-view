package com.davemorrissey.labs.subscaleview.test.imagedisplay;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.davemorrissey.labs.subscaleview.test.R;
import com.davemorrissey.labs.subscaleview.test.R.id;
import com.davemorrissey.labs.subscaleview.test.R.layout;

public class ImageDisplayAnimatedFragment extends Fragment {

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(layout.imagedisplay_large_fragment, container, false);
		final ImageDisplayActivity activity = (ImageDisplayActivity) getActivity();
		if (activity != null) {
			rootView.findViewById(id.next).setOnClickListener(v -> activity.next());
		}
		SubsamplingScaleImageView imageView = rootView.findViewById(id.imageView);
		imageView.setImage(ImageSource.Asset("animated.gif"));
		return rootView;
	}
}
