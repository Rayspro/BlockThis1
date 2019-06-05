package com.example.admin.blockthis1;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest extends BaseActivity{

    @Override
    public void beforeTest(){
        super.beforeTest();
    }

    @Test
    public void testDrawerLayout(){
        onView(withContentDescription("Open navigation drawer")).perform(click());
//        SetProfilePage page = new SetProfilePage();
//        page.friendProfile();
    }

    @Test
    public void testMenuOptions(){
        onView(withContentDescription("More options")).perform(click());
        onView(withText("In Meeting")).perform(click());

    }


    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.example.admin.blockthis1", appContext.getPackageName());
    }
}
