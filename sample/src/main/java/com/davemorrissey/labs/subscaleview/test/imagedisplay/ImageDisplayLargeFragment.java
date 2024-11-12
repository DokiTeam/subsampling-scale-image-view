package com.davemorrissey.labs.subscaleview.test.imagedisplay;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.davemorrissey.labs.subscaleview.test.R;
import com.davemorrissey.labs.subscaleview.test.R.id;
import com.davemorrissey.labs.subscaleview.test.R.layout;

public class ImageDisplayLargeFragment extends Fragment {

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(layout.imagedisplay_large_fragment, container, false);
		final ImageDisplayActivity activity = (ImageDisplayActivity) getActivity();
		if (activity != null) {
			rootView.findViewById(id.next).setOnClickListener(v -> activity.next());
		}
		SubsamplingScaleImageView imageView = rootView.findViewById(id.imageView);
		imageView.setImage(ImageSource.asset("card.png"));
		rootView.findViewById(R.id.downsampling).setOnClickListener(v -> {
			int ds = imageView.getDownSampling();
			ds = ds == 16 ? 1 : ds * 2;
			imageView.setDownSampling(ds);
			((Button) v).setText(String.valueOf(ds));
		});
		return rootView;
	}
}
