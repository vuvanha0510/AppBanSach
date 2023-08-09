package com.huawei.test_hms;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;

import com.huawei.hms.hmsscankit.ScanUtil;
import com.huawei.hms.ml.scan.HmsScan;
import com.huawei.hms.ml.scan.HmsScanAnalyzerOptions;

import java.util.ArrayList;

public class ListSP extends AppCompatActivity {
    ArrayList<SanPham> arrayList = new ArrayList<>();
    GridView listView;
    AdapterSanpham adapter;
    SearchView searchView;
    public static final int CAMERA_REQ_CODE = 111;
    private static final int REQUEST_CODE_SCAN_ONE = 0X01;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listproduct);
        listView = findViewById(R.id.listsp);
        Button themSP = findViewById(R.id.themsp);
        themSP.setOnClickListener(view -> {
            Intent intent = new Intent(ListSP.this, ThemSP.class);
            intent.putExtra("hanhdong", "them");
            startActivityForResult(intent, ThemSP.returnValue);
        });

        arrayList.add(new SanPham("Sách 1", "300.000", 0));
        arrayList.add(new SanPham("Sách 2", "450.000", 1));
        arrayList.add(new SanPham("Sách 3", "499.000", 2));
        arrayList.add(new SanPham("Sách 4", "220.000", 3));
        adapter = new AdapterSanpham(ListSP.this, R.layout.activity_product, arrayList);
        listView.setAdapter(adapter);


//        listView.setOnItemClickListener((adapterView, view, i, l) -> {
//            AlertDialog.Builder builder = new AlertDialog.Builder(ListSP.this);
//            builder.setTitle("Thực hiện hành động");

////            Hủy
//            builder.setNeutralButton("Hủy", (dialogInterface, i12) ->
//                    Toast.makeText(ListSP.this, "Đã hủy hành động", Toast.LENGTH_SHORT).show());

////            Sửa
//            builder.setPositiveButton("Sửa", (dialogInterface, i1) -> {
//                Intent intent = new Intent(ListSP.this, ThemSP.class);
//                intent.putExtra("hanhdong", "sua");
//                intent.putExtra("chiso", i);
//                intent.putExtra("ten", arrayList.get(i).getName());
//                intent.putExtra("gia", arrayList.get(i).getMota());
//                intent.putExtra("hinh", arrayList.get(i).getHinh());

//                startActivityForResult(intent, ThemSP.returnValue);
//            });

////            Xóa
//            builder.setNegativeButton("Xóa", (dialogInterface, i1) -> {
//                arrayList.remove(i);
//                adapter.notifyDataSetChanged();
//            });

////            Sau khi hủy hành động
//            builder.setOnCancelListener(dialogInterface ->
//                    Toast.makeText(ListSP.this, "Đã hủy hành động", Toast.LENGTH_SHORT).show());

//            builder.show();
//        });

    }
//       Tim kiem
//        @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_btl, menu);
//        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        searchView = (SearchView) menu.findItem(R.id.search_icon).getActionView();
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//        searchView.setMaxWidth(Integer.MAX_VALUE);

//                    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//               public boolean onQueryTextSubmit(String query) {
//                adapter.getFilter().filter(query);
//                return false;
//            }

    //                        @Override
//              public boolean onQueryTextChange(String newText) {
//                adapter.getFilter().filter(newText);
//                return false;
//            }
//        });

//        return super.onCreateOptionsMenu(menu);
//    }

    /*public void sapxepBtnClick(View view) {
            arrayList.add(new SanPham("Sách 4", "220.000", 3));
            arrayList.add(new SanPham("Sách 2", "450.000", 1));("Sách 1", "300.000", 0));
            arrayList.add(new SanPham("Sách 3", "499.000", 2));
            arrayList.add(new SanPham
            adapter = new AdapterSanpham(ListSP.this, R.layout.activity_product, arrayList);
            listView.setAdapter(adapter);
    }*/

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
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SCAN_ONE) {
            HmsScan obj = data.getParcelableExtra(ScanUtil.RESULT);
            if (obj instanceof HmsScan) {
                if (!TextUtils.isEmpty(((HmsScan) obj).getOriginalValue())) {
                    String textCode = obj.getOriginalValue();
                    Toast.makeText(this, textCode, Toast.LENGTH_SHORT).show();
                    String[] part = textCode.split("-");
                    arrayList.add(new SanPham(part[0], part[1], parseInt(part[2])));
                    adapter.notifyDataSetChanged();
                }
            }
        }

        if(requestCode == ThemSP.returnValue){
            if (data != null) {
                SanPham sanPham = new SanPham(
                        data.getStringExtra("tensp"),
                        data.getStringExtra("giasp"),
                        data.getIntExtra("hinh", 0)
                );

//                Kiểm tra hành động trả về
                int chiso = data.getIntExtra("chiso", -1);
                if(chiso > -1)
                    arrayList.set(chiso, sanPham);
                else
                    arrayList.add(sanPham);
                adapter.notifyDataSetChanged();
            }
            else
                Toast.makeText(ListSP.this, "Đã hủy hành động", Toast.LENGTH_SHORT).show();
        }
    }

    private int parseInt(String s) {
        return Integer.valueOf(s);
    }
}
