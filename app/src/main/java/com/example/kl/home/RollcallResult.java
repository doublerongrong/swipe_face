package com.example.kl.home;




import android.content.Intent;
import android.support.design.widget.TabLayout;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Bundle;
import android.support.design.widget.TabLayout;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class RollcallResult extends AppCompatActivity implements ViewPager.OnPageChangeListener,
        TabLayout.OnTabSelectedListener{
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private String classId;

    private Fragment_attend fragmentAttend = new Fragment_attend();
    private Fragment_absence fragmentAbsence = new Fragment_absence();
    private FragmentManager fragmentManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rollcall_result);

        Bundle bundle = this.getIntent().getExtras();
        classId = bundle.getString("class_id");
        Log.i("classid:",classId);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout)findViewById(R.id.tabLayout);


        //註冊監聽
        viewPager.addOnPageChangeListener(this);
        tabLayout.addOnTabSelectedListener(this);



        //添加適配器，在viewPager裡引入Fragment
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        Fragment_attend fragment_attend = new Fragment_attend();
                        Log.d("classIdddd",classId);
                        Bundle bundlecall = new Bundle();
                        bundlecall.putString("class_id", classId);
                        fragment_attend.setArguments(bundlecall);

                        fragmentManager = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction =fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.content2,fragment_attend);
                        fragmentTransaction.commit();
                        //---------------------
                                //作者：vrinux
                        //来源：CSDN
                        //原文：https://blog.csdn.net/vrinux/article/details/44086649
                    //版权声明：本文为博主原创文章，转载请附上博文链接！
                        return fragmentAttend;
                    case 1:
                        return fragmentAbsence;

                }
                return null;
            }

            @Override
            public int getCount() {
                return 2;
            }
        });


    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        //TabLayout裡的TabItem被選中的時候觸發
        viewPager.setCurrentItem(tab.getPosition());

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        //viewPager滑動之後顯示觸發
        tabLayout.getTabAt(position).select();

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
