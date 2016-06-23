package com.pcjinrong.pcjr.plugins;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pcjinrong.pcjr.model.FocusImg;
import com.pcjinrong.pcjr.R;

/**
 * Created by Mario on 16/6/19.
 */
public class NetworkImageHolderView implements Holder<FocusImg> {
    private ImageView imageView;
    @Override
    public View createView(Context context) {
        imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    }

    @Override
    public void UpdateUI(Context context,int position, FocusImg data) {
        imageView.setImageResource(R.drawable.ic_default_adimage);
        ImageLoader.getInstance().displayImage(data.getImg_url(),imageView);
    }
}