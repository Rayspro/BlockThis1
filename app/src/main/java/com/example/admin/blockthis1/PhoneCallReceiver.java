package com.example.admin.blockthis1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;


/**
 * Created by admin on 13/02/18.
 */

public class PhoneCallReceiver extends BroadcastReceiver {
    String TAG = "PhoneCallReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        PhoneCallStateListener customPhoneListener = new PhoneCallStateListener(context);
        telephony.listen(customPhoneListener, PhoneStateListener.LISTEN_CALL_STATE);
        Log.d(TAG, "onReceive: ");
    }

}
