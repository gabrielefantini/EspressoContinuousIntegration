package testForThesis;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerMatchers.isClosed;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.core.AllOf.allOf;

import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.PerformException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.util.HumanReadables;
import androidx.test.espresso.util.TreeIterables;
import androidx.test.filters.LargeTest;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hamcrest.Matcher;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.util.concurrent.TimeoutException;

import it.feio.android.omninotes.MainActivity;
import it.feio.android.omninotes.R;

@LargeTest
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MyTestCases {
    private static final String NOTE_TITLE = "Titolo di prova";
    private static final String NOTE_CONTENT = "Contenuto di prova";


    @Test
    public void A_testInfo(){
        /*=======================
           1-Open Drawer
           2-Verify the UI
           3-Go to Settings
           4-Go to Info
           5-Verify the UI
           6-3 time press back
         ========================*/

        //  SETUP
        ActivityScenario activityScenario =
                ActivityScenario.launch(MainActivity.class);

        //Open drawer
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());

        //Click on Settings on the drawer menu
        onView(withId(R.id.settings_view))
                .check(matches(isDisplayed()));
        onView(withId(R.id.settings_view))
                .perform(click());

        //Check if settings menu is correctly displayed
        onView(withId(R.id.recycler_view))
                .perform(RecyclerViewActions.scrollToPosition(2));
        onView(allOf(
                withId(android.R.id.title),
                withText(R.string.settings_screen_data)))
                .check(matches(isDisplayed()));

        onView(withId(R.id.recycler_view))
                .perform(RecyclerViewActions.scrollToPosition(3));
        onView(allOf(withId(android.R.id.title),
                withText(R.string.settings_screen_interface)))
                .check(matches(isDisplayed()));

        onView(withId(R.id.recycler_view))
                .perform(RecyclerViewActions.scrollToPosition(4));
        onView(allOf(withId(android.R.id.title),
                withText(R.string.settings_screen_navigation)))
                .check(matches(isDisplayed()));

        onView(withId(R.id.recycler_view))
                .perform(RecyclerViewActions.scrollToPosition(5));
        onView(allOf(withId(android.R.id.title),
                withText(R.string.settings_screen_behaviors)))
                .check(matches(isDisplayed()));

        onView(withId(R.id.recycler_view)).
                perform(RecyclerViewActions.scrollToPosition(6));
        onView(allOf(withId(android.R.id.title),
                withText(R.string.settings_screen_notifications)))
                .check(matches(isDisplayed()));

        onView(withId(R.id.recycler_view)).
                perform(RecyclerViewActions.scrollToPosition(7));
        onView(allOf(withId(android.R.id.title),
                withText(R.string.settings_screen_privacy)))
                .check(matches(isDisplayed()));

        onView(withId(R.id.recycler_view))
                .perform(RecyclerViewActions.scrollToPosition(9));
        onView(allOf(withId(android.R.id.title),
                withText(R.string.settings_beta)))
                .check(matches(isDisplayed()));

        onView(withId(R.id.recycler_view))
                .perform(RecyclerViewActions.scrollToPosition(11));
        onView(allOf(withId(android.R.id.title),
                withText(R.string.online_manual)))
                .check(matches(isDisplayed()));

        onView(withId(R.id.recycler_view))
                .perform(RecyclerViewActions.scrollToPosition(12));
        onView(allOf(withId(android.R.id.title),
                withText(R.string.settings_tour_show_again)))
                .check(matches(isDisplayed()));

        onView(withId(R.id.recycler_view))
                .perform(RecyclerViewActions.scrollToPosition(14));
        onView(allOf(withId(android.R.id.title),
                withText(R.string.settings_changelog)))
                .check(matches(isDisplayed()));

        onView(withId(R.id.recycler_view))
                .perform(RecyclerViewActions.scrollToPosition(15));
        onView(allOf(withId(android.R.id.title),
                withText(R.string.settings_statistics)))
                .check(matches(isDisplayed()));

        //Click on info
        onView(withId(R.id.recycler_view))
                .perform(RecyclerViewActions.scrollToPosition(16));
        onView(allOf(withId(android.R.id.title),
                withText(R.string.info)))
                .check(matches(isDisplayed()))
                .perform(click());
        //Check if info are displayed
        onView(withId(R.id.webview))
                .check(matches(isDisplayed()));

        pressBack();
        pressBack();
        pressBack();
    }

    @Test
    public void B_testInsertNote(){
        /*=======================
           1-Click fab
           2-Verify the UI
           3-Insert Title
           4-Insert Content
           5-Go back
           6-Verify the UI
         ========================*/

        //  SETUP
        ActivityScenario activityScenario =
                ActivityScenario.launch(MainActivity.class);

        insert(NOTE_TITLE, NOTE_CONTENT);

        onView(withText(NOTE_TITLE))
                .check(matches(isDisplayed()));
        onView(withText(NOTE_CONTENT))
                .check(matches(isDisplayed()));
    }

    /*@Test
    public void C_testArchiveNote(){
        *//*=======================
           1-Swipe Left an existing note
           2-Click drawer
           3-Verify that archive section has appeared
           4-Click on Archive
           5-Verify that the note archived is displayed
         ========================*//*

        //  SETUP
        ActivityScenario activityScenario =
                ActivityScenario.launch(MainActivity.class);

        //Swipe left
        onView(withId(R.id.list))
                .perform(
                        RecyclerViewActions
                                .actionOnItemAtPosition(
                                        0, swipeLeft()
                                ));
        //Open drawer
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());
        //Wait for swipe animation to end
        onView(isRoot())
                .perform(waitForText("Archive", 5000));
        //Click on Archive on the drawer menu
        onView(withText("Archive"))
                .check(matches(isDisplayed()));
        onView(withText("Archive"))
                .perform(click());
        //Check if actually the note has been archived
        onView(withText(NOTE_TITLE))
                .check(matches(isDisplayed()));
        onView(withText(NOTE_CONTENT))
                .check(matches(isDisplayed()));
    }*/

    @Test
    public void D_testSearchNotes(){
        /*=======================
           1-Insert 3 new notes
           2-Search for one title of one note
           3-Verify that the note searched is
             the only one that has been displayed
         ========================*/

        //  SETUP
        ActivityScenario activityScenario =
                ActivityScenario.launch(MainActivity.class);

        insert("X", NOTE_CONTENT);
        insert("Y", NOTE_CONTENT);
        insert("Z", NOTE_CONTENT);

        onView(withId(R.id.menu_search)).perform(click());
        onView(withId(R.id.search_src_text))
                .perform(typeText("X"))
                .perform(pressKey(KeyEvent.KEYCODE_ENTER));

        onView(allOf(withText("X"),withId(R.id.note_title)))
                .check(matches(isDisplayed()));
        onView(allOf(withText("Y"),withId(R.id.note_title)))
                .check(doesNotExist());
        onView(allOf(withText("Z"),withId(R.id.note_title)))
                .check(doesNotExist());

    }

    /*@Test
    public void E_testForDeleteNoteAndEmptyTrash(){
        *//*=======================
           1-Select a note
           2-Select Context Menu, select Trash
           3-Go to Trash
           4-Verify UI
           5-Empty Trash
           6-Verify UI
         ========================*//*

        //  SETUP
        ActivityScenario activityScenario =
                ActivityScenario.launch(MainActivity.class);

        //select note
        onView(withId(R.id.list))
                .perform(
                        RecyclerViewActions
                                .actionOnItemAtPosition(0, click())
                );

        openActionBarOverflowOrOptionsMenu(
                getInstrumentation().getTargetContext()
        );

        onView(withText("Trash")).perform(click());

        //Open drawer
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());
        //Wait for swipe animation to end
        onView(isRoot()).perform(waitForText("Trash", 5000));
        //Click on Archive on the drawer menu
        onView(withText("Trash")).check(matches(isDisplayed()));
        onView(withText("Trash")).perform(click());
        //Check if actually the note has been archived
        onView(withText(NOTE_TITLE)).check(matches(isDisplayed()));
        onView(withText(NOTE_CONTENT)).check(matches(isDisplayed()));

        openActionBarOverflowOrOptionsMenu(
                getInstrumentation().getTargetContext()
        );

        onView(withText("Empty trash")).perform(click());
        onView(withText("OK")).perform(click());
        onView(withText(NOTE_TITLE)).check(doesNotExist());
        onView(withText(NOTE_CONTENT)).check(doesNotExist());
    }*/

    /*=====================================
     * Utils functions
     * =====================================*/

    private void insert(String title, String body){
        //Click on fab button
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
                .perform(typeText(title));
        onView(withId(R.id.detail_content))
                .perform(typeText(body));

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