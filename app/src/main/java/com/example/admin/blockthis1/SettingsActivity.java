package com.example.admin.blockthis1;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

import org.w3c.dom.Text;

import java.util.Calendar;

public class SettingsActivity extends AppCompatActivity implements NumberPicker.OnValueChangeListener{

    Button btn_call, btn_time;
    int profile_type;
    String timeLimit = "", callLimit = "";
    myDbAdapter helper;
    TextView textView_call,textView_time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        btn_call = (Button)findViewById(R.id.button_call);
        btn_time = (Button)findViewById(R.id.button_time);
        textView_call = (TextView)findViewById(R.id.textView_call);
        textView_time = (TextView)findViewById(R.id.textView_time);

        profile_type = getIntent().getIntExtra("profile_status", 0);

        helper = new myDbAdapter(this);
        btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show();
            }

        });
        btn_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(SettingsActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        textView_time.setText("Time Selected :"+selectedHour + ":" + selectedMinute);
                        timeLimit =""+selectedHour + ":" + selectedMinute;
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

    }
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

        Log.i("value is",""+newVal);

    }

    @Override
    public void onBackPressed() {
        String typeOfProfile = String.valueOf(profile_type);
        helper.updateTimeByProfile(typeOfProfile,timeLimit,callLimit);
        super.onBackPressed();
    }

    public void show()
    {

        final Dialog d = new Dialog(SettingsActivity.this);
        d.setTitle("NumberPicker");
        d.setContentView(R.layout.dialog);
        Button button_set = (Button) d.findViewById(R.id.button_set);
        Button button_cancel = (Button) d.findViewById(R.id.button_cancel);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
        np.setMaxValue(100); // max value 100
        np.setMinValue(0);   // min value 0
        np.setWrapSelectorWheel(false);
        np.setOnValueChangedListener(this);
        button_set.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
               textView_call .setText("Calls in Minutes:"+String.valueOf(np.getValue())); //set the value to textview
                callLimit = String.valueOf(np.getValue());
                d.dismiss();
            }
        });
        button_cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                d.dismiss(); // dismiss the dialog
            }
        });
        d.show();


    }
}