package vn.example.do_an.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;

import vn.example.do_an.R;
import vn.example.do_an.model.Products;

public class productAdapter extends ArrayAdapter<Products> {
    Activity context;
    int resource;
    private ArrayList<Products> lstGoc;
    private ArrayList<Products> lstHienThi;

    public productAdapter(Activity context, int resource) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
        lstGoc = new ArrayList<>();
        lstHienThi = new ArrayList<>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) convertView = this.context.getLayoutInflater().inflate(this.resource, parent, false);

        TextView txtName, txtGia;
        ImageView imgView;

        final CheckBox cbChoose;

        // tìm control
        txtName = convertView.findViewById(R.id.txtName);
        txtGia = convertView.findViewById(R.id.txtGia);
        cbChoose = convertView.findViewById(R.id.cbChoose);
        imgView = convertView.findViewById(R.id.imgView);

        // Nạp dữ liệu
        final Products p = getItem(position);

        txtName.setText(p.getNameProduct());

//        String gia =  Integer.toString(p.getGia());
        NumberFormat format = NumberFormat.getCurrencyInstance();
        txtGia.setText(format.format(p.getGia()));
        cbChoose.setChecked(p.isChoose());

        if (p.base64 != null && !p.base64.isEmpty()) {
            byte[] decodedString = Base64.decode(p.base64, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imgView.setImageBitmap(decodedByte);
        }
        else {
            imgView.setImageResource(R.drawable.cho);
        }

        // tạo event
        cbChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p.setChoose(cbChoose.isChecked());
            }
        });
        return convertView;
    }
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                ArrayList<Products> FilteredArrList = new ArrayList<Products>();

                if (lstGoc == null) {
                    lstGoc = new ArrayList<Products>(lstHienThi); // saves the original data in mOriginalValues
                }


                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return
                    results.count = lstGoc.size();
                    results.values = lstGoc;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < lstGoc.size(); i++) {
                        String data = lstGoc.get(i).getNameProduct();
                        if (data.toLowerCase().contains(constraint.toString())) {
                            FilteredArrList.add(lstGoc.get(i));
                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                lstHienThi = (ArrayList<Products>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }
        };
        return filter;
    }

}
