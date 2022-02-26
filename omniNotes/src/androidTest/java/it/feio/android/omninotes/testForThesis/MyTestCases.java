package it.feio.android.omninotes.testForThesis;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerMatchers.isClosed;
import static androidx.test.espresso.contrib.DrawerMatchers.isOpen;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withChild;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withResourceName;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.PerformException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
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

import it.feio.android.checklistview.widgets.EditTextMultiLineNoEnter;
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
    public void insertNewCheckList() throws IOException, InterruptedException {
        //  SETUP
        ActivityScenario activityScenario =
                ActivityScenario.launch(MainActivity.class);

        onView(withId(R.id.fab_expand_menu_button))
                .perform(click());
        onView(withId(R.id.fab_checklist))
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
                .perform(typeText("New Checklist"));
        onView(withClassName(containsString("EditTextMultiLineNoEnter")))
                .perform(typeText("New Checklist Item"));
        pressBack();
        onView(withText("New Checklist"))
                .check(matches(isDisplayed()));
        onView(withText("New Checklist Item"))
                .check(matches(isDisplayed()));

    }

    @Test
    public void archiveNote() throws IOException, InterruptedException {
        //  SETUP
        ActivityScenario activityScenario =
                ActivityScenario.launch(MainActivity.class);

        insertNote("New Note", "New Note Content");

        //Swipe left
        onView(withId(R.id.list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, swipeLeft()));
        //Open drawer two times to avoid animation of swipe left
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());
        onView(withId(R.id.drawer_layout))
                .check(matches(isOpen(Gravity.LEFT)))
                .perform(DrawerActions.close());
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());
        //Click on Archive on the drawer menu
        onView(withText("Archive")).check(matches(isDisplayed()));
        onView(withText("Archive")).perform(click());
        //Check if actually the note has been archived
        onView(withText("New Note")).check(matches(isDisplayed()));
        onView(withText("New Note Content")).check(matches(isDisplayed()));


    }

    @Test
    public void searchNote() throws IOException, InterruptedException {
        //  SETUP
        ActivityScenario activityScenario =
                ActivityScenario.launch(MainActivity.class);

        insertNote("X", "");
        insertNote("Y", "");
        insertNote("Z", "");

        onView(withId(R.id.menu_search)).perform(click());
        onView(withId(R.id.search_src_text))
                .perform(typeText("X"))
                .perform(pressKey(KeyEvent.KEYCODE_ENTER));

        onView(allOf(withText("X"),withId(R.id.note_title))).check(matches(isDisplayed()));
        onView(allOf(withText("Y"),withId(R.id.note_title))).check(doesNotExist());
        onView(allOf(withText("Z"),withId(R.id.note_title))).check(doesNotExist());


    }

    @Test
    public void insertNoteWithReminder() throws IOException, InterruptedException {
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
                .perform(typeText("New Note With Reminder"));
        onView(withId(R.id.reminder_icon))
                .perform(click());
        onView(allOf(withText("OK"), withParent(withId(R.id.button_layout))))
                .check(matches(isDisplayed()))
                .perform(click());
        pressBack();
        onView(withId(R.id.alarmIcon))
                .check(matches(isDisplayed()));

    }
    /*=====================================
     * Utils functions
     * =====================================*/
    private void insertNote(String title, String body){
        //Click on fab button
        onView(withId(R.id.fab_expand_menu_button)).perform(click());
        onView(withId(R.id.fab_note)).perform(click());
        //Check if navigation on another fragment has happened
        onView(withId(R.id.detail_root)).check(matches(isDisplayed()));
        onView(withId(R.id.detail_tile_card)).check(matches(isDisplayed()));
        onView(withId(R.id.detail_content_card)).check(matches(isDisplayed()));
        //Inserting new title
        onView(withId(R.id.detail_title)).perform(typeText(title));
        onView(withId(R.id.detail_content)).perform(typeText(body));

        pressBack();
        pressBack();
    }
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