package com.fancysoft.androidnoteapp;


import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.is;

import android.content.Intent;
import android.widget.ListView;
import android.widget.TextView;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class NoteAppTest {

    @Rule
    public ActivityTestRule<MainActivity> main = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() {
        flushDb();
        relaunchActivity();
    }

    private void flushDb() {
        main.getActivity().deleteDatabase("noteapp.db");
    }

    private void relaunchActivity() {
        Intent intent = main.getActivity().getIntent();
        main.finishActivity();
        main.launchActivity(intent);
    }

    @Test
    public void shouldShowEmptyListWhenNoNotesExist() {
        ListView listView = main.getActivity().findViewById(R.id.note_list);
        assertThat(listView.getCount(), is(0));
    }

    @Test
    public void shouldAddNote() {
        onView(withId(R.id.add_button)).perform(click());
        onView(withId(R.id.input_field)).perform(typeText("Some note"));
        onView(withId(R.id.save_button)).perform(click());
        onView(withId(R.id.back_button)).perform(click());

        ListView listView = main.getActivity().findViewById(R.id.note_list);
        assertThat(listView.getCount(), is(1));

        TextView noteIdView = listView.findViewById(R.id.note_id);
        TextView noteContentView = listView.findViewById(R.id.note_content);

        assertThat(noteIdView.getText().toString(), is("1"));
        assertThat(noteContentView.getText().toString(), is("Some note"));
    }

    @Test
    public void shouldCancelNoteCreation() {
        onView(withId(R.id.add_button)).perform(click());
        onView(withId(R.id.input_field)).perform(typeText("Some note"), closeSoftKeyboard());
        onView(withId(R.id.cancel_button)).perform(click());

        ListView listView = main.getActivity().findViewById(R.id.note_list);
        assertThat(listView.getCount(), is(0));
    }

    @Test
    public void shouldEditNote() {
        onView(withId(R.id.add_button)).perform(click());
        onView(withId(R.id.input_field)).perform(typeText("Some note"), closeSoftKeyboard());
        onView(withId(R.id.save_button)).perform(click());

        onView(withId(R.id.note_content)).perform(typeText(" edited"), closeSoftKeyboard());
        onView(withId(R.id.save_button)).perform(click());
        onView(withId(R.id.back_button)).perform(click());

        ListView listView = main.getActivity().findViewById(R.id.note_list);
        assertThat(listView.getCount(), is(1));

        TextView noteIdView = listView.findViewById(R.id.note_id);
        TextView noteContentView = listView.findViewById(R.id.note_content);

        assertThat(noteIdView.getText().toString(), is("1"));
        assertThat(noteContentView.getText().toString(), is("Some note edited"));
    }

    @Test
    public void shouldCancelEditNote() {
        onView(withId(R.id.add_button)).perform(click());
        onView(withId(R.id.input_field)).perform(typeText("Some note"), closeSoftKeyboard());
        onView(withId(R.id.save_button)).perform(click());

        onView(withId(R.id.note_content)).perform(typeText(" edited"), closeSoftKeyboard());
        onView(withId(R.id.back_button)).perform(click());

        ListView listView = main.getActivity().findViewById(R.id.note_list);
        assertThat(listView.getCount(), is(1));

        TextView noteIdView = listView.findViewById(R.id.note_id);
        TextView noteContentView = listView.findViewById(R.id.note_content);

        assertThat(noteIdView.getText().toString(), is("1"));
        assertThat(noteContentView.getText().toString(), is("Some note"));
    }

    @Test
    public void shouldDeleteNote() {
        onView(withId(R.id.add_button)).perform(click());
        onView(withId(R.id.input_field)).perform(typeText("Some note"), closeSoftKeyboard());
        onView(withId(R.id.save_button)).perform(click());

        onView(withId(R.id.delete_button)).perform(click());

        ListView listView = main.getActivity().findViewById(R.id.note_list);
        assertThat(listView.getCount(), is(0));
    }

    @Test
    public void shouldSelectNoteFromList() {
        onView(withId(R.id.add_button)).perform(click());
        onView(withId(R.id.input_field)).perform(typeText("First note"), closeSoftKeyboard());
        onView(withId(R.id.save_button)).perform(click());
        onView(withId(R.id.back_button)).perform(click());

        onView(withId(R.id.add_button)).perform(click());
        onView(withId(R.id.input_field)).perform(typeText("Second note"), closeSoftKeyboard());
        onView(withId(R.id.save_button)).perform(click());
        onView(withId(R.id.back_button)).perform(click());

        onData(anything()).inAdapterView(withId(R.id.note_list)).atPosition(1).perform(click());

        TextView noteIdView = main.getActivity().findViewById(R.id.note_id);
        TextView noteContentView = main.getActivity().findViewById(R.id.note_content);

        assertThat(noteIdView.getText().toString(), is("2"));
        assertThat(noteContentView.getText().toString(), is("Second note"));
    }

    @Test
    public void shouldDisplaySearchResults() {
        onView(withId(R.id.add_button)).perform(click());
        onView(withId(R.id.input_field)).perform(typeText("First note"), closeSoftKeyboard());
        onView(withId(R.id.save_button)).perform(click());
        onView(withId(R.id.back_button)).perform(click());

        onView(withId(R.id.add_button)).perform(click());
        onView(withId(R.id.input_field)).perform(typeText("Second note"), closeSoftKeyboard());
        onView(withId(R.id.save_button)).perform(click());
        onView(withId(R.id.back_button)).perform(click());

        onView(withId(R.id.search)).perform(typeText("First"));

        ListView listView = main.getActivity().findViewById(R.id.note_list);
        assertThat(listView.getCount(), is(1));

        TextView noteIdView = listView.findViewById(R.id.note_id);
        TextView noteContentView = listView.findViewById(R.id.note_content);

        assertThat(noteIdView.getText().toString(), is("1"));
        assertThat(noteContentView.getText().toString(), is("First note"));
    }

    @Test
    public void shouldDisplayNoSearchResultsWhenNothingIsFound() {
        onView(withId(R.id.add_button)).perform(click());
        onView(withId(R.id.input_field)).perform(typeText("First note"), closeSoftKeyboard());
        onView(withId(R.id.save_button)).perform(click());
        onView(withId(R.id.back_button)).perform(click());

        onView(withId(R.id.add_button)).perform(click());
        onView(withId(R.id.input_field)).perform(typeText("Second note"), closeSoftKeyboard());
        onView(withId(R.id.save_button)).perform(click());
        onView(withId(R.id.back_button)).perform(click());

        onView(withId(R.id.search)).perform(typeText("Wrong"));

        ListView listView = main.getActivity().findViewById(R.id.note_list);
        assertThat(listView.getCount(), is(0));
    }
}