package algonquin.cst2335.ju000013;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest2 {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    /**
     * Verifies that entering a simple password that does not meet complexity requirements
     * results in the correct error message.
     */
    @Test
    public void mainActivityTest2() {
        // Find the EditText by its ID and type "12345" into it, then close the keyboard
        ViewInteraction appCompatEditText = onView(withId(R.id.editText));
        appCompatEditText.perform(replaceText("12345"), closeSoftKeyboard());

        // Find the Button by its ID and perform a click action on it
        ViewInteraction materialButton = onView(withId(R.id.button));
        materialButton.perform(click());

        // Find the TextView by its ID and check that the text is "You shall not pass!"
        // This is the assertion step where the test passes if the text matches, or fails if it does not
        ViewInteraction textView = onView(withId(R.id.textView));
        textView.check(matches(withText("You shall not pass!")));
    }

    /**
     * Tests that a password lacking an uppercase letter is correctly identified as not complex enough.
     */
    @Test
    public void testFindMissingUpperCase() {
        ViewInteraction appCompatEditText = onView(withId(R.id.editText));
        appCompatEditText.perform(replaceText("password123#$*"), closeSoftKeyboard());

        ViewInteraction materialButton = onView(withId(R.id.button));
        materialButton.perform(click());

        ViewInteraction textView = onView(withId(R.id.textView));
        textView.check(matches(withText("You shall not pass!")));
    }

    /**
     * Tests that a password lacking a lowercase letter is correctly identified as not complex enough.
     */
    @Test
    public void testFindMissingLowerCase() {
        ViewInteraction appCompatEditText = onView(withId(R.id.editText));
        appCompatEditText.perform(replaceText("PASSWORD123#$*"), closeSoftKeyboard());

        ViewInteraction materialButton = onView(withId(R.id.button));
        materialButton.perform(click());

        ViewInteraction textView = onView(withId(R.id.textView));
        textView.check(matches(withText("You shall not pass!")));
    }

    /**
     * Tests that a password lacking a digit is correctly identified as not complex enough.
     */
    @Test
    public void testFindMissingDigit() {
        ViewInteraction appCompatEditText = onView(withId(R.id.editText));
        appCompatEditText.perform(replaceText("Password!#$*"), closeSoftKeyboard());

        ViewInteraction materialButton = onView(withId(R.id.button));
        materialButton.perform(click());

        ViewInteraction textView = onView(withId(R.id.textView));
        textView.check(matches(withText("You shall not pass!")));
    }

    /**
     * Tests that a password lacking a special character is correctly identified as not complex enough.
     */
    @Test
    public void testFindMissingSpecialCharacter() {
        ViewInteraction appCompatEditText = onView(withId(R.id.editText));
        appCompatEditText.perform(replaceText("Password123"), closeSoftKeyboard());

        ViewInteraction materialButton = onView(withId(R.id.button));
        materialButton.perform(click());

        ViewInteraction textView = onView(withId(R.id.textView));
        textView.check(matches(withText("You shall not pass!")));
    }

    /**
     * Verifies that a password with all required elements is acknowledged as complex enough.
     */
    @Test
    public void testPasswordIsComplexEnough() {
        // Define the complex password
        String complexPassword = "Password123#$*";

        // Type the complex password into the EditText and close the keyboard
        onView(withId(R.id.editText)).perform(replaceText(complexPassword), closeSoftKeyboard());

        // Click the 'button' to submit the password
        onView(withId(R.id.button)).perform(click());

        // Define the expected success message that the app actually displays
        String successMessage = "Your password meets the requirements";

        // Check that the 'textView' displays the expected success message
        onView(withId(R.id.textView))
                .check(matches(withText(successMessage)))
                .withFailureHandler((error, viewMatcher) -> {
                    throw new AssertionError("Test failed with error: " + error.getMessage());
                });
    }


    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
