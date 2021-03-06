package it.feio.android.omninotes.testForThesis;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.longClick;
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
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

import android.content.res.Resources;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
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

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
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

    @Test
    public void sortNotes() throws IOException, InterruptedException {
        //  SETUP
        ActivityScenario activityScenario =
                ActivityScenario.launch(MainActivity.class);

        insertNote("Note 1", "");
        insertNote("Note 2", "");
        insertNote("Note 3", "");

        onView(withId(R.id.menu_sort))
                .perform(click());
        onView(withText("Creation date"))
                .perform(longClick());
        onView(new RecyclerViewMatcher(R.id.list)
                .atPositionOnView(0, R.id.note_title))
                .check(matches(withText("Note 3")));
    }

    @Test
    public void insertNewCategory() throws IOException, InterruptedException {
        //  SETUP
        ActivityScenario activityScenario =
                ActivityScenario.launch(MainActivity.class);

        onView(withId(R.id.fab_expand_menu_button)).perform(click());
        onView(withId(R.id.fab_note)).perform(click());
        onView(withId(R.id.detail_root)).check(matches(isDisplayed()));
        onView(withId(R.id.detail_tile_card)).check(matches(isDisplayed()));
        onView(withId(R.id.detail_content_card)).check(matches(isDisplayed()));

        onView(withId(R.id.menu_category))
                .perform(click());
        onView(withText("ADD CATEGORY"))
                .perform(click());
        onView(withId(R.id.category_title))
                .perform(typeText("New Category"));
        onView(withId(R.id.color_chooser))
                .perform(click());
        onView(withText("DONE"))
                .perform(click());
        onView(withText("OK"))
                .perform(click());
        onView(withId(R.id.detail_title)).perform(typeText("New Note"));
        pressBack();
        pressBack();

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
        onView(withText("New Category")).check(matches(isDisplayed()));
    }

    @Test
    public void deleteCategory() throws IOException, InterruptedException {
        //  SETUP
        ActivityScenario activityScenario =
                ActivityScenario.launch(MainActivity.class);

        onView(withId(R.id.fab_expand_menu_button)).perform(click());
        onView(withId(R.id.fab_note)).perform(click());
        onView(withId(R.id.detail_root)).check(matches(isDisplayed()));
        onView(withId(R.id.detail_tile_card)).check(matches(isDisplayed()));
        onView(withId(R.id.detail_content_card)).check(matches(isDisplayed()));

        onView(withId(R.id.menu_category))
                .perform(click());
        onView(withText("ADD CATEGORY"))
                .perform(click());
        onView(withId(R.id.category_title))
                .perform(typeText("New Category"));
        onView(withId(R.id.color_chooser))
                .perform(click());
        onView(withText("DONE"))
                .perform(click());
        onView(withText("OK"))
                .perform(click());
        onView(withId(R.id.detail_title)).perform(typeText("New Note"));
        pressBack();
        pressBack();

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
        onView(withText("New Category")).check(matches(isDisplayed()));

        onView(withText("New Category"))
                .perform(longClick());
        onView(withText("DELETE"))
                .perform(click());
        onView(withText("CONFIRM"))
                .perform(click());
        onView(withText("New Category")).check(doesNotExist());

    }

    @Test
    public void deleteNoteAndEmptyTrash() throws IOException, InterruptedException {
        //  SETUP
        ActivityScenario activityScenario =
                ActivityScenario.launch(MainActivity.class);
        insertNote("New Note", "New Note Content");
        onView(withText("New Note"))
                .perform(click());
        openActionBarOverflowOrOptionsMenu(
                getInstrumentation().getTargetContext()
        );
        onView(withText("Trash"))
                .perform(click());
        //Open drawer
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());
        //Wait for swipe animation to end
        onView(isRoot()).perform(waitForText("Trash", 5000));
        //Click on Trash on the drawer menu
        onView(withText("Trash")).check(matches(isDisplayed()));
        onView(withText("Trash")).perform(click());
        //Check if actually the note has been trashed
        onView(withText("New Note")).check(matches(isDisplayed()));
        onView(withText("New Note Content")).check(matches(isDisplayed()));

        openActionBarOverflowOrOptionsMenu(
                getInstrumentation().getTargetContext()
        );

        onView(withText("Empty trash")).perform(click());
        onView(withText("OK")).perform(click());
        onView(withText("New Note")).check(doesNotExist());
        onView(withText("New Note Content")).check(doesNotExist());
    }

    @Test
    public void infoMenu(){
        //  SETUP
        ActivityScenario activityScenario = ActivityScenario.launch(MainActivity.class);

        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());
        //Click on Archive on the drawer menu
        onView(withId(R.id.settings_view)).check(matches(isDisplayed()));
        onView(withId(R.id.settings_view)).perform(click());

        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.scrollToPosition(2));
        onView(allOf(withId(android.R.id.title), withText(R.string.settings_screen_data)))
                .check(matches(isDisplayed()));
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.scrollToPosition(3));
        onView(allOf(withId(android.R.id.title), withText(R.string.settings_screen_interface))).check(
                matches(isDisplayed()));
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.scrollToPosition(4));
        onView(allOf(withId(android.R.id.title), withText(R.string.settings_screen_navigation))).check(
                matches(isDisplayed()));
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.scrollToPosition(5));
        onView(allOf(withId(android.R.id.title), withText(R.string.settings_screen_behaviors))).check(
                matches(isDisplayed()));
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.scrollToPosition(6));
        onView(allOf(withId(android.R.id.title), withText(R.string.settings_screen_notifications)))
                .check(matches(isDisplayed()));
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.scrollToPosition(7));
        onView(allOf(withId(android.R.id.title), withText(R.string.settings_screen_privacy)))
                .check(matches(isDisplayed()));
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.scrollToPosition(9));
        onView(allOf(withId(android.R.id.title), withText(R.string.settings_beta)))
                .check(matches(isDisplayed()));
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.scrollToPosition(11));
        onView(allOf(withId(android.R.id.title), withText(R.string.online_manual)))
                .check(matches(isDisplayed()));
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.scrollToPosition(12));
        onView(allOf(withId(android.R.id.title), withText(R.string.settings_tour_show_again)))
                .check(matches(isDisplayed()));
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.scrollToPosition(14));
        onView(allOf(withId(android.R.id.title), withText(R.string.settings_changelog)))
                .check(matches(isDisplayed()));
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.scrollToPosition(15));
        onView(allOf(withId(android.R.id.title), withText(R.string.settings_statistics)))
                .check(matches(isDisplayed()));
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.scrollToPosition(16));
        onView(allOf(withId(android.R.id.title), withText(R.string.info)))
                .check(matches(isDisplayed()))
                .perform(click());
        onView(withId(R.id.webview)).check(matches(isDisplayed()));

        pressBack();
        pressBack();
        pressBack();
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

