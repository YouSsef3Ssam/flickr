package com.youssef.flickr.framework.presentation.features

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.IdlingRegistry
import com.youssef.flickr.framework.utils.EspressoIdlingResource
import org.junit.After
import org.junit.Before
import org.junit.Test

abstract class BaseTest {

    lateinit var scenario: ActivityScenario<MainActivity>
    lateinit var activity: MainActivity

    @Before
    fun setUp() {
        createActivity()
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun unRegister() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    private fun createActivity() {
        scenario = ActivityScenario.launch(MainActivity::class.java)
        scenario.onActivity {
            activity = it
        }
    }

    @Test
    abstract fun startTest()
}
