/*
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.huawei.test_hms;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.huawei.hms.hmsscankit.ScanUtil;
import com.huawei.hms.hmsscankit.WriterException;
import com.huawei.hms.ml.scan.HmsBuildBitmapOption;
import com.huawei.hms.ml.scan.HmsScan;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Objects;

public class GenerateCodeActivity extends Activity {

    private static final String TAG = "GenerateCodeActivity" ;
    private static final int[] BARCODE_TYPES = {HmsScan.QRCODE_SCAN_TYPE, HmsScan.DATAMATRIX_SCAN_TYPE, HmsScan.PDF417_SCAN_TYPE, HmsScan.AZTEC_SCAN_TYPE,
            HmsScan.EAN8_SCAN_TYPE, HmsScan.EAN13_SCAN_TYPE, HmsScan.UPCCODE_A_SCAN_TYPE, HmsScan.UPCCODE_E_SCAN_TYPE, HmsScan.CODABAR_SCAN_TYPE,
            HmsScan.CODE39_SCAN_TYPE, HmsScan.CODE93_SCAN_TYPE, HmsScan.CODE128_SCAN_TYPE, HmsScan.ITF14_SCAN_TYPE};
    private static  final int[] COLOR = {Color.BLACK, Color.BLUE, Color.GRAY, Color.GREEN, Color.RED, Color.YELLOW};
    private static  final int[] BACKGROUND = {Color.WHITE, Color.YELLOW, Color.RED, Color.GREEN, Color.GRAY, Color.BLUE, Color.BLACK};
    //Define a view.
    private EditText inputContent;
    private EditText inputGia;
    private Spinner generateType;
    private Spinner generateMargin;
    private Spinner generateColor;
    private Spinner generateBackground;
    private ImageView barcodeImage;
    private EditText barcodeWidth, barcodeHeight;
    private String content;
    private String gia;
    private int width, height;
    private Bitmap resultImage;
    private int type = 0;
    private int margin = 1;
    private int color = Color.BLACK;
    private int background = Color.WHITE;
    String[] strings = new String[]{"Sách 1", "Sách 2", "Sách 3", "Sách 4"};
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_generate);
        this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            if (window != null) {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }
        }
        inputContent = findViewById(R.id.tensp);
        inputGia = findViewById(R.id.giasp);
        barcodeImage = findViewById(R.id.barcode_image);
        spinner = findViewById(R.id.chonhinh);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                GenerateCodeActivity.this,
                android.R.layout.simple_spinner_item,
                strings
        );
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinner.setAdapter(arrayAdapter);
        //Set the barcode type.
        type = BARCODE_TYPES[0];
        margin = 1;
        color = COLOR[0];
        background = BACKGROUND[0];
    };
    /**
     * Generate a barcode.
     */
    public void generateCodeBtnClick(View v) {
        content = inputContent.getText().toString();
        gia = inputGia.getText().toString();
        content = content + "-" + gia + "-" + spinner.getSelectedItemPosition();
        width = 700;
        height = 700;
        //Set the barcode content.
        if (content.length() <= 0) {
            Toast.makeText(this, "Please input content first!", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            //Generate the barcode.
            HmsBuildBitmapOption options = new HmsBuildBitmapOption.Creator().setBitmapMargin(margin).setBitmapColor(color).setBitmapBackgroundColor(background).create();
            resultImage = ScanUtil.buildBitmap(content, type, width, height, options);
            barcodeImage.setImageBitmap(resultImage);
        } catch (WriterException e) {
            Toast.makeText(this, "Parameter Error!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Save the barcode.
     */
    public void saveCodeBtnClick(View v) {
        if (resultImage == null) {
            Toast.makeText(GenerateCodeActivity.this, "Please generate barcode first!", Toast.LENGTH_LONG).show();
            return;
        }
        try {
            String fileName = System.currentTimeMillis() + ".jpg";
            String storePath = Environment.getExternalStorageDirectory().getAbsolutePath();
            File appDir = new File(storePath);
            if (!appDir.exists()) {
                appDir.mkdir();
            }
            File file = new File(appDir, fileName);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            boolean isSuccess = resultImage.compress(Bitmap.CompressFormat.JPEG, 70, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            Uri uri = Uri.fromFile(file);
            GenerateCodeActivity.this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            if (isSuccess) {
                Toast.makeText(GenerateCodeActivity.this, "Barcode has been saved locally", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(GenerateCodeActivity.this, "Barcode save failed", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.w(TAG, Objects.requireNonNull(e.getMessage()));
            Toast.makeText(GenerateCodeActivity.this, "Unkown Error", Toast.LENGTH_SHORT).show();
        }

    }
}
