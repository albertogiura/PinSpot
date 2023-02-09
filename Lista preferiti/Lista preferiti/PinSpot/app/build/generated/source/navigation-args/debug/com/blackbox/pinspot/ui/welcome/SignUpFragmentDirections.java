package com.blackbox.pinspot.ui.welcome;

import androidx.annotation.NonNull;
import androidx.navigation.ActionOnlyNavDirections;
import androidx.navigation.NavDirections;
import com.blackbox.pinspot.R;

public class SignUpFragmentDirections {
  private SignUpFragmentDirections() {
  }

  @NonNull
  public static NavDirections actionSignUpFragmentToMainActivity() {
    return new ActionOnlyNavDirections(R.id.action_signUpFragment_to_mainActivity);
  }

  @NonNull
  public static NavDirections actionSignUpFragmentToLoginFragment() {
    return new ActionOnlyNavDirections(R.id.action_signUpFragment_to_loginFragment);
  }
}
