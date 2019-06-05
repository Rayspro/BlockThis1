package com.example.admin.blockthis1;

import com.android.internal.telephony.ITelephony;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.preference.PreferenceManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 13/02/18.
 */

public class PhoneCallStateListener extends PhoneStateListener{

    private Context context;
    private myDbAdapter helper;
    private static int flag = 0;
    private static int myMode = -1;

    public PhoneCallStateListener(Context context){
        this.context = context;
    }

    public void setDefaultValues(String incomingNumber) {
        String blackList = helper.blaclList();
        if(blackList.contains(incomingNumber)){
            int exceeded = helper.checkExceededValue(incomingNumber);
            if(exceeded == 1){
                helper.updateForLimitExceeded(incomingNumber);
            }
        }
    }

    public boolean isNumberBlocked(String blackList, String incomingNumber){
        if(blackList.contains(incomingNumber)) return true;

        return false;
    }

    public boolean isBossProfile(String incomingNumber){
        if (helper.ifBossProfile(incomingNumber) == 1) return true;
        return false;
    }

    public void tuneAudioForBossProfile(AudioManager am){
            myMode = am.getRingerMode();
            switch (am.getRingerMode()) {
                case AudioManager.RINGER_MODE_SILENT:
                    am.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                    break;
                case AudioManager.RINGER_MODE_VIBRATE:
                    am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                    break;
                case AudioManager.RINGER_MODE_NORMAL:
                    Log.i("MyApp", "Normal mode");
                    break;
            }
    }

    public void updateExceededValue(String incomingNumber){
        helper.updateToggleExceeded(incomingNumber, "1");
    }

    public int getValueOfCount(String phone){
        String count = helper.getCount(phone);
        if(count != null) return Integer.parseInt(count);

        return 0;
    }

    public int getValueOfCallLimit(String phone){
        String callLimit = helper.getCallLimit(phone);
        if(callLimit != null) return Integer.parseInt(callLimit);

        return 0;
    }

    public boolean isTimeValid(Calendar calendar1,Calendar calendar2) {
        if (calendar1.compareTo(calendar2) < 0) return true;

        return false;
    }

    public void updateCount(String incomingNumber, int count){
        helper.update(incomingNumber, Integer.toString(count));
    }

    public boolean timeValidForFirstCallAndTimeLimit(String firstCall,String incomingNumber){
        String str[] = firstCall.split(":");

        int hr_firstCall = Integer.parseInt(str[0]);
        int min_firstCall = Integer.parseInt(str[1]);
        int sec_firstCall = Integer.parseInt(str[2]);

        int hr_timeLimit = 0, min_timeLimit = 0,sec_timeLimit = 0;

        String timeLimit = helper.valueOfTimeLimitForIncomingNumber(incomingNumber);

        if (!timeLimit.equals("0")) {
            String str1[] = timeLimit.split(":");
            hr_timeLimit = Integer.parseInt(str1[0]);
            min_timeLimit = Integer.parseInt(str1[1]);
        }

        int totalHours = hr_firstCall + hr_timeLimit;
        int totalMinutes = min_firstCall + min_timeLimit;
        int totalSeconds = sec_firstCall + sec_timeLimit;

        if (totalSeconds >= 60) {
            totalMinutes++;
            totalSeconds = totalSeconds % 60;
        }
        if (totalMinutes >= 60) {
            totalHours++;
            totalMinutes = totalMinutes % 60;
        }
        Calendar current = Calendar.getInstance();
        int hourCurrent = current.get(Calendar.HOUR_OF_DAY);
        int minuteCurrent = current.get(Calendar.MINUTE);
        int secondCurrent = current.get(Calendar.SECOND);

        SimpleDateFormat sdf_current = new SimpleDateFormat("hh:mm:ss");

        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();

        Date date1 = null, date2 = null;

        try {
            date1 = sdf_current.parse(hourCurrent + ":" + minuteCurrent + ":" + secondCurrent);
            date2 = sdf_current.parse(totalHours + ":" + totalMinutes + ":" + totalSeconds);

        } catch (Exception e) {

        }

        calendar1.setTime(date1);
        calendar2.setTime(date2);
        Log.i("current Time is :" + hourCurrent + ":" + minuteCurrent + ":" + secondCurrent, "------------>");
        Log.i("Total Time is :" + totalHours + ":" + totalMinutes + ":" + totalSeconds, "----------->");
        return isTimeValid(calendar1,calendar2);
    }

    public void thisIsFirstCall(String incomingNumber){
        Calendar currentTime = Calendar.getInstance();
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentTime.get(Calendar.MINUTE);
        int sec = currentTime.get(Calendar.SECOND);
        String timeFirstCall = String.valueOf(hour) + ":" + String.valueOf(minute) + ":" + String.valueOf(sec);
        helper.updateWhenFirstCall(incomingNumber, timeFirstCall);
    }

    public void onCallStateChanged (int state, String incomingNumber) {
        helper = new myDbAdapter(context);

        switch (state) {

            case TelephonyManager.CALL_STATE_IDLE:
                flag = 0;
                break;

            case TelephonyManager.CALL_STATE_RINGING:
                itsRinging(incomingNumber);
                Toast.makeText(context,"recieved2",Toast.LENGTH_SHORT).show();
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                setDefaultValues(incomingNumber);
                flag = 0;
                Toast.makeText(context,"recieved3",Toast.LENGTH_SHORT).show();

               /* if(myMode != -1 && myMode == AudioManager.RINGER_MODE_SILENT)
                    am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                else if(myMode != -1 && myMode == AudioManager.RINGER_MODE_VIBRATE)
                    am.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                else if(myMode != -1 && myMode == AudioManager.RINGER_MODE_NORMAL)
                    am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);*/
                break;
        }
    }
    public void updateRejected(String incomingNumber){
        int rejected = helper.getRejectedCount(incomingNumber);
        String rejectedCount = String.valueOf(++rejected);
        helper.updateRejectedCallValue(incomingNumber ,rejectedCount);
    }

    public void itsRinging(String incomingNumber){

        ITelephony telephonyService;
        TelephonyManager telephony = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);
        AudioManager am;
        am= (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);


        String blackList = helper.blaclList();

        if (isNumberBlocked(blackList,incomingNumber)) {
            int count = getValueOfCount(incomingNumber);
            if (isBossProfile(incomingNumber)) {
                tuneAudioForBossProfile(am);
            }
            else if (flag == 0) {
                int callLimit = getValueOfCallLimit(incomingNumber);
                if (callLimit > count) {
                    //if count is  0  then only it is first call, hence update it in database
                    if (count == 0) {
                        thisIsFirstCall(incomingNumber);
                    }
                    String firstCall = helper.valueOfFirstCallForIncomingNumber(incomingNumber);
                    //first call is not zero means first call is updated
                    if (!firstCall.equals("0")) {
                        if (timeValidForFirstCallAndTimeLimit(firstCall, incomingNumber)) {
                            try {
                                Class c = Class.forName(telephony.getClass().getName());
                                Method m = c.getDeclaredMethod("getITelephony");
                                m.setAccessible(true);
                                telephonyService = (ITelephony) m.invoke(telephony);
                                telephonyService.endCall();
                                updateRejected(incomingNumber);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            int newCount = count + 1;
                            updateCount(incomingNumber,newCount);
                            flag = 1;
                        }
                        else {
                            updateExceededValue(incomingNumber);
                        }
                    }
                }
                else {
                    updateExceededValue(incomingNumber);
                }
            }
        }
    }
}

