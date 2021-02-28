package com.example.deardiary;

import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

public class Fragment_Base extends Fragment {

    public void updateImage(int id, ImageView imageView) {
        Glide
                .with(this)
                .load(id)
                .into(imageView);
    }

}
