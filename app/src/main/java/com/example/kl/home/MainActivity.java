package com.example.kl.home;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends AppCompatActivity implements Fragment_ClassList.OnFragmentSelectedListener {
    private static final String TAG = "BACKFLAG";

    private TextView mTextMessage;
    private Fragment_ClassList fragment_classList;
    private Fragment_LeaveList fragment_leaveList;

    private ViewPager viewPager;

    private FragmentTransaction transaction;
    private FragmentManager fragmentManager;

    // 設置默認進來是tab 顯示的頁面
    private void setDefaultFragment(){
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content,new Fragment_ClassList());
        transaction.commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            fragmentManager = getSupportFragmentManager();
            transaction = fragmentManager.beginTransaction();

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    transaction.replace(R.id.content,new Fragment_ClassList());
                    transaction.addToBackStack(new Fragment_ClassList().getClass().getName());
                    transaction.commit();

                    return true;
                case R.id.navigation_leave:
                    transaction.replace(R.id.content,new Fragment_LeaveList());
                    transaction.addToBackStack(new Fragment_LeaveList().getClass().getName());
                    transaction.commit();
                    return true;
                case R.id.navigation_user:
                    transaction.replace(R.id.content,new Fragment_User());
                    transaction.addToBackStack(new Fragment_User().getClass().getName());
                    transaction.commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t_activity_homepage);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        setDefaultFragment();

        //呼叫Permission
        MainActivityPermissionsDispatcher.AllPermissionsWithPermissionCheck(this);
    }

    //Permission
    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void AllPermissions() {
    }
    //Permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    public void onBackPressed() {
        int count = getFragmentManager().getBackStackEntryCount();

//        if (count == 0) {
//            super.onBackPressed();
//        } else {
//            getFragmentManager().popBackStack();
//        }
        if (!BackHandlerHelper.handleBackPress(this)) {
            super.onBackPressed();
            Log.d(TAG, "ERROR");
        } else {
            Log.d(TAG, "success");
        }
    }//fragment退回鍵

    @Override
    public void onFragmentSelected(String info ,String fragmentKey) {
        if(fragmentKey.equals("ClassList")) {
            FragmentClassDetail fragmentClassDetail = new FragmentClassDetail();
            Bundle args = new Bundle();
            args.putString("info", info);
            fragmentClassDetail.setArguments(args);
            Log.d(TAG, " MAIN");
            getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.content, fragmentClassDetail).commit();
        }//判斷是哪個fragment傳來的請求
    }//fragment傳值並換頁

}
