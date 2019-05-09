package com.example.kl.home;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends AppCompatActivity implements OnFragmentSelectedListener {
    private static final String TAG = "BACKFLAG";

    private TextView mTextMessage;
    private Fragment_ClassList fragment_classList;
    private Fragment_LeaveList fragment_leaveList;

    private ViewPager viewPager;

    private FragmentTransaction transaction;
    private FragmentManager fragmentManager;
    private String teacher_email;
    private String reClassId,reRollcallId,reClassDocId;
    private int fragmentRequest;
    private FirebaseFirestore db;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();//抓現在登入user

    // 設置默認進來是tab 顯示的頁面
    private void setDefaultFragment(){
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        Fragment_ClassList fragment_classList = new Fragment_ClassList();
        Bundle args = new Bundle();
        args.putString("teacher_email", teacher_email);
        Log.d(TAG,"TEST" + teacher_email);
        fragment_classList.setArguments(args);
        transaction.replace(R.id.content,new Fragment_ClassList());
        transaction.addToBackStack(new Fragment_ClassList().getClass().getName());
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
                    Fragment_ClassList fragment_classList = new Fragment_ClassList();
                    Bundle args = new Bundle();
                    args.putString("teacher_email", teacher_email);
                    Log.d(TAG,"TEST" + teacher_email);
                    fragment_classList.setArguments(args);
                    transaction.replace(R.id.content,new Fragment_ClassList());
                    transaction.addToBackStack(new Fragment_ClassList().getClass().getName());
                    transaction.commit();

                    return true;
                case R.id.navigation_leave:
                    Fragment_LeaveListN fragment_leave_list = new Fragment_LeaveListN();
                    Bundle argsLeave = new Bundle();
                    argsLeave.putString("teacher_email", teacher_email);
                    fragment_leave_list.setArguments(argsLeave);
                    transaction.replace(R.id.content,fragment_leave_list);
                    transaction.addToBackStack(fragment_leave_list.getClass().getName());
                    transaction.commit();
                    return true;
                case R.id.navigation_user:
                    Fragment_User fragment_user = new Fragment_User();
                    Bundle args2 = new Bundle();
                    args2.putString("teacher_email", teacher_email);
                    Log.d(TAG,"TEST" + teacher_email);
                    fragment_user.setArguments(args2);
                    transaction.replace(R.id.content,fragment_user);
                    transaction.addToBackStack(fragment_user.getClass().getName());
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
        teacher_email = user.getEmail();

        system system1 = new system();
//        IP
//        system1.setIp();

        Bundle bundle = this.getIntent().getExtras();
        if(bundle != null) {
            if(bundle.getString("class_id") != null){
                reClassId = bundle.getString("class_id");
                reRollcallId = bundle.getString("rollcall_id");
                reClassDocId = bundle.getString("classDoc_id");
                fragmentRequest = bundle.getInt("request");

                if (fragmentRequest == 2) {
                    gotoClassDetailFragment();
                }
            }else{
                setDefaultFragment();
            }
            fragmentRequest = bundle.getInt("request");

            if (fragmentRequest == 2) {
                reClassId = bundle.getString("class_id");
                reRollcallId = bundle.getString("rollcall_id");
                reClassDocId = bundle.getString("classDoc_id");
                gotoClassDetailFragment();
            }
//            else if(fragmentRequest == 3){
//                String class_id = bundle.getString("class_id");
//                String teacher_email = bundle.getString("teacher_email");
//                gotoClassLeaveListFragment(class_id, teacher_email);
//            } //修改假單後 導向課堂內假單

            else if(fragmentRequest == 4){
                String teacher_email = bundle.getString("teacher_email");
                gotoLeaveListFragment(teacher_email);
            } //修改假單後 導向底部欄假單


        }else{
            setDefaultFragment();
        }




        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


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
        if(fragmentKey.equals("toClassListDetail")) {
            Fragment_ClassDetail fragmentClassDetail = new Fragment_ClassDetail();
            Bundle args = new Bundle();
            args.putString("info", info);
            fragmentClassDetail.setArguments(args);
            Log.d(TAG, " MAIN");
            getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.content, fragmentClassDetail).commit();
        }//判斷是哪個fragment傳來的請求

//        else if (fragmentKey.equals("toLeaveManage")) {
//            Fragment_LeaveListClassN fragment_leaveList = new Fragment_LeaveListClassN();
//            Bundle args = new Bundle();
//            args.putString("info", info);
//            args.putString("teacher_email", teacher_email);
//            fragment_leaveList.setArguments(args);
//            Log.d(TAG, " toLeaveManage");
//            getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.content, fragment_leaveList).commit();
//        }//判斷是哪個fragment傳來的請求
        else if (fragmentKey.equals("toClassStudentList")) {
            Fragment_Class_StudentList fragment_Class_StudentList = new Fragment_Class_StudentList();
            Bundle args = new Bundle();
            args.putString("info", info);
            args.putString("teacher_email", teacher_email);
            fragment_Class_StudentList.setArguments(args);
            Log.d(TAG, " toClassStudentList");
            getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.content, fragment_Class_StudentList).commit();
        }//判斷是哪個fragment傳來的請求
        else if (fragmentKey.equals("toUserInfor")) {
            Fragment_User_Infor fragment_user_infor = new Fragment_User_Infor();
            Bundle args = new Bundle();
            args.putString("info", info);
            args.putString("teacher_email", teacher_email);
            fragment_user_infor.setArguments(args);
            Log.d(TAG, " toUserInfor");
            getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.content, fragment_user_infor).commit();
        }//判斷是哪個fragment傳來的請求
        else if (fragmentKey.equals("toUserInforSetting")) {
            Fragment_User_InforSetting fragment_user_inforsetting = new Fragment_User_InforSetting();
            Bundle args = new Bundle();
            args.putString("info", info);
            args.putString("teacher_email", teacher_email);
            fragment_user_inforsetting.setArguments(args);
            Log.d(TAG, " toUserInforSetting");
            getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.content, fragment_user_inforsetting).commit();
        }//判斷是哪個fragment傳來的請求

        else if (fragmentKey.equals("toUser")) {
            Fragment_User fragment_user = new Fragment_User();
            Bundle args = new Bundle();
            args.putString("info", info);
            args.putString("teacher_email", teacher_email);
            fragment_user.setArguments(args);
            Log.d(TAG, " toUserInforSetting");
            getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.content, fragment_user).commit();
        }//判斷是哪個fragment傳來的請求
    }//fragment傳值並換頁


    public void gotoClassDetailFragment() {    //去下载class_detail页面
        Fragment_ClassDetail fragment_classDetail = new Fragment_ClassDetail();
        Bundle args = new Bundle();
        args.putString("info", reClassDocId);
        args.putString("class_id",reClassId);
        args.putString("rollcall_id",reRollcallId);
        fragment_classDetail.setArguments(args);
        getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.content, fragment_classDetail).commit();
    }

    public void gotoLeaveListFragment(String teacher_email) {    // 改完假單回到假單介面(底部欄)
        Fragment_LeaveListN fragment_leaveList = new Fragment_LeaveListN();
        Bundle args = new Bundle();
        args.putString("teacher_email",teacher_email);
        fragment_leaveList.setArguments(args);
        getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.content, fragment_leaveList).commit();
    }


//    public void gotoClassLeaveListFragment(String class_id, String teacher_email) {    // 改完假單回到假單介面(課堂內)
//        Fragment_LeaveListN fragment_leaveList = new Fragment_LeaveListN();
//        Bundle args = new Bundle();
//        args.putString("info",class_id);
//        args.putString("teacher_email",teacher_email);
//        fragment_leaveList.setArguments(args);
//        getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.content, fragment_leaveList).commit();
//    }



}
