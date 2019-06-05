package com.example.admin.blockthis1;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


/**
 * Created by admin on 14/03/18.
 */

public class SetProfilePage {

    public void friendProfile(){
        onView(withText("Friend Profile")).perform(click());
    }
}
