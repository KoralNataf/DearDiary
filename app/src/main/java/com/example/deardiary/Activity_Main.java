package com.example.deardiary;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.hardware.input.InputManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.TableLayout;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;


public class Activity_Main extends AppCompatActivity {
    private Toolbar main_toolbar_tabs;
    private ViewPager main_view_pager;
    private TabLayout main_LAY_tab_layout;
    private Fragment_My_Meetings fragment_my_meetings;
    private Fragment_New_Meeting fragment_new_meeting;
    private Fragment_Profile fragment_profile;
    private Fragment_ToDo_List fragment_toDo_list;
    private  ViewPagerAdapter viewPagerAdapter;
    private boolean inEdit= false;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        initViews();
    }


    private void findViews() {
        main_toolbar_tabs=findViewById(R.id.main_toolbar_tabs);
        main_view_pager=findViewById(R.id.main_view_pager);
        main_LAY_tab_layout=findViewById(R.id.main_LAY_tab_layout);
    }

    private void initViews() {
        setSupportActionBar(main_toolbar_tabs);

        fragment_profile= new Fragment_Profile();
        fragment_new_meeting = new Fragment_New_Meeting();
        fragment_my_meetings = new Fragment_My_Meetings();
        fragment_toDo_list= new Fragment_ToDo_List();

        fragment_profile.setCallBack(callBack);
        fragment_new_meeting.setCallBack(callBack);

        main_LAY_tab_layout.setupWithViewPager(main_view_pager);

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),0);
        viewPagerAdapter.addFragment(fragment_profile,"User\nProfile");
        viewPagerAdapter.addFragment(fragment_new_meeting,"New\nMeeting");
        viewPagerAdapter.addFragment(fragment_my_meetings,"My\nMeetings");
        viewPagerAdapter.addFragment(fragment_toDo_list,"To-Do\nList");
        main_view_pager.setAdapter(viewPagerAdapter);

    }

    private CallBack callBack= new CallBack() {
        @Override
        public void changeActivity() {
            Intent intent= new Intent(Activity_Main.this,Activity_Image.class);
            startActivity(intent);
            finish();
        }

        @Override
        public void updateList() {

            fragment_my_meetings.updateMeetingListView();
        }

        @Override
        public void logOut() {
            firebaseAuth=FirebaseAuth.getInstance();
            if(firebaseAuth!=null)
                firebaseAuth.signOut();
            Intent intent = new Intent(Activity_Main.this,Activity_Login.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    };


    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments = new ArrayList<>();
        private List<String> fragmentsTitle = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        public void addFragment(Fragment fragment, String title){
            fragments.add(fragment);
            fragmentsTitle.add(title);
        }


        @NonNull
        @Override
        public Fragment getItem(int position) {

            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentsTitle.get(position);
        }
    }

}