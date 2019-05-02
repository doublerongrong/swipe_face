public class Method {
//    //Linearlayout 置底
//
//    Your outside LinearLayout has layout_height="match_parent"
//    Your inside LinearLayout has layout_weight="1" and layout_height="0dp"
//    Your TextView has layout_weight="0"
//    You've set the gravity properly on your inner LinearLayout: android:gravity="center|bottom"
//
//    https://stackoverflow.com/questions/14779688/put-buttons-at-bottom-of-screen-with-linearlayout
//
//    //RecyclerView Divider
//    加在init RecyclerView後
//    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(groupDetailRecycleView.getContext(),
//            mgr.getOrientation());
//        groupDetailRecycleView.addItemDecoration(dividerItemDecoration);

    /****/
//    //上方導覽列 xml
//
// <LinearLayout
//    style="@style/Header"
//    android:orientation="horizontal"
//    android:background="#FFFFFF">
//
//        <LinearLayout
//    android:layout_width="match_parent"
//    android:layout_height="match_parent"
//    android:layout_weight="10"
//    android:gravity="center">
//
//            <ImageButton
//    android:id="@+id/backIBtn"
//    android:layout_width="35dp"
//    android:layout_height="35dp"
//    android:background="@drawable/ic_chevron_left_black_24dp" />
//
//
//        </LinearLayout>
//
//        <TextView
//    android:layout_width="match_parent"
//    android:layout_height="match_parent"
//    android:layout_weight="6"
//    android:gravity="center"
//    android:text="小組"
//    android:textColor="#000000"
//    style="@style/Header.TextViewSize" />
//
//        <LinearLayout
//    android:layout_width="match_parent"
//    android:layout_height="match_parent"
//    android:layout_weight="10"></LinearLayout>
//
//
//    </LinearLayout>
//
//
//    //Divider For xml
//<ImageView
//    android:layout_width="match_parent"
//    android:layout_height="wrap_content"
//    android:background="@drawable/divider"
//    android:layout_marginTop="15dp"/>
//
//
//    //DateFormat
//    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//Sdf.format(Date);
//
//
//    //抓現在使用者
//    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
//    currentFirebaseUser.getEmail();
//    String[] currentUserIdToStringList = currentFirebaseUser.getEmail().split("@");
//    currentUserId = currentUserIdToStringList[0];


}
