package com.huawei.test_hms;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.huawei.hmf.tasks.Task;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.hmsscankit.ScanUtil;
import com.huawei.hms.ml.scan.HmsScan;
import com.huawei.hms.ml.scan.HmsScanAnalyzerOptions;
import com.huawei.hms.support.account.AccountAuthManager;
import com.huawei.hms.support.account.request.AccountAuthParams;
import com.huawei.hms.support.account.request.AccountAuthParamsHelper;
import com.huawei.hms.support.account.result.AuthAccount;
import com.huawei.hms.support.account.service.AccountAuthService;

public class MainActivity extends AppCompatActivity {
    Button sign_in, first, dssanpham;
    AccountAuthParams authParams;
    AccountAuthService service;
    public static final int DECODE = 1;
    public static final int GENERATE = 2;
    public static final int CAMERA_REQ_CODE = 111;
    private static final int REQUEST_CODE_SCAN_ONE = 0X01;
    public static final String RESULT = "SCAN_RESULT";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sign_in = (Button)findViewById(R.id.sign_in);
        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authParams = new AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM).setAuthorizationCode().createParams();
                service    = AccountAuthManager.getService(MainActivity.this, authParams);
                startActivityForResult(service.getSignInIntent(), 8888);
            }

        });

        first = (Button) findViewById(R.id.first);
        first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GenerateCodeActivity.class);
                startActivity(intent);
            }
        });

        dssanpham = (Button) findViewById(R.id.dssanpham);
        dssanpham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListSP.class);
                startActivity(intent);
            }
        });
    }

    public void loadScanKitBtnClick(View view) {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_REQ_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissions == null || grantResults == null) {
            return;
        }
        //Default View Mode
        if (requestCode == CAMERA_REQ_CODE) {
            ScanUtil.startScan(this, REQUEST_CODE_SCAN_ONE, new HmsScanAnalyzerOptions.Creator().create());
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // Process the authorization result to obtain the authorization code from AuthAccount.
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 8888) {
            Task<AuthAccount> authAccountTask = AccountAuthManager.parseAuthResultFromIntent(data);
            if (authAccountTask.isSuccessful()) {
                // The sign-in is successful, and the user's ID information and authorization code are obtained.
                AuthAccount authAccount = authAccountTask.getResult();
                Log.i("MainActivity", "serverAuthCode:" + authAccount.getAuthorizationCode());
                Log.i("MainActivity", "getDisplayName:" + authAccount.getDisplayName());
                Log.i("MainActivity", "getResult:" + authAccountTask.getResult());
            } else {
                // The sign-in failed.
                Log.e("MainActivity", "sign in failed:" + ((ApiException) authAccountTask.getException()).getStatusCode());
            }
        }
        if (requestCode == REQUEST_CODE_SCAN_ONE) {
            HmsScan obj = data.getParcelableExtra(ScanUtil.RESULT);
            if (obj instanceof HmsScan) {
                if (!TextUtils.isEmpty(((HmsScan) obj).getOriginalValue())) {
                    String textCode = obj.getOriginalValue();
                    Toast.makeText(this, textCode, Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(MainActivity.this, ThemSP.class);
                    intent1.putExtra("hanhdong", "themqr");
                    intent1.putExtra("scanresult", textCode);
                    startActivity(intent1);
                }
            }
        }
    }


}