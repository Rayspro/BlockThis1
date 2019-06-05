package com.example.admin.blockthis1;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    AnimationDrawable animationDrawable;
    NotificationCompat.Builder notification;
    PendingIntent pIntent;
    NotificationManager manager;
    Intent resultIntent;
    TaskStackBuilder stackBuilder;
    Boolean firstTime = true;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String TAG = "MainActivity :";
    SharedPreferences menuChecked;
    Intent intent;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    static final int NOTIFICATION_ID = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        menuChecked = getSharedPreferences("MenuChecke", 0);

        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        PhoneStateListener phoneStateListener = new PhoneStateListener(){
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {

                if(state == TelephonyManager.CALL_STATE_IDLE)
                    System.out.println("Call Idle");
                else
                    System.out.println("Call not Idle");

                super.onCallStateChanged(state, incomingNumber);
            }
        };

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //makes icon and title clickable

        if(isFirstTime() == false){
            intent = new Intent(this,SharedPrefActivtyRegister.class);
            startActivity(intent);
        }

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.root_layout);


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
    private void showNotification(boolean b) {


            System.out.println("showNotification -->" + b);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setAutoCancel(false)
                    .setContentTitle("Block Mode")
                    .setContentText("Busy Mode is On, No one will disturb you")
                    .setSmallIcon(R.drawable.block_notify)
                    .setOngoing(b);       // make it removable

//        Intent notificationIntent = new Intent(this, MainActivity.class);
//        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
//                PendingIntent.FLAG_UPDATE_CURRENT);
//        builder.setContentIntent(contentIntent);


        Notification notification = builder.build();
        NotificationManager manager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);

        if(b) {
            manager.notify(NOTIFICATION_ID, notification);
        }
        else {
            manager.cancel(NOTIFICATION_ID);
        }
    }
    private boolean isFirstTime() {
        if (firstTime == true) {
            SharedPreferences mPreferences = this.getSharedPreferences("first_time", Context.MODE_PRIVATE);
            firstTime = mPreferences.getBoolean("firstTime", true);
            if (firstTime) {
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putBoolean("firstTime", false);
                Log.d(TAG, "onCreate: ");

                editor.commit();
            }
        }
        return firstTime;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new BlockedCallsFragment(), "Black List");
        adapter.addFragment(new RejectedCallsFragment(), "Rejected Calls");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();


        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            Log.d("position :",mFragmentList.toString());

            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {

            Log.d("position :",mFragmentTitleList.get(position).toString());
            return mFragmentTitleList.get(position);
        }
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    private boolean isChecked = false;

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem checkable = menu.findItem(R.id.checkable_menu);
        isChecked = menuChecked.getBoolean("checkbox", false);
        System.out.println("Loaded value for isChecked() :"+isChecked);
        checkable = menu.findItem(R.id.checkable_menu);
        checkable.setChecked(isChecked);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.checkable_menu:
                System.out.println("in OptionsItemSelected");
                isChecked = !item.isChecked();
                item.setChecked(isChecked);

                SharedPreferences.Editor editor = menuChecked.edit();
                editor.putBoolean("checkbox", item.isChecked());
                editor.commit();
                System.out.println("isChecked() --->"+menuChecked.getBoolean("checkbox",false));

                Intent intent = new Intent();
                intent.putExtra("checked_status",isChecked);
                intent.setAction("com.example.admin.blockthis1.BUSY_MODE");
                sendBroadcast(intent);

//                Intent serviceIntent = new Intent(MainActivity.this,MyService.class);
//                serviceIntent
//                        .putExtra("checked_status",isChecked);
//                startService(serviceIntent);
//                if(!isChecked){ stopService(serviceIntent);}
                if (!isChecked) {
                    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.cancel(1);
                }

                break;
            case R.id.action_loading_contact:
                intent = new Intent(this,LoadContactsActivity.class);
                startActivity(intent);
                break;
            default:
                return false;
        }
        return super.onOptionsItemSelected(item);
    }


    private void addNotification() {
        // create notification builder
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        // setting notification Properties
                        .setSmallIcon(R.drawable.block_notify)
                        .setContentTitle("Busy Mode")
                        .setContentText("Busy Mode is ON, No one will disturb you now");

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_edit_profile) {
            // Handle the camera action
        } else if (id == R.id.nav_set_profile) {
            Intent newIntent = new Intent(this,ProfileActivity.class);
            startActivity(newIntent);

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
