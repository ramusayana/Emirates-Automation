import XCTest
import SwiftUI
@testable import YourApp

class LoginViewTests: XCTestCase {

    func testLoginButtonEnabledState() {
        let viewModel = LoginViewModel()
        let loginView = LoginView(viewModel: viewModel)
        
        // Initially, the login button should be disabled
        XCTAssertFalse(loginView.isLoginButtonEnabled)
        
        // Enter username
        viewModel.username = "username"
        
        // Enter password
        viewModel.password = "password"
        
        // Now the login button should be enabled
        XCTAssertTrue(loginView.isLoginButtonEnabled)
    }


    func testLoginErrorHandling() {
    let viewModel = LoginViewModel()
    let loginView = LoginView(viewModel: viewModel)
    
    // Enter username
    viewModel.username = "username"
    
    // Enter incorrect password
    viewModel.password = "wrongpassword"
    
    // Simulate login attempt
    loginView.login()
    
    // Check that an error message is shown
    XCTAssertEqual(viewModel.errorMessage, "Incorrect username or password")
}

func testLoginLockoutAfterFailures() {
    let viewModel = LoginViewModel()
    let loginView = LoginView(viewModel: viewModel)
    
    // Simulate three failed attempts
    for _ in 0..<3 {
        viewModel.username = "username"
        viewModel.password = "wrongpassword"
        loginView.login()
    }
    
    // Now, the user should be locked out
    XCTAssertTrue(viewModel.isLockedOut)
    
    // Check that a lockout message is shown
    XCTAssertEqual(viewModel.errorMessage, "Too many failed attempts. Please try again later.")
}

}