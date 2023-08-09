package com.huawei.test_hms;

public class SanPham {
    private String name;
    private String mota;
    private int hinh;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMota() {
        return mota;
    }

    public void setMota(String mota) {
        this.mota = mota;
    }

    public int getHinh() {
        return hinh;
    }

    public void setHinh(int hinh) {
        this.hinh = hinh;
    }

    public SanPham(String name, String mota, int hinh) {
        this.name = name;
        this.mota = mota;
        this.hinh = hinh;
    }

}
