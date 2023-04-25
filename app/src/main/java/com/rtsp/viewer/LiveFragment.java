package com.rtsp.viewer;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alexvas.rtsp.widget.RtspSurfaceView;
import com.rtsp.viewer.databinding.FragmentLiveBinding;


public class LiveFragment extends Fragment {

    private static final String DEFAULT_RTSP_REQUEST = "rtsp://172.16.1.1/stream1";
    private static final String DEFAULT_RTSP_USERNAME = "";
    private static final String DEFAULT_RTSP_PASSWORD = "";
    private final int retryTime = 5;
    private FragmentLiveBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLiveBinding.inflate(inflater, container, false);

        binding.svVideo.setStatusListener(new RtspSurfaceView.RtspStatusListener() {
            @Override
            public void onRtspStatusConnecting() {
                binding.svLoading.setVisibility(View.GONE);
            }

            @Override
            public void onRtspStatusConnected() {
                binding.svLoading.setVisibility(View.GONE);
            }

            @Override
            public void onRtspStatusDisconnected() {
                binding.svLoading.setVisibility(View.VISIBLE);
                new Handler().postDelayed(() -> {
                    Uri uri = Uri.parse(DEFAULT_RTSP_REQUEST);
                    binding.svVideo.init(uri, DEFAULT_RTSP_USERNAME, DEFAULT_RTSP_PASSWORD, "rtsp-client-android");
                    binding.svVideo.setDebug(true);
                    binding.svVideo.start(true, true);
                }, retryTime * 1000L);
            }

            @Override
            public void onRtspStatusFailedUnauthorized() {
                if(getContext() == null) return;
                binding.svLoading.setVisibility(View.VISIBLE);
            }

            @Override
            public void onRtspStatusFailed(@Nullable String s) {
                if(getContext() == null) return;
                binding.svLoading.setVisibility(View.VISIBLE);
            }

            @Override
            public void onRtspFirstFrameRendered() {

            }
        });
        
        startSVVideo();

        return binding.getRoot();
    }
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(binding.svVideo.isStarted()) {
            binding.svVideo.stop();
        }
    }

    private void startSVVideo() {
        // SVVideo
        Uri uri = Uri.parse(DEFAULT_RTSP_REQUEST);
        binding.svVideo.init(uri, DEFAULT_RTSP_USERNAME, DEFAULT_RTSP_PASSWORD, "rtsp-client-android");
        binding.svVideo.setDebug(true);
        binding.svVideo.start(true, true);
    }
}