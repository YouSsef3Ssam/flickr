package com.youssef.flickr.framework.presentation.features.photos

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.youssef.flickr.R
import com.youssef.flickr.framework.presentation.features.BaseTest
import com.youssef.flickr.framework.utils.clickOnItem
import org.junit.Test
import java.lang.Thread.sleep

internal class PhotosFragmentTest : BaseTest() {

    override fun startTest() {
        sleep(1000)
        onView(withId(R.id.photosFragmentLayout)).check(matches(isDisplayed()))
        onView(withId(R.id.photosRV)).check(matches(isDisplayed()))
    }

    @Test
    fun openPhotoDetails() {
        openFirstPhoto()
        onView(withId(R.id.photoDetailsFragmentLayout)).check(matches(isDisplayed()))
    }

    private fun openFirstPhoto() {
        sleep(1000)
        clickOnItem<PhotoHolder>(R.id.photosRV, R.id.itemPhotoLayout, 0)
    }
}