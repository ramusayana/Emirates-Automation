import XCTest
import SwiftUI


class LoginViewTests: XCTestCase {

  // Scenario 1: Verify the Login button is not enabled as the user and password fields are empty and  we then enter the username and password to verify the functionality  

    func testLoginButtonEnabledState() {

        let viewModel = LoginViewModel()
        let loginView = LoginView(viewModel: viewModel)
        
      
        XCTAssertFalse(loginView.isLoginButtonEnabled)
        
        viewModel.username = "username"
        
        viewModel.password = "password"
      
        XCTAssertTrue(loginView.isLoginButtonEnabled)
    }

// Scenario 2: In this test I will be verifying the error message when I enter wrong password and clicked on Login button and I've also added an assertion "check match" to verify the text

    func testLoginErrorHandling() {

    let viewModel = LoginViewModel()
    let loginView = LoginView(viewModel: viewModel)
    
    viewModel.username = "username"
    
    viewModel.password = "wrongpassword"
    
    loginView.login()
    
    XCTAssertEqual(viewModel.errorMessage, "Incorrect username or password")
}

// Scenario 3: In this scenario I'm validating the message when user enter too many times and failed attempts and locked out


    func testLoginLockoutAfterFailures() { 

    let viewModel = LoginViewModel()
    let loginView = LoginView(viewModel: viewModel)
    
  // trying to simulte for 3 times here

    for (int i = 0; i < 3; i++) {
    
    viewModel.setUsername("username");
    viewModel.setPassword("wrongpassword");
    loginView.login();
}
    XCTAssertTrue(viewModel.isLockedOut)
    
    XCTAssertEqual(viewModel.errorMessage, "Too many failed attempts. Please try again later.")
}

// Scenario : Network Monitor (Offline → show message, no service call)
    func testOfflineShowsMessageAndDoesNotCallService() async {

    // recreate with offline monitor

    networkMonitor = FakeNetworkMonitor(isOnline: false)

    viewModel = LoginViewModel(
        authService: authService,
        networkMonitor: networkMonitor,
        tokenStore: tokenStore
    )

    viewModel.updateEmail("test@example.com")
    viewModel.updatePassword("Password123")

    await viewModel.login()

    let state = viewModel.state
    XCTAssertTrue(state.isOffline)
    XCTAssertNotNil(state.errorMessage)
    XCTAssertEqual(authService.callCount, 0)
}

// Scenario : Remember me Persists Token

func testRememberMeSavesTokenOnSuccess() async {
    
    authService.mode = .success(token: "token_abc")
    viewModel.updateEmail("test@example.com")
    viewModel.updatePassword("Password123")
    viewModel.updateRememberMe(true)

    await viewModel.login()

    XCTAssertEqual(tokenStore.savedToken, "token_abc")
}


}