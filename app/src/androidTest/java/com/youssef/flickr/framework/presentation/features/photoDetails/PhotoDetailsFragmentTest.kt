package com.youssef.flickr.framework.presentation.features.photoDetails

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.youssef.flickr.R
import com.youssef.flickr.framework.presentation.entities.Photo
import com.youssef.flickr.framework.presentation.features.BaseTest
import com.youssef.flickr.framework.presentation.features.photos.PhotoHolder
import com.youssef.flickr.framework.presentation.features.photos.PhotosAdapter
import com.youssef.flickr.framework.utils.checkSnackBarDisplayed
import com.youssef.flickr.framework.utils.checkSnackBarMessage
import com.youssef.flickr.framework.utils.clickOnItem
import com.youssef.flickr.framework.utils.withDrawable
import org.junit.Test
import java.lang.Thread.sleep

internal class PhotoDetailsFragmentTest : BaseTest() {

    override fun startTest() {
        sleep(2000)
        val photo = getFirstPhoto()
        openFirstPhoto()
        checkViewsData(photo)
        testBack()
    }

    @Test
    fun addPhotoToFavourite() {
        sleep(2000)
        val photo = getFirstPhoto()
        openFirstPhoto()
        sleep(500)
        if (photo.isFavourite) {
            onView(withId(R.id.favouriteIV)).check(matches(withDrawable(R.drawable.ic_favorite_filled)))
            onView(withId(R.id.favouriteIV)).perform(click())
            onView(withId(R.id.favouriteIV)).check(matches(withDrawable(R.drawable.ic_favorite_border)))
            checkSnackBarMessage(activity.getString(R.string.remove_from_favourite_success_message))
        } else {
            onView(withId(R.id.favouriteIV)).check(matches(withDrawable(R.drawable.ic_favorite_border)))
            onView(withId(R.id.favouriteIV)).perform(click())
            onView(withId(R.id.favouriteIV)).check(matches(withDrawable(R.drawable.ic_favorite_filled)))
            checkSnackBarMessage(activity.getString(R.string.add_to_favourite_success_message))
        }
    }


    @Test
    fun downloadPhoto() {
        sleep(2000)
        openFirstPhoto()
        onView(withId(R.id.downloadIV)).perform(click())
        checkSnackBarDisplayed()
    }

    private fun checkViewsData(photo: Photo) {
        onView(withId(R.id.photoTitle)).check(matches(withText(photo.title)))
        onView(withId(R.id.favouriteIV)).check(matches(isDisplayed()))
        onView(withId(R.id.downloadIV)).check(matches(isDisplayed()))
    }

    private fun testBack() {
        pressBack()
        onView(withId(R.id.photosFragmentLayout)).check(matches(isDisplayed()))
    }

    private fun getFirstPhoto(): Photo {
        return (activity.findViewById<RecyclerView>(R.id.photosRV).adapter as PhotosAdapter).currentList.first()!!
    }

    private fun openFirstPhoto() {
        sleep(1000)
        clickOnItem<PhotoHolder>(R.id.photosRV, R.id.itemPhotoLayout, 0)
    }
}