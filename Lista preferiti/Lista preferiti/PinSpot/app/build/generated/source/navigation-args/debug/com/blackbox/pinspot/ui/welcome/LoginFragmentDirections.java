package com.blackbox.pinspot.ui.welcome;

import androidx.annotation.NonNull;
import androidx.navigation.ActionOnlyNavDirections;
import androidx.navigation.NavDirections;
import com.blackbox.pinspot.R;

public class LoginFragmentDirections {
  private LoginFragmentDirections() {
  }

  @NonNull
  public static NavDirections actionLoginFragmentToSignUpFragment() {
    return new ActionOnlyNavDirections(R.id.action_loginFragment_to_signUpFragment);
  }

  @NonNull
  public static NavDirections actionLoginFragmentToMainActivity() {
    return new ActionOnlyNavDirections(R.id.action_loginFragment_to_mainActivity);
  }
}
