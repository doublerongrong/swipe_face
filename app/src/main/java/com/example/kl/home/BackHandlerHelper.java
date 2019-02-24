package com.example.kl.home;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import java.util.List;

public class BackHandlerHelper {

    /**
     * 將back事件分發給 FragmentManager 中管理的子Fragment，如果該 FragmentManager 中的所有Fragment都
     * 沒有處理back事件，則嘗試 FragmentManager.popBackStack()
     *
     * @return 如果處理了back鍵則返回 <b>true</b>
     * @see #handleBackPress(Fragment)
     * @see #handleBackPress(FragmentActivity)
     */
    public static boolean handleBackPress(FragmentManager fragmentManager) {
        List<Fragment> fragments = fragmentManager.getFragments();

        if (fragments == null) return false;

        for (int i = fragments.size() - 1; i >= 0; i--) {
            Fragment child = fragments.get(i);

            if (isFragmentBackHandled(child)) {
                return true;
            }
        }

        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
            return true;
        }
        return false;
    }

    public static boolean handleBackPress(Fragment fragment) {
        return handleBackPress(fragment.getChildFragmentManager());
    }

    public static boolean handleBackPress(FragmentActivity fragmentActivity) {
        return handleBackPress(fragmentActivity.getSupportFragmentManager());
    }

    /**
     * 判斷Fragment是否處理了Back鍵
     *
     * @return 如果處理了back鍵則返回 <b>true</b>
     */
    public static boolean isFragmentBackHandled(Fragment fragment) {
        return fragment != null
                && fragment.isVisible()
                && fragment.getUserVisibleHint() //for ViewPager
                && fragment instanceof FragmentBackHandler
                && ((FragmentBackHandler) fragment).onBackPressed();
    }
}//fragment返回鍵相關

