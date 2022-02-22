package it.feio.android.omninotes.testForThesis;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerMatchers.isClosed;
import static androidx.test.espresso.contrib.DrawerMatchers.isOpen;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.not;

import android.view.View;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.PerformException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.espresso.util.HumanReadables;
import androidx.test.espresso.util.TreeIterables;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;

import org.hamcrest.Matcher;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import it.feio.android.omninotes.MainActivity;
import it.feio.android.omninotes.R;


@RunWith(AndroidJUnit4.class)
public class MyTestCases {
    @Test
    public void insertNewNote() throws IOException, InterruptedException {
        //  SETUP
        ActivityScenario activityScenario =
                ActivityScenario.launch(MainActivity.class);

        onView(withId(R.id.fab_expand_menu_button))
                .perform(click());
        onView(withId(R.id.fab_note))
                .perform(click());
        //Check if navigation on another fragment has happened
        onView(withId(R.id.detail_root))
                .check(matches(isDisplayed()));
        onView(withId(R.id.detail_tile_card))
                .check(matches(isDisplayed()));
        onView(withId(R.id.detail_content_card))
                .check(matches(isDisplayed()));
        //Inserting new title
        onView(withId(R.id.detail_title))
                .perform(typeText("New Note"));
        onView(withId(R.id.detail_content))
                .perform(typeText("New Note Content"));
        pressBack();
        onView(withText("New Note"))
                .check(matches(isDisplayed()));
        onView(withText("New Note Content"))
                .check(matches(isDisplayed()));

    }

    @Test
    public void insertNewNoteB() throws IOException, InterruptedException {
        //  SETUP
        ActivityScenario activityScenario =
                ActivityScenario.launch(MainActivity.class);

        onView(withText("New Note")).check(doesNotExist());
        onView(withText("New Note Content")).check(doesNotExist());

    }
    /*=====================================
     * Utils functions
     * =====================================*/

    //waiting for a textView to appear
    private static ViewAction waitForText(
            final String viewText,
            final long millis
    ) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isRoot();
            }
            @Override
            public String getDescription() {
                return "wait for a specific view with text <"
                        + viewText +
                        "> during "
                        + millis +
                        " millis.";
            }
            @Override
            public void perform(
                    final UiController uiController,
                    final View view
            ) {
                uiController.loopMainThreadUntilIdle();
                final long startTime = System.currentTimeMillis();
                final long endTime = startTime + millis;
                final Matcher<View> viewMatcher = withText(viewText);

                do {
                    for (View child : TreeIterables
                            .breadthFirstViewTraversal(view)) {
                        // found view with required ID
                        if (viewMatcher.matches(child)) {
                            return;
                        }
                    }

                    uiController
                            .loopMainThreadForAtLeast(50);
                }
                while (System.currentTimeMillis() < endTime);

                // timeout happens
                throw new PerformException.Builder()
                        .withActionDescription(
                                this.getDescription()
                        ).withViewDescription(
                                HumanReadables.describe(view)
                        ).withCause(new TimeoutException())
                        .build();
            }
        };
    }

}