package com.example.admin.blockthis1;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressBack;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by admin on 14/03/18.
 */
@RunWith(AndroidJUnit4.class)
public class BaseActivity {
    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void beforeTest(){
        onView(withId(R.id.image_block)).perform(closeSoftKeyboard());;
        onView(withId(R.id.button)).perform(click());
        onView(isRoot()).perform(pressBack());
    }
}
