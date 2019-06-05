package com.example.admin.blockthis1;


import android.os.Bundle;
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


public class RejectedCallsFragment extends Fragment {

    public RejectedCallsFragment() {
        // Required empty public constructor
    }
    int img = R.drawable.user_icon;
    myDbAdapter helper;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    RejectedDataModel rejectedDataModel;
    TextView name,phone;
    ArrayList<RejectedDataModel> arrayList=new ArrayList<RejectedDataModel>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rejected_calls, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycle);

        helper = new myDbAdapter(getActivity());

        name = (TextView) view.findViewById(R.id.name_textView);
        phone = (TextView) view.findViewById(R.id.phone_textView);

        return view;
    }

    /*@Override
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
                    int rejectedCount = Integer.parseInt(myData[j+2]);
                    rejectedDataModel = new RejectedDataModel(img, myData[j], myData[j + 1],rejectedCount);
                    arrayList.add(rejectedDataModel);
                }

            }
        }
        else
        {
            data = "Nothing to show";
            Toast.makeText(getActivity(),data,Toast.LENGTH_SHORT).show();
        }
        adapter= new RecyclerAdapterNew(arrayList);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),LinearLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        Log.d(TAG, "onStart: ");
    }*/

    public String viewdata(View view){
        String data = helper.getRejectedCalls();
        if(data.length()==0)
            data = "";
        return data;
    }
}
