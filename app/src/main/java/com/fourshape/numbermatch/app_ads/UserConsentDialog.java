package com.fourshape.numbermatch.app_ads;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fourshape.easythingslib.listeners.OnAdConsentDialogListener;
import com.fourshape.easythingslib.utils.MakeLog;
import com.fourshape.easythingslib.utils.TestDevices;
import com.google.android.ump.ConsentDebugSettings;
import com.google.android.ump.ConsentForm;
import com.google.android.ump.ConsentInformation;
import com.google.android.ump.ConsentRequestParameters;
import com.google.android.ump.FormError;
import com.google.android.ump.UserMessagingPlatform;

import org.jetbrains.annotations.NotNull;

public class UserConsentDialog {

    private static final String TAG = "UserConsentDialog";

    private ConsentInformation consentInformation;
    private Activity activity;
    private OnAdConsentDialogListener onAdConsentDialogListener;

    public UserConsentDialog (Activity activity) {
        this.activity = activity;
        prepare();
    }

    public UserConsentDialog setOnAdConsentDialogListener(OnAdConsentDialogListener onAdConsentDialogListener) {
        this.onAdConsentDialogListener = onAdConsentDialogListener;
        return this;
    }

    private void prepare () {
        if (this.activity == null)
        {
            MakeLog.error(TAG, "NULL Activity");
            return;
        }

        ConsentDebugSettings debugSettings = new ConsentDebugSettings.Builder(activity)
                .setDebugGeography(ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA)
                .addTestDeviceHashedId(TestDevices.ADMOB_TEST_DEVICE)
                .build();

        // Set tag for underage of consent. false means users are not underage.
        ConsentRequestParameters params = new ConsentRequestParameters
                .Builder()
                .setConsentDebugSettings(debugSettings)
                .setTagForUnderAgeOfConsent(false)
                .build();

        if (consentInformation == null)
            consentInformation = UserMessagingPlatform.getConsentInformation(activity);

        consentInformation.requestConsentInfoUpdate(this.activity, params, new ConsentInformation.OnConsentInfoUpdateSuccessListener() {
            @Override
            public void onConsentInfoUpdateSuccess() {

                if (consentInformation.isConsentFormAvailable()) {
                    // load form here.
                    MakeLog.info(TAG, "Consent Form is Available");
                    loadForm();
                } else {
                    MakeLog.info(TAG, "Consent Form is not Available");
                    afterFormActionStuff();
                }

            }
        }, new ConsentInformation.OnConsentInfoUpdateFailureListener() {
            @Override
            public void onConsentInfoUpdateFailure(@NonNull @NotNull FormError formError) {

                MakeLog.info(TAG, "Consent form error: " + formError.getMessage());
                MakeLog.info(TAG, "Consent form error code: " + formError.getErrorCode());

                afterFormActionStuff();

            }
        });

    }

    public void loadForm() {

        if (activity == null) {
            MakeLog.error(TAG, "NULL Activity");
            afterFormActionStuff();
            return;
        }

        UserMessagingPlatform.loadConsentForm(activity, new UserMessagingPlatform.OnConsentFormLoadSuccessListener() {
            @Override
            public void onConsentFormLoadSuccess(@NonNull @NotNull ConsentForm consentForm) {

                if (consentInformation.getConsentStatus() == ConsentInformation.ConsentStatus.REQUIRED) {

                    if (activity == null) {
                        MakeLog.error(TAG, "NULL Activity");
                        afterFormActionStuff();
                        return;
                    }

                    consentForm.show(activity, new ConsentForm.OnConsentFormDismissedListener() {
                        @Override
                        public void onConsentFormDismissed(@Nullable @org.jetbrains.annotations.Nullable FormError formError) {
                            // handle the dismissal by re-loading the form.
                            loadForm();
                        }
                    });
                } else if (consentInformation.getConsentStatus() ==  ConsentInformation.ConsentStatus.OBTAINED || consentInformation.getConsentStatus() == ConsentInformation.ConsentStatus.NOT_REQUIRED){

                    MakeLog.info(TAG, "Obtained OR NOT_REQUIRED");
                    afterFormActionStuff();

                } else {
                    // do nothing with unknown status.
                    afterFormActionStuff();
                }

            }
        }, new UserMessagingPlatform.OnConsentFormLoadFailureListener() {
            @Override
            public void onConsentFormLoadFailure(@NonNull @NotNull FormError formError) {
                MakeLog.error(TAG, "FormError Code: " + formError.getErrorCode());
                MakeLog.error(TAG, "FormError Message: " + formError.getMessage());
            }
        });

    }

    private void afterFormActionStuff () {
        if (onAdConsentDialogListener != null) {
            onAdConsentDialogListener.onDone();
        }
    }

}

