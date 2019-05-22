package com.pengxl.petassistant;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

public class UserFragment extends Fragment {

    private View layout;
    private TextView userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.page_main_user, container, false);
        init();
        return layout;
    }

    private void init() {
        userId = layout.findViewById(R.id.user_id);

        String s = "当前用户：" + StaticMember.account;
        userId.setText(s);
    }
}
