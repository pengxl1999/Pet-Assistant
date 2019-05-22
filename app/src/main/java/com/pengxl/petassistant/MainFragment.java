package com.pengxl.petassistant;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class MainFragment extends Fragment {

    private View layout;
    private ViewPager viewPager;
    private ImageTextButton info, communication, music, sport, status, position, fence, others;
    private ImageButton baike, problem;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.page_main_home, container, false);
        init();
        setPage();      //设置滚动页面
        return layout;
    }

    private void init() {
        info = (ImageTextButton) layout.findViewById(R.id.info);
        communication = (ImageTextButton) layout.findViewById(R.id.communication);
        music = (ImageTextButton) layout.findViewById(R.id.music);
        sport = (ImageTextButton) layout.findViewById(R.id.sport);
        status = (ImageTextButton) layout.findViewById(R.id.status);
        position = (ImageTextButton) layout.findViewById(R.id.position);
        fence = (ImageTextButton) layout.findViewById(R.id.fence);
        others = (ImageTextButton) layout.findViewById(R.id.others);
        baike = (ImageButton) layout.findViewById(R.id.baike);
        problem = (ImageButton) layout.findViewById(R.id.problem);

        /* setOnClickListener! */
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PetInfomationActivity.class);
                startActivity(intent);
            }
        });
        sport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SportActivity.class);
                startActivity(intent);
            }
        });
        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CollarStatusActivity.class);
                startActivity(intent);
            }
        });
        position.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LocationActivity.class);
                startActivity(intent);
            }
        });
        fence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), GeoFenceActivity.class);
                startActivity(intent);
            }
        });
        baike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), BaikeActivity.class);
                startActivity(intent);
            }
        });
        problem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ProblemActivity.class);
                startActivity(intent);
            }
        });

        /* setImage */
        info.setImgResource(R.drawable.info);
        communication.setImgResource(R.drawable.communicate);
        music.setImgResource(R.drawable.music);
        sport.setImgResource(R.drawable.sport);
        status.setImgResource(R.drawable.status);
        position.setImgResource(R.drawable.position);
        fence.setImgResource(R.drawable.fence);
        others.setImgResource(R.drawable.others);


        /* setText */
        info.setText("宠物信息");
        communication.setText("宠物交流");
        music.setText("播放音乐");
        sport.setText("宠物运动量");
        status.setText("查看项圈状态");
        position.setText("宠物当前位置");
        fence.setText("设置地理围栏");
        others.setText("其他");
    }

    private void setPage() {
        viewPager = (ViewPager) layout.findViewById(R.id.viewpager);
        ArrayList<View> viewList = new ArrayList<>();

        LinearLayout linearLayout = (LinearLayout) layout.findViewById(R.id.line1);
        LayoutInflater layoutInflater = getLayoutInflater();
        for(int i = 1;i <= 3; i++) {
            View view = layoutInflater.inflate(R.layout.scroll_page_item, linearLayout, false);
            ImageButton imageButton = (ImageButton) view.findViewById(R.id.image);
            switch (i) {
                case 1:
                    imageButton.setBackgroundResource(R.drawable.scroll_page_1);
                    break;
                case 2:
                    imageButton.setBackgroundResource(R.drawable.scroll_page_2);
                    break;
                case 3:
                    imageButton.setBackgroundResource(R.drawable.scroll_page_3);
                    break;
                default:
                    break;
            }
            viewList.add(view);
        }
        viewPager.setAdapter(new ScrollPageAdapter(viewList));
    }
}
