package com.example.kl.home;




import android.content.Context;
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
import android.widget.Button;

public class RollcallResult extends AppCompatActivity implements ViewPager.OnPageChangeListener,
        TabLayout.OnTabSelectedListener{
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private String classId,docId,classDocId;
    private Button finishBtn;

    private Fragment_attend fragmentAttend = new Fragment_attend();
    private Fragment_absence fragmentAbsence = new Fragment_absence();
    private  Fragment_ClassDetail fragmentClassDetail = new Fragment_ClassDetail();
    private static Context mContext;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rollcall_result);

        Bundle bundle = this.getIntent().getExtras();
        classId = bundle.getString("class_id");
        docId = bundle.getString("classDoc_id");
        classDocId = bundle.getString("class_doc");
        Log.i("classid",classId);

        mContext = getApplicationContext();

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        finishBtn = (Button)findViewById(R.id.finishButton);


        //註冊監聽
        viewPager.addOnPageChangeListener(this);
        tabLayout.addOnTabSelectedListener(this);

        finishBtn.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(), MainActivity.class);
            intent.putExtra("class_id", classId);
            intent.putExtra("classDoc_id",classDocId);
            intent.putExtra("rollcall_id",docId);
            intent.putExtra("request",2);
            startActivity(intent);
            finish();
        });





        //添加適配器，在viewPager裡引入Fragment
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        Log.d("classIdddd",classId);
                        Log.d("docIdddddd",docId);
                        Bundle args = new Bundle();
                        args.putString("class_id", classId);
                        args.putString("classDoc_id",docId);
                        fragmentAttend.setArguments(args);

                        return fragmentAttend;
                    case 1:

                        Bundle args2 = new Bundle();
                        args2.putString("class_id", classId);
                        args2.putString("classDoc_id",docId);
                        fragmentAbsence.setArguments(args2);

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

    public static Context getmContext(){
        return mContext;
    }
}
