package vn.example.do_an;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import vn.example.do_an.adapter.productAdapter;
import vn.example.do_an.adapter.userAdapter;
import vn.example.do_an.model.DataSystem;
import vn.example.do_an.model.Products;
import vn.example.do_an.model.User;


public class HomeFragment extends Fragment {
    private DatabaseReference mDatabase;
    View myview;
    ProgressBar progressBar;
    public productAdapter adapter;
    String username;
    ListView lvProduct;
    Button thanhtoan;
    int vitri = -1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        myview = inflater.inflate(R.layout.fragment_home, container, false);
        addControll();
        layUsername();
        addFireBase();
        addEvent();
        return myview;

    }

    private void addEvent() {
        lvProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(vitri != -1){
                    Products p1 = adapter.getItem(vitri);
                    p1.setChon(false);
                }
                vitri = i;
                Products p = adapter.getItem(i);
                p.setChon(true);
                Toast.makeText(HomeFragment.this.getActivity(), "Bạn đã chọn " + " " + p.getNameProduct(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void layUsername() {
        username = DataSystem.username;
    }

    private void addFireBase() {
        progressBar.setVisibility(myview.VISIBLE);
        mDatabase = FirebaseDatabase.getInstance().getReference("products").child(username);
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                progressBar.setVisibility(myview.GONE);
                Products p = dataSnapshot.getValue(Products.class);
                adapter.add(p);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        };
        mDatabase.addChildEventListener(childEventListener);
    }


    private void addControll() {
        thanhtoan = myview.findViewById(R.id.thanhtoan);
        progressBar = myview.findViewById(R.id.progressBar);
        lvProduct = myview.findViewById(R.id.lvProduct);
        adapter = new productAdapter(HomeFragment.this.getActivity(), R.layout.item_products);
        lvProduct.setAdapter(adapter);
    }


}
