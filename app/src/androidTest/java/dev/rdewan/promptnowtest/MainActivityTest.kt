package dev.rdewan.promptnowtest

import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.rdewan.promptnowtest.ui.adaptor.ItemAdaptor
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsNot
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {


    @Rule
    @JvmField
    val activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)


    /*
    1.check activity is launched and displayed
     */
    @Test
    fun isActivityInView(){
        Espresso.onView(withId(R.id.mainActivity))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    /*
    1.check add button is visible when activity is launched
    2.check delete button is visible when activity is launched
     */
    @Test
    fun is_add_delete_button_in_view(){
        Espresso.onView(withId(R.id.btnAdd))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(withId(R.id.btnDelete))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

    }

    /*
    1.check add button is disabled when activity is launched
    2.check delete button is disabled when activity is launched
     */
    @Test
    fun is_add_delete_button_not_enabled(){
        Espresso.onView(withId(R.id.btnAdd))
            .check((ViewAssertions.matches(IsNot.not(ViewMatchers.isEnabled()))))

        Espresso.onView(withId(R.id.btnDelete))
            .check(ViewAssertions.matches(IsNot.not(ViewMatchers.isEnabled())))
    }

    /*
    1. check when type text add button is enabled
    2. check checkbox is displayed and perform click
    3. check delete button is enabled and perform click
     */
    @Test
    fun when_type_text_add_button_enabled_checkbox_checked_delete_button_enabled(){
        Espresso.onView(withId(R.id.txtItem))
            .perform(ViewActions.typeText("item 1"), ViewActions.closeSoftKeyboard())
        Espresso.onView(withId(R.id.btnAdd)).check(ViewAssertions.matches(ViewMatchers.isEnabled()))
            .perform(ViewActions.click())

        Espresso.onView(withId(R.id.cbItem))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            .perform(ViewActions.click())

        Espresso.onView(withId(R.id.btnDelete))
            .check(ViewAssertions.matches(ViewMatchers.isEnabled()))
            .perform(ViewActions.click())

    }

    /*
    1. add item 10 items to recyclerview adaptor
    2. scroll to position 7
    3. check item item checkbox at position 7 and 8
    3. click the delete button
     */
    @Test
    fun test_recycler_view_adaptor_add_item_check_checkbox_delete_button_clicked(){
        Espresso.onView(withId(R.id.txtItem))
            .perform(ViewActions.typeText("item 1"), ViewActions.closeSoftKeyboard())
        Espresso.onView(withId(R.id.btnAdd)).check(ViewAssertions.matches(ViewMatchers.isEnabled()))
            .perform(ViewActions.click())
        Espresso.onView(withId(R.id.txtItem))
            .perform(ViewActions.typeText("item 2"), ViewActions.closeSoftKeyboard())
        Espresso.onView(withId(R.id.btnAdd)).check(ViewAssertions.matches(ViewMatchers.isEnabled()))
            .perform(ViewActions.click())
        Espresso.onView(withId(R.id.txtItem))
            .perform(ViewActions.typeText("item 3"), ViewActions.closeSoftKeyboard())
        Espresso.onView(withId(R.id.btnAdd)).check(ViewAssertions.matches(ViewMatchers.isEnabled()))
            .perform(ViewActions.click())
        Espresso.onView(withId(R.id.txtItem))
            .perform(ViewActions.typeText("item 4"), ViewActions.closeSoftKeyboard())
        Espresso.onView(withId(R.id.btnAdd)).check(ViewAssertions.matches(ViewMatchers.isEnabled()))
            .perform(ViewActions.click())
        Espresso.onView(withId(R.id.txtItem))
            .perform(ViewActions.typeText("item 5"), ViewActions.closeSoftKeyboard())
        Espresso.onView(withId(R.id.btnAdd)).check(ViewAssertions.matches(ViewMatchers.isEnabled()))
            .perform(ViewActions.click())
        Espresso.onView(withId(R.id.txtItem))
            .perform(ViewActions.typeText("item 6"), ViewActions.closeSoftKeyboard())
        Espresso.onView(withId(R.id.btnAdd)).check(ViewAssertions.matches(ViewMatchers.isEnabled()))
            .perform(ViewActions.click())
        Espresso.onView(withId(R.id.txtItem))
            .perform(ViewActions.typeText("item 7"), ViewActions.closeSoftKeyboard())
        Espresso.onView(withId(R.id.btnAdd)).check(ViewAssertions.matches(ViewMatchers.isEnabled()))
            .perform(ViewActions.click())
        Espresso.onView(withId(R.id.txtItem))
            .perform(ViewActions.typeText("item 8"), ViewActions.closeSoftKeyboard())
        Espresso.onView(withId(R.id.btnAdd)).check(ViewAssertions.matches(ViewMatchers.isEnabled()))
            .perform(ViewActions.click())
        Espresso.onView(withId(R.id.txtItem))
            .perform(ViewActions.typeText("item 9"), ViewActions.closeSoftKeyboard())
        Espresso.onView(withId(R.id.btnAdd)).check(ViewAssertions.matches(ViewMatchers.isEnabled()))
            .perform(ViewActions.click())
        Espresso.onView(withId(R.id.txtItem))
            .perform(ViewActions.typeText("item 10"), ViewActions.closeSoftKeyboard())
        Espresso.onView(withId(R.id.btnAdd)).check(ViewAssertions.matches(ViewMatchers.isEnabled()))
            .perform(ViewActions.click())

        Espresso.onView(withId(R.id.rvItems))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))


        Espresso.onView(withId(R.id.rvItems))
            .perform(
                RecyclerViewActions.scrollToPosition<ItemAdaptor.ItemViewHolder>(
                8))

        val materialCheckBox1 = Espresso.onView(
            Matchers.allOf(
                withId(R.id.cbItem),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.rvItems),
                        7
                    ),
                    1
                ),
                ViewMatchers.isDisplayed()
            )
        )
        materialCheckBox1.perform(ViewActions.click())

        val materialCheckBox2 = Espresso.onView(
            Matchers.allOf(
                withId(R.id.cbItem), childAtPosition(
                    childAtPosition(
                        withId(R.id.rvItems), 8
                    ), 1
                ), ViewMatchers.isDisplayed()
            ),
        )
        materialCheckBox2.perform(ViewActions.click())

        Espresso.onView(withId(R.id.btnDelete)).perform(ViewActions.click())

    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }

    @After
    fun tearDown() {
    }

}