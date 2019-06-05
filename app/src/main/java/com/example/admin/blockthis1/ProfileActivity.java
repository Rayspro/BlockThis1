package com.example.admin.blockthis1;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    private static final String normal = "0";
    private static final String boss = "1";
    private static final String friend = "2";
    private static final String family = "3";
    BottomNavigationView navigation;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    DataModel dataModel;
    TextView name, phone;
    StringBuffer output = new StringBuffer();

    static final int PICK_CONTACT = 1;
    myDbAdapter helper;

    ArrayList<DataModel> arrayListBoss=new ArrayList<DataModel>();
    ArrayList<DataModel> arrayListFriend=new ArrayList<DataModel>();
    ArrayList<DataModel> arrayListFamily=new ArrayList<DataModel>();

    int img = R.drawable.user;

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent aboutIntent = new Intent(ProfileActivity.this, SettingsActivity.class);

                if("BossProfile".equals(navigation.getMenu().findItem(navigation.getSelectedItemId()).toString())){
                    aboutIntent.putExtra("profile_status",1);
                }
                else  if("FriendProfile".equals(navigation.getMenu().findItem(navigation.getSelectedItemId()).toString())){
                    aboutIntent.putExtra("profile_status",2);
                }
                else  if("FamilyProfile".equals(navigation.getMenu().findItem(navigation.getSelectedItemId()).toString())){
                    aboutIntent.putExtra("profile_status",3);
                }
                startActivity(aboutIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_boss:
                   helper = new myDbAdapter(getApplicationContext());
                    arrayListBoss.clear();
                    arrayListFriend.clear();
                    arrayListFamily.clear();
                    Log.i("in boss nav :","------>");
                    String data = viewdata(boss);  //take blocked contacts from database
                    Log.d("Data is :", data);     //split data to pass to the recyclerview
                    String str[] = data.split("\n");
                    String myData[];
                    if (data.length() != 0) {
                        for (int i = 0; i < str.length; i++) {
                            myData = str[i].split(",");

                            for (int j = 0; j < myData.length; j = j + 2) {
                                dataModel = new DataModel(img, myData[j], myData[j + 1]);
                                Log.d("Data is :", myData[j + 1]);
                                arrayListBoss.add(dataModel);
                            }

                        }
                    } else {
                        data = "Nothing to show";
                        Toast.makeText(getApplication(), data, Toast.LENGTH_SHORT).show();
                    }
                    adapter = new RecyclerAdapter(arrayListBoss);
                    //recyclerView.setHasFixedSize(false);
                    layoutManager = new LinearLayoutManager(getApplication());
                    recyclerView.addItemDecoration(new DividerItemDecoration(getApplication(), LinearLayoutManager.VERTICAL));
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter);
                    return true;
                case R.id.navigation_friend:
                    arrayListBoss.clear();
                    arrayListFriend.clear();
                    arrayListFamily.clear();
                    Log.i("in friend nav :","------>");
                    String dataFriend = viewdata(friend);  //take blocked contacts from database
                    Log.d("Data of Friend is :",dataFriend);     //split data to pass to the recyclerview
                    String strFriend[] = dataFriend.split("\n");
                    String myDataFriend[];
                    if(dataFriend.length()!=0) {
                        for (int i = 0; i < strFriend.length; i++) {
                            myDataFriend = strFriend[i].split(",");

                            for (int j = 0; j < myDataFriend.length; j = j + 2) {
                                dataModel = new DataModel(img, myDataFriend[j], myDataFriend[j + 1]);
                                Log.d("Data is :",myDataFriend[j+1]);
                                arrayListFriend.add(dataModel);
                            }

                        }
                    }
                    else
                    {
                        dataFriend = "Nothing to show";
                        Toast.makeText(getApplicationContext(),dataFriend,Toast.LENGTH_SHORT).show();
                    }
                    adapter = null;
                    adapter = new RecyclerAdapter(arrayListFriend);
                    recyclerView.setAdapter(adapter);

                    return true;
                case R.id.navigation_family:
                    arrayListBoss.clear();
                    arrayListFamily.clear();
                    Log.i("in family nav :","------>");
                    arrayListFriend.clear();
                    String dataFamily = viewdata(family);  //take blocked contacts from database
                    Log.d("dataFamily is :",dataFamily);     //split data to pass to the recyclerview
                    String strFamily[] = dataFamily.split("\n");
                    String myDataFamily[];
                    if(dataFamily.length()!=0) {
                        for (int i = 0; i < strFamily.length; i++) {
                            myDataFamily = strFamily[i].split(",");

                            for (int j = 0; j < myDataFamily.length; j = j + 2) {
                                dataModel = new DataModel(img, myDataFamily[j], myDataFamily[j + 1]);
                                Log.d("Data is :",myDataFamily[j+1]);
                                arrayListFamily.add(dataModel);
                            }

                        }
                    }
                    else
                    {
                        dataFamily = "Nothing to show";
                        Toast.makeText(getApplicationContext(),dataFamily,Toast.LENGTH_SHORT).show();
                    }

                    adapter = null;
                    adapter = new RecyclerAdapter(arrayListFamily);
                    recyclerView.setAdapter(adapter);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        FloatingActionButton fab_new = (FloatingActionButton)findViewById(R.id.fab_new);
        helper = new myDbAdapter(this);
        fab_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Adding contact to the block list
               Log.d("Fab Listener called",":here");
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });

        navigation = (BottomNavigationView) findViewById(R.id.navigation);

       // if("BossProfile".equals(navigation.getMenu().findItem(navigation.getSelectedItemId()))){
            String data = viewdata(boss);  //take blocked contacts from database
            Log.d("Data is :", data);     //split data to pass to the recyclerview
            String str[] = data.split("\n");
            String myData[];
            if (data.length() != 0) {
                for (int i = 0; i < str.length; i++) {
                    myData = str[i].split(",");

                    for (int j = 0; j < myData.length; j = j + 2) {
                        dataModel = new DataModel(img, myData[j], myData[j + 1]);
                        Log.d("Data is :", myData[j + 1]);
                        arrayListBoss.add(dataModel);
                    }

                }
            } else {
                data = "Nothing to show";
                Toast.makeText(getApplication(), data, Toast.LENGTH_SHORT).show();
            }

            recyclerView = (RecyclerView) findViewById(R.id.recycle_new);
            adapter = new RecyclerAdapter(arrayListBoss);
           // recyclerView.setHasFixedSize(false);
            layoutManager = new LinearLayoutManager(getApplication());
            recyclerView.addItemDecoration(new DividerItemDecoration(getApplication(), LinearLayoutManager.VERTICAL));
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);


       // }
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        helper = new myDbAdapter(this);




        name = (TextView) findViewById(R.id.name_textView);
        phone = (TextView) findViewById(R.id.phone_textView);


    }


    public String viewdata(String type){
        String data = "";
        if(type.equals(boss)) {
            data = helper.getDataIf(boss);
        }
        else if(type.equals(friend))
        {
            data = helper.getDataIf(friend);
        }
        else if(type.equals(family)){
            data = helper.getDataIf(family);
        }
        Log.d("data is :",data);
        if(data.length()==0)
            data = "";
        return data;
    }

    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        System.out.println("in Activity result");
        switch (reqCode) {
            case (PICK_CONTACT):
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    try {
                        Cursor c = getContentResolver().query(contactData, null, null, null, null);
                        if (c.moveToFirst()) {

                            String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                            output.append("\n" + name);
                            String phoneNumber = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            String newNumber = "";
                            if (phoneNumber.contains("+91")) {
                                output.append("\n" + phoneNumber);
                                dataModel = new DataModel(img, name, phoneNumber);
                            } else {
                                newNumber = "+91" + phoneNumber;
                                output.append("\n" + newNumber);
                                dataModel = new DataModel(img, name, newNumber);
                            }
                            long id = 0;
                            System.out.print(navigation.getMenu().findItem(navigation.getSelectedItemId()).toString());
                            if("BossProfile".equals(navigation.getMenu().findItem(navigation.getSelectedItemId()).toString())){
                                Log.d("Inserted from","BossProfile");
                               id = helper.insertData(name, phoneNumber, boss,"0","0","0","0","0","0");
                                arrayListBoss.add(dataModel);

                                adapter = null;
                                adapter = new RecyclerAdapter(arrayListBoss);
                                recyclerView.setAdapter(adapter);
                            }
                            else if("FriendProfile".equals(navigation.getMenu().findItem(navigation.getSelectedItemId()).toString())){
                                Log.d("Inserted from","FriendProfile");
                                id = helper.insertData(name, phoneNumber, friend,"0","0:5","3","0","0","0");
                                arrayListFriend.add(dataModel);
                                adapter = null;
                                adapter = new RecyclerAdapter(arrayListFriend);
                                recyclerView.setAdapter(adapter);
                            }
                            else  if("FamilyProfile".equals(navigation.getMenu().findItem(navigation.getSelectedItemId()).toString())){
                                Log.d("Inserted from","FamilyProfile");
                                id = helper.insertData(name, phoneNumber, family,"0","0:10","3","0","0","0");
                                arrayListFamily.add(dataModel);
                                adapter = null;
                                adapter = new RecyclerAdapter(arrayListFamily);
                                recyclerView.setAdapter(adapter);
                            }

                            if (id <= 0) {
                                Toast.makeText(this, "Insertion Unsuccessful", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(this, "Insertion Successful", Toast.LENGTH_SHORT).show();
                            }
                        }
                        c.close();
                    } catch (Exception e) {

                    }
                }
                break;
        }
    }
}

