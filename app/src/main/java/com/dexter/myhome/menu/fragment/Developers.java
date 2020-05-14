package com.dexter.myhome.menu.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dexter.myhome.R;

import java.util.List;

public class Developers extends Fragment {

    private View view;
    private ImageButton drashti;
    private ImageButton avani;
    private ImageButton shyam;
    private ImageButton harsh;

    public Developers() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.about_developers, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View mview, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(mview, savedInstanceState);

        drashti = view.findViewById(R.id.insta_drashti);
        avani = view.findViewById(R.id.insta_avani);
        shyam = view.findViewById(R.id.insta_shyam);
        harsh = view.findViewById(R.id.insta_harsh);

        drashti.setOnClickListener(v -> {
            openInstagram("drashtibangoria");
        });

        avani.setOnClickListener(v -> {
            openInstagram("_avani_vanpariya_");
        });

        shyam.setOnClickListener(v -> {
            openInstagram("officialshyamkaila");
        });

        harsh.setOnClickListener(v -> {
            openInstagram("honey_chudasama");
        });

    }

    private boolean isIntentAvailable(Context ctx, Intent intent) {
        final PackageManager packageManager = ctx.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    private void openInstagram(String profileId) {
        Uri uri = Uri.parse("http://instagram.com/_u/" + profileId);
        Intent instagram = new Intent(Intent.ACTION_VIEW, uri);
        instagram.setPackage("com.instagram.android");

        if (isIntentAvailable(getActivity(), instagram)) {
            startActivity(instagram);
        } else {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/" + profileId)));
        }
    }
}
