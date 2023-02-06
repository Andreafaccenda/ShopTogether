package com.example.speedmarket.ui

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.speedmarket.R
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    private lateinit var email: String
    private lateinit var password: String
    private lateinit var nomeUtente: String
    private lateinit var nomeProdotto: String

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setUp() {
        email = "l@gmail.com"
        password = "12345678"
        nomeUtente = "andrea"
        nomeProdotto = "Arance"
    }

    @Test
    fun homeFragmentIsDisplayed() {
        onView(withId(R.id.btnAccedi)).perform(click())
        onView(withId(R.id.etEmail)).perform(typeText(email), closeSoftKeyboard())
        onView(withId(R.id.etPassword)).perform(typeText(password), closeSoftKeyboard())
        onView(withId(R.id.btnAccediLogIn)).perform(click())
        onView(withId(R.id.recyclerView_categorie)).check(matches(isDisplayed()))
        onView(withId(R.id.txt_account)).check(matches(withText(Matchers.containsString(nomeUtente))))
    }

    @Test
    fun catalogoFragmentIsDisplayed() {
        onView(withId(R.id.btnAccedi)).perform(click())
        onView(withId(R.id.etEmail)).perform(typeText(email), closeSoftKeyboard())
        onView(withId(R.id.etPassword)).perform(typeText(password), closeSoftKeyboard())
        onView(withId(R.id.btnAccediLogIn)).perform(click())
        onView(withId(R.id.recyclerView_categorie))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
                click()))
        onView(withId(R.id.recyclerView_catalogo)).check(matches(isDisplayed()))
    }

    @Test
    fun productIsDisplayed() {
        onView(withId(R.id.btnAccedi)).perform(click())
        onView(withId(R.id.etEmail)).perform(typeText(email), closeSoftKeyboard())
        onView(withId(R.id.etPassword)).perform(typeText(password), closeSoftKeyboard())
        onView(withId(R.id.btnAccediLogIn)).perform(click())
        onView(withId(R.id.recyclerView_categorie))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
                click()))
        onView(withId(R.id.recyclerView_catalogo))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
                click()))
        onView(withId(R.id.title)).check(matches(withText(nomeProdotto)))
    }

    @Test
    fun carrelloIsDisplayed() {
        onView(withId(R.id.btnAccedi)).perform(click())
        onView(withId(R.id.etEmail)).perform(typeText(email), closeSoftKeyboard())
        onView(withId(R.id.etPassword)).perform(typeText(password), closeSoftKeyboard())
        onView(withId(R.id.btnAccediLogIn)).perform(click())
        onView(withId(R.id.recyclerView_categorie))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(1,
                click()))
        onView(withId(R.id.recyclerView_catalogo))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
                click()))
        onView(withId(R.id.txt_aggiungi_carrello)).perform(click())
        onView(withId(R.id.recyclerView_carrello)).check(matches(isDisplayed()))
    }

}
