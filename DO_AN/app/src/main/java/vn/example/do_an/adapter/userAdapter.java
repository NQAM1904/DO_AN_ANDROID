package vn.example.do_an.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import vn.example.do_an.R;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import vn.example.do_an.model.Products;
import vn.example.do_an.model.User;

public class userAdapter extends ArrayAdapter<User> {
    Activity context;
    int resource;

    public userAdapter( Activity context, int resource) {
        super(context, resource);
        this.resource= resource;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView,  ViewGroup parent) {
        View custom = context.getLayoutInflater().inflate(resource,null);
        TextView txtUser, txtEmail;
        txtUser = custom.findViewById(R.id.txtUser);
        txtEmail = custom.findViewById(R.id.txtEmail);

        User u = new User();
        txtUser.setText(u.getUsername());
        txtEmail.setText(u.getEmail());

        return custom;
    }
}
