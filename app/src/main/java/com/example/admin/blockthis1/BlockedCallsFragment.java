package com.example.admin.blockthis1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.content.ContentResolver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class BlockedCallsFragment extends Fragment {

    RecyclerView recyclerView;
    private static final String normal ="0";
    private static final String boss ="1";
    private static final String friend ="2";
    private static final String family ="3";
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    DataModel dataModel;
    TextView name,phone;
    StringBuffer output = new StringBuffer();
    static final int PICK_CONTACT = 1;
    myDbAdapter helper;
    int img = R.drawable.user_icon;
    public BlockedCallsFragment() {
        // Required empty public constructor
    }

    ArrayList<DataModel> arrayList=new ArrayList<DataModel>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_blocked_calls, container, false);
        Log.d(TAG, "onCreateView: ");

        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.fab);
        helper = new myDbAdapter(getActivity());
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Adding contact to the block list
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.recycle);

        helper = new myDbAdapter(getActivity());


        name = (TextView) view.findViewById(R.id.name_textView);
        phone = (TextView) view.findViewById(R.id.phone_textView);


        return  view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach: ");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter = null;
        arrayList.clear();
        String data = viewdata(getView());  //take blocked contacts from database
        Log.d("Data is block list :",data);     //split data to pass to the recyclerview
        String str[] = data.split("\n");
        String myData[];
        if(data.length()!=0) {
            for (int i = 0; i < str.length; i++) {
                myData = str[i].split(",");

                for (int j = 0; j < myData.length; j = j + 3) {
                    int img_id = Integer.parseInt(myData[j+2]);
                    if(img_id == 1){
                        img = R.drawable.boss_icon1;
                    }
                    else if(img_id == 2){
                        img = R.drawable.friend_icon;
                    }
                    else if(img_id == 3) {
                        img = R.drawable.family_icon;
                    }
                    dataModel = new DataModel(img, myData[j], myData[j + 1]);
                    arrayList.add(dataModel);
                }

            }
        }
        else
        {
            data = "Nothing to show";
            Toast.makeText(getActivity(),data,Toast.LENGTH_SHORT).show();
        }
        adapter= new RecyclerAdapter(arrayList);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),LinearLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        Log.d(TAG, "onStart: ");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach: ");
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public String viewdata(View view){
        String data = helper.getData();
        if(data.length()==0)
            data = "";
        return data;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated: ");
        super.onActivityCreated(savedInstanceState);
    }

    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        switch (reqCode) {
            case (PICK_CONTACT) :
                if (resultCode == Activity.RESULT_OK) {
                   Uri contactData = data.getData();
                    try {
                        Cursor c = getActivity().getContentResolver().query(contactData, null, null, null, null);
                        if (c.moveToFirst()) {

                            String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                           output.append("\n" + name);
                            String phoneNumber = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            String newNumber = "";
                            if(phoneNumber.contains("+91")){
                                output.append("\n" + phoneNumber);
                                dataModel = new DataModel(img, name,phoneNumber);
                            }
                            else {
                                newNumber = "+91"+phoneNumber;
                                output.append("\n" + newNumber);
                                dataModel = new DataModel(img, name,newNumber);
                            }
                            long id = helper.insertData(name, phoneNumber,"0","0","0:5","3","5","0","0");
                            System.out.println("Inserted with ID in BlankFrag :"+id);

                            if(id>0) {
                                arrayList.add(dataModel);

                                adapter = new RecyclerAdapter(arrayList);
                                recyclerView.setHasFixedSize(true);
                                layoutManager = new LinearLayoutManager(getActivity());
                                recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
                                recyclerView.setLayoutManager(layoutManager);
                                recyclerView.setAdapter(adapter);
                            }
                            if(id<=0)
                            {
                                Toast.makeText(getActivity(), "This is already added to the blacklist",Toast.LENGTH_SHORT).show();
                            } else
                            {
                                Toast.makeText(getActivity(), "Added to blacklist",Toast.LENGTH_SHORT).show();
                            }
                        }
                        c.close();
                    }catch (Exception e)
                    {

                    }
                }
                break;
        }
    }

}
