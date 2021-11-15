package dev.rdewan.promptnowtest.util

import android.view.View
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher


object RecyclerViewMatcher {
    private var recyclerViewId = 0

    fun RecyclerViewMatcher(recyclerViewId: Int) {
        RecyclerViewMatcher.recyclerViewId = recyclerViewId
    }

    fun atPositionOnView(
        position: Int, itemMatcher: Matcher<View?>,
        @NonNull targetViewId: Int
    ): Matcher<View?> {
        return object : BoundedMatcher<View?, RecyclerView>(RecyclerView::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("has view id $itemMatcher at position $position")
            }

            override fun matchesSafely(recyclerView: RecyclerView): Boolean {
                val viewHolder = recyclerView.findViewHolderForAdapterPosition(position)
                val targetView = viewHolder!!.itemView.findViewById<View>(targetViewId)
                return itemMatcher.matches(targetView)
            }
        }
    }
}