package vn.example.do_an;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import vn.example.do_an.model.User;

public class SettingFragment extends Fragment {
    Button btn_logout;
    View myview;
    FirebaseAuth firebaseAuth;
    ArrayAdapter<User> UserAdapter;
    private DatabaseReference mDatabase;
    com.google.firebase.auth.FirebaseUser FirebaseUser;
    TextView edtMail;

    public SettingFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myview = inflater.inflate(R.layout.fragment_setting, container, false);
        addControlls();
        addEvent();
        addData();
        return myview;
    }

    private void addData() {
        mDatabase = FirebaseDatabase.getInstance().getReference("User");
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String email = user.getEmail();
            edtMail.setText(email);


        }
    }

    private void addEvent() {
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();

                LoginActivity();
            }
        });
    }

    private void addControlls() {
        btn_logout = myview.findViewById(R.id.btn_logout);
        edtMail = myview.findViewById(R.id.edtMail);
    }

    public void LoginActivity() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}
