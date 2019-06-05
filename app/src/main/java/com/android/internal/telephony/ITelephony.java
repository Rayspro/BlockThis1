package com.android.internal.telephony;

/**
 * Created by admin on 14/02/18.
 */

public interface ITelephony {

        boolean endCall();

        void answerRingingCall();

        void silenceRinger();
}
