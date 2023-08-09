package com.huawei.test_hms;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AdapterSanpham extends BaseAdapter implements Filterable {
    private Context context;
    private int layout;
    private List<SanPham> arraylist;
    private List<SanPham> oldarray;

    private int[] hinhs = new int[]{
            R.drawable.sach1,
            R.drawable.sach2,
            R.drawable.sach3,
            R.drawable.sach4,

    };

    public AdapterSanpham(Context context, int layout, List<SanPham> arraylist) {
        this.context = context;
        this.layout = layout;
        this.arraylist = arraylist;
        this.oldarray = arraylist;
    }

    @Override
    public int getCount() {
        return arraylist.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(layout, null);
        SanPham sanPham = arraylist.get(position);
        TextView textView1 = convertView.findViewById(R.id.tensp);
        TextView textView2 = convertView.findViewById(R.id.gia);
        ImageView imageView = convertView.findViewById(R.id.hinh);
        textView1.setText(sanPham.getName());
        textView2.setText("$ " + sanPham.getMota() + "");
        imageView.setImageResource(hinhs[sanPham.getHinh()]);
        return convertView;
    }
//  Hien ds search
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String strSearch = constraint.toString();
                if(strSearch.isEmpty()){
                    arraylist = oldarray;
                }else {
                    List<SanPham> list = new ArrayList<>();
                    for (SanPham sanPham : oldarray){
                        if(sanPham.getName().toLowerCase(Locale.ROOT).contains(strSearch.toLowerCase(Locale.ROOT))){
                            list.add(sanPham);
                        }
                    }
                    arraylist = list;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = arraylist;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                arraylist = (List<SanPham>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