class RecyclerViewMatcher {
    private final int recyclerViewId;

    public RecyclerViewMatcher(int recyclerViewId) {
        this.recyclerViewId = recyclerViewId;
    }

    public Matcher<View> atPosition(final int position) {
        return atPositionOnView(position, -1);
    }

    public Matcher<View> atPositionOnView(final int position, final int targetViewId) {

        return new TypeSafeMatcher<View>() {
            Resources resources = null;
            View childView;

            public void describeTo(Description description) {
                String idDescription = Integer.toString(recyclerViewId);
                if (this.resources != null) {
                    try {
                        idDescription = this.resources.getResourceName(recyclerViewId);
                    } catch (Resources.NotFoundException var4) {
                        idDescription = String.format("%s (resource name not found)",
                                new Object[] { Integer.valueOf
                                        (recyclerViewId) });
                    }
                }

                description.appendText("with id: " + idDescription);
            }

            public boolean matchesSafely(View view) {

                this.resources = view.getResources();

                if (childView == null) {
                    RecyclerView recyclerView =
                            (RecyclerView) view.getRootView().findViewById(recyclerViewId);
                    if (recyclerView != null && recyclerView.getId() == recyclerViewId) {
                        childView = recyclerView.findViewHolderForAdapterPosition(position).itemView;
                    }
                    else {
                        return false;
                    }
                }

                if (targetViewId == -1) {
                    return view == childView;
                } else {
                    View targetView = childView.findViewById(targetViewId);
                    return view == targetView;
                }

            }
        };
    }
}