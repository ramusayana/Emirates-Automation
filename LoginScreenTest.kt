import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginScreenTest {
    @get:Rule
    val activityRule = ActivityTestRule(LoginActivity::class.java)




    @Test
    fun testLoginButtonEnabledState() {
        // Initially, the login button should be disabled
        onView(withId(R.id.login_button)).check(matches(not(isEnabled())))

        // Enter username
        onView(withId(R.id.username)).perform(typeText("username"))

        // Enter password
        onView(withId(R.id.password)).perform(typeText("password"))

        // Now the login button should be enabled
        onView(withId(R.id.login_button)).check(matches(isEnabled()))
    }

    @Test
fun testLoginErrorHandling() {
    // Enter username
    onView(withId(R.id.username)).perform(typeText("username"))

    // Enter incorrect password
    onView(withId(R.id.password)).perform(typeText("wrongpassword"))

    // Click on the login button
    onView(withId(R.id.login_button)).perform(click())

    // Check that an error message is displayed
    onView(withText("Incorrect username or password"))
        .inRoot(ToastMatcher())
        .check(matches(isDisplayed()))
}

@Test
fun testLoginLockoutAfterFailures() {
    // Simulate three failed attempts
    repeat(3) {
        onView(withId(R.id.username)).perform(replaceText("username"))
        onView(withId(R.id.password)).perform(replaceText("wrongpassword"))
        onView(withId(R.id.login_button)).perform(click())
    }

    // Now, the user should be locked out
    onView(withId(R.id.login_button)).check(matches(not(isEnabled())))

    // Check that a lockout message is displayed
    onView(withText("Too many failed attempts. Please try again later."))
        .check(matches(isDisplayed()))
}

}