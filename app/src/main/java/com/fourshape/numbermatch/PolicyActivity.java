package com.fourshape.numbermatch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.fourshape.easythingslib.listeners.OnInternetStatusListener;
import com.fourshape.easythingslib.utils.CheckInternet;
import com.fourshape.easythingslib.utils.CommonSharedData;
import com.fourshape.numbermatch.app_ads.UserConsentDialog;
import com.fourshape.numbermatch.utils.OpenExternalUrl;
import com.fourshape.numbermatch.utils.SharedData;
import com.fourshape.easythingslib.listeners.OnAdConsentDialogListener;
import com.fourshape.easythingslib.utils.VariablesControl;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;

public class PolicyActivity extends AppCompatActivity {

    private MaterialButton viewPolicyMB, agreeMB, declineMB;
    private CircularProgressIndicator progressIndicator;
    private LinearLayoutCompat contentLL, btnLL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy);

        viewPolicyMB = findViewById(R.id.view_policy_mb);
        agreeMB = findViewById(R.id.agree_mb);
        declineMB = findViewById(R.id.decline_mb);
        progressIndicator = findViewById(R.id.progress_circular);
        contentLL = findViewById(R.id.content_container_ll);
        btnLL = findViewById(R.id.btn_container_ll);

        viewPolicyMB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenExternalUrl.open(view.getContext(), view, VariablesControl.PRIVACY_POLICY_URL);
            }
        });

        agreeMB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CommonSharedData(PolicyActivity.this, SharedData.SHARED_PREF_TITLE).policyStatus(true);
                progressIndicator.setVisibility(View.VISIBLE);
                contentLL.setVisibility(View.GONE);
                btnLL.setVisibility(View.GONE);
                new CheckInternet().setOnInternetStatusListener(onInternetStatusListener).now();
            }
        });

        declineMB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private final OnInternetStatusListener onInternetStatusListener = new OnInternetStatusListener() {
        @Override
        public void onOnline() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new UserConsentDialog(PolicyActivity.this).setOnAdConsentDialogListener(onAdConsentDialogListener).loadForm();
                }
            });
        }

        @Override
        public void onOffline() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressIndicator.setVisibility(View.GONE);
                    finish();
                }
            });
        }
    };

    private final OnAdConsentDialogListener onAdConsentDialogListener = new OnAdConsentDialogListener() {
        @Override
        public void onDone() {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressIndicator.setVisibility(View.GONE);
                    finish();
                }
            }, 250);

        }
    };

}