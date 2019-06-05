package com.example.admin.blockthis1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.Myholder> {
    ArrayList<DataModel> dataModelArrayList;
    myDbAdapter helper;
    Context context;
    int img_id = R.drawable.user_icon;
    TextView name,phone;



    class Myholder extends RecyclerView.ViewHolder{
        TextView name,phone;
        ImageView user;

        public Myholder(View itemView) {
            super(itemView);
            user = (ImageView) itemView.findViewById(R.id.imageUser);
            name = (TextView) itemView.findViewById(R.id.name_textView);
            phone = (TextView) itemView.findViewById(R.id.phone_textView);

            helper = new myDbAdapter(context);
            itemView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction() == MotionEvent.ACTION_DOWN)
                    {
                        v.setBackgroundColor(Color.parseColor("#f0f0f0"));
                    }
                    if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)
                    {
                        v.setBackgroundColor(Color.TRANSPARENT);
                    }
                    return false;
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Log.i("positon-of-clicked-item", String.valueOf(name.getText()));
                    Log.i("positon-of-clicked-item", String.valueOf(phone.getText()));
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setTitle("Alert!!");
                    alert.setMessage("Are you sure to Unblock");
                    alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //do your work here
                            helper = new myDbAdapter(context);
                            Log.i("Deleted from DB",Integer.toString(helper.delete(String.valueOf(phone.getText()))));
                            dataModelArrayList.remove(getAdapterPosition());
                            notifyItemRemoved(getAdapterPosition());
                            dialog.dismiss();

                        }
                    });
                    alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                        }
                    });

                    alert.show();

                    Toast.makeText(v.getContext(), "Position is " + getAdapterPosition(), Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
        }
    }


    public RecyclerAdapter(ArrayList<DataModel> arrayList)
    {
        this.dataModelArrayList=arrayList;
    }


    @Override
    public Myholder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.put_this_form,null);
        Myholder myholder=new Myholder(view);
        context = parent.getContext();
        return myholder;

    }

    @Override
    public void onBindViewHolder(Myholder holder, int position) {
        DataModel dataModel=dataModelArrayList.get(position);
        holder.user.setImageResource(dataModel.getID());
        holder.name.setText(dataModel.getName());
        holder.phone.setText(dataModel.getPhoneNumber());

    }

    @Override
    public int getItemCount() {
        return dataModelArrayList.size();
    }
}