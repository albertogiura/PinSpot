package com.blackbox.pinspot.ui.main;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.SavedStateHandle;
import androidx.navigation.NavArgs;
import java.lang.IllegalArgumentException;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;

public class PinInfoFragmentArgs implements NavArgs {
  private final HashMap arguments = new HashMap();

  private PinInfoFragmentArgs() {
  }

  @SuppressWarnings("unchecked")
  private PinInfoFragmentArgs(HashMap argumentsMap) {
    this.arguments.putAll(argumentsMap);
  }

  @NonNull
  @SuppressWarnings("unchecked")
  public static PinInfoFragmentArgs fromBundle(@NonNull Bundle bundle) {
    PinInfoFragmentArgs __result = new PinInfoFragmentArgs();
    bundle.setClassLoader(PinInfoFragmentArgs.class.getClassLoader());
    if (bundle.containsKey("PinID")) {
      String PinID;
      PinID = bundle.getString("PinID");
      __result.arguments.put("PinID", PinID);
    } else {
      throw new IllegalArgumentException("Required argument \"PinID\" is missing and does not have an android:defaultValue");
    }
    return __result;
  }

  @NonNull
  @SuppressWarnings("unchecked")
  public static PinInfoFragmentArgs fromSavedStateHandle(
      @NonNull SavedStateHandle savedStateHandle) {
    PinInfoFragmentArgs __result = new PinInfoFragmentArgs();
    if (savedStateHandle.contains("PinID")) {
      String PinID;
      PinID = savedStateHandle.get("PinID");
      __result.arguments.put("PinID", PinID);
    } else {
      throw new IllegalArgumentException("Required argument \"PinID\" is missing and does not have an android:defaultValue");
    }
    return __result;
  }

  @SuppressWarnings("unchecked")
  @Nullable
  public String getPinID() {
    return (String) arguments.get("PinID");
  }

  @SuppressWarnings("unchecked")
  @NonNull
  public Bundle toBundle() {
    Bundle __result = new Bundle();
    if (arguments.containsKey("PinID")) {
      String PinID = (String) arguments.get("PinID");
      __result.putString("PinID", PinID);
    }
    return __result;
  }

  @SuppressWarnings("unchecked")
  @NonNull
  public SavedStateHandle toSavedStateHandle() {
    SavedStateHandle __result = new SavedStateHandle();
    if (arguments.containsKey("PinID")) {
      String PinID = (String) arguments.get("PinID");
      __result.set("PinID", PinID);
    }
    return __result;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
        return true;
    }
    if (object == null || getClass() != object.getClass()) {
        return false;
    }
    PinInfoFragmentArgs that = (PinInfoFragmentArgs) object;
    if (arguments.containsKey("PinID") != that.arguments.containsKey("PinID")) {
      return false;
    }
    if (getPinID() != null ? !getPinID().equals(that.getPinID()) : that.getPinID() != null) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int result = 1;
    result = 31 * result + (getPinID() != null ? getPinID().hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "PinInfoFragmentArgs{"
        + "PinID=" + getPinID()
        + "}";
  }

  public static final class Builder {
    private final HashMap arguments = new HashMap();

    @SuppressWarnings("unchecked")
    public Builder(@NonNull PinInfoFragmentArgs original) {
      this.arguments.putAll(original.arguments);
    }

    @SuppressWarnings("unchecked")
    public Builder(@Nullable String PinID) {
      this.arguments.put("PinID", PinID);
    }

    @NonNull
    public PinInfoFragmentArgs build() {
      PinInfoFragmentArgs result = new PinInfoFragmentArgs(arguments);
      return result;
    }

    @NonNull
    @SuppressWarnings("unchecked")
    public Builder setPinID(@Nullable String PinID) {
      this.arguments.put("PinID", PinID);
      return this;
    }

    @SuppressWarnings({"unchecked","GetterOnBuilder"})
    @Nullable
    public String getPinID() {
      return (String) arguments.get("PinID");
    }
  }
}
