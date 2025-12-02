import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import org.hamcrest.Matchers.not
import org.junit.Test
import org.junit.Rule
import org.junit.runner.RunWith



// Here I'm using Junit 4 as my test runner
@RunWith(AndroidJUnit4::class)
class LoginScreenTest {

    // Rule annotation here I'm using JUnit 4
    @get:Rule
    val activityRule = ActivityTestRule(LoginActivity::class.java)

    @Test

    // Scenario 1: Verify the Login button is not enabled as the user and password fields are empty and  we then enter the username and password to verify the functionality
    
    fun testLoginButtonEnabledState() {

        onView(withId(R.id.login_button)).check(matches(not(isEnabled())))

        onView(withId(R.id.username)).perform(typeText("username"))

        onView(withId(R.id.login_button)).check(matches(not(isEnabled())))

        onView(withId(R.id.password)).perform(typeText("password"))

        onView(withId(R.id.login_button)).check(matches(isEnabled()))
    }

    @Test

    // Scenario 2: In this test I will be verifying the error message when I enter wrong password and clicked on Login button and I've also added an assertion "check match" to verify the text

    fun testLoginErrorHandling() {

        onView(withId(R.id.username)).perform(typeText("username"))

        onView(withId(R.id.password)).perform(typeText("wrongpassword"))

        onView(withId(R.id.login_button)).perform(click())

        onView(withText("Incorrect username or password entered in the password field"))
            .inRoot(ToastMatcher())
            .check(matches(isDisplayed()))
    }

    @Test

    // Scenario 3: In this scenario I'm validating the message when user enter too many times and failed attempts and locked out

    fun testLoginLockoutAfterFailures() {

        // Loop for multiple failed attempts (e.g., for three times)
        repeat(3) {
            onView(withId(R.id.username)).perform(replaceText("username"))
            onView(withId(R.id.password)).perform(replaceText("wrongpassword"))
            onView(withId(R.id.login_button)).perform(click())
        }

        onView(withId(R.id.login_button)).check(matches(not(isEnabled())))

        onView(withText("Too many failed attempts. Please try again later."))
            .check(matches(isDisplayed()))
    }


    @Test

// scenario 4 :  Network Monitor (Offline → show message, no service call)

    fun `offlineMessageNoServiceCall`() = runTest {

     // Recreate ViewModel with offline network monitor

    networkMonitor = FakeNetworkMonitor(online = false)
    viewModel = LoginViewModel(authRepository, networkMonitor, tokenStore)

    viewModel.onEmailChanged("test@example.com")
    viewModel.onPasswordChanged("Password123")

    viewModel.onLoginClicked()

    val state = viewModel.uiState.value
    assertTrue(state.isOffline)
    assertNotNull(state.errorMessage)
    assertEquals(0, authRepository.callCount) // no backend call
}


// Scenario 5 : Remember me Persists Token

@Test
    fun `rememberSavesTokenOnSuccess`() = runTest {

    authRepository.nextResult = Result.success("token_abc")
    viewModel.onEmailChanged("test@example.com")
    viewModel.onPasswordChanged("Password123")

  
    viewModel.onRememberMeChanged(true)

    viewModel.onLoginClicked()

    assertEquals("token_abc", tokenStore.savedToken)
}


}