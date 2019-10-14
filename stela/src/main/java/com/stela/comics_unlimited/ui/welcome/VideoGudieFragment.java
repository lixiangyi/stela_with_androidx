package com.stela.comics_unlimited.ui.welcome;

import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stela.comics_unlimited.R;
import com.stela.comics_unlimited.widget.GuideVideoView;

public class VideoGudieFragment extends Fragment {

    private GuideVideoView gvvPlayer;

    public VideoGudieFragment() {

    }
    public static VideoGudieFragment getInstance(int index) {
        VideoGudieFragment fragment = new VideoGudieFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("index", index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.stela_guide_fragment_layout, container, false);
        gvvPlayer = (GuideVideoView) view.findViewById(R.id.gvv_player);
        int index = getArguments().getInt("index");
        Uri uri;
        if (index == 1) { //这里的地址是指向本地的视频资源
            uri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.step1);
        } else if (index == 2) {
            uri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.step2);
        } else  if (index == 3){
            uri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.step3);
        }else {
            uri =  Uri.parse("");
        }
        gvvPlayer.playVideo(uri);//播放
        return view;
    }

}
