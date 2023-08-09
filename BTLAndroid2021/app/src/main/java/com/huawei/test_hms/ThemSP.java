package com.huawei.test_hms;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ThemSP extends AppCompatActivity {
    public static int returnValue = 10000;
    String[] strings = new String[]{"Sách 1", "Sách 2", "Sách 3", "Sách 4"};
    int chiso = -1;

    TextView tensp, giasp;
    Spinner spinner;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_sp);
        giasp = findViewById(R.id.giasp);
        tensp = findViewById(R.id.tensp);
        spinner = findViewById(R.id.spinner);
        button = findViewById(R.id.them);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                ThemSP.this,
                android.R.layout.simple_spinner_item,
                strings
        );
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinner.setAdapter(arrayAdapter);

//        Nhận và cài dữ liệu intent gửi đến
        Intent dulieuden = getIntent();
        if(dulieuden.getStringExtra("hanhdong").equals("sua")){
            button.setText("Sửa");
            tensp.setText(dulieuden.getStringExtra("ten"));
            giasp.setText(dulieuden.getStringExtra("gia"));
            spinner.setSelection(dulieuden.getIntExtra("hinh", 0));
            chiso = dulieuden.getIntExtra("chiso", 0);
        }else {
            if(dulieuden.getStringExtra("hanhdong").equals("themqr")){
                String scanresult = dulieuden.getStringExtra("scanresult");
                String[] part = scanresult.split("-");
                tensp.setText(part[0]);
                giasp.setText(part[1]);
            }
        }
//        Kết thúc nhận

        button.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setClass(ThemSP.this, ListSP.class);
            intent.putExtra("hanhdong", "themqr");
            intent.putExtra("tensp", tensp.getText().toString());
            intent.putExtra("giasp", giasp.getText().toString());
            intent.putExtra("hinh", spinner.getSelectedItemPosition());
            intent.putExtra("chiso", chiso);
            if(dulieuden.getStringExtra("hanhdong").equals("themqr")){
                startActivity(intent);
                finish();
            }else {
                setResult(returnValue, intent);
                finish();
            }
        });
    }
}