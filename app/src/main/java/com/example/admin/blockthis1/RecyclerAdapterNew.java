package com.example.admin.blockthis1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by admin on 16/03/18.
 */

public class RecyclerAdapterNew extends RecyclerView.Adapter<RecyclerAdapterNew.MyHolderNew> {
    Context context;
    ImageView user;
    TextView name, phone, rejected;
    ArrayList<RejectedDataModel> rejectedDataModelArrayList;
    myDbAdapter helper;

    public class MyHolderNew extends RecyclerView.ViewHolder {
        TextView name,phone,rejected;
        ImageView user;

        public MyHolderNew(View itemView) {
            super(itemView);
            user = (ImageView) itemView.findViewById(R.id.imageUser);
            name = (TextView) itemView.findViewById(R.id.name_textView);
            phone = (TextView) itemView.findViewById(R.id.phone_textView);
            rejected = (TextView)itemView.findViewById(R.id.rejectedCount);

            helper = new myDbAdapter(context);


        }
    }
    public RecyclerAdapterNew(ArrayList<RejectedDataModel> arrayList)
    {
        this.rejectedDataModelArrayList=arrayList;
    }

    @Override
    public MyHolderNew onCreateViewHolder(ViewGroup parent, int viewType){
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.put_this_for_rejected_calls,null);
        MyHolderNew myHolderNew = new MyHolderNew(view);
        context = parent.getContext();
        return myHolderNew;
    }
    @Override
    public void onBindViewHolder(MyHolderNew myHolderNew,int position){
        RejectedDataModel rejectedDataModel = rejectedDataModelArrayList.get(position);
        myHolderNew.user.setImageResource(rejectedDataModel.getID());
        myHolderNew.name.setText(rejectedDataModel.getName());
        myHolderNew.phone.setText(rejectedDataModel.getPhoneNumber());
        myHolderNew.rejected.setText(rejectedDataModel.getRejected());
    }

    @Override
    public int getItemCount() {
        return rejectedDataModelArrayList.size();
    }
}
