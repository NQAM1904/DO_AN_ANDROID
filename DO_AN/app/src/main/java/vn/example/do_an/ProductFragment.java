package vn.example.do_an;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import vn.example.do_an.adapter.productAdapter;
import vn.example.do_an.model.DataSystem;
import vn.example.do_an.model.Products;
import vn.example.do_an.model.User;


public class ProductFragment extends Fragment {
    private DatabaseReference mDatabase;
    ImageButton btnNew, btnDelete;
    SwipeMenuListView lvProduct;
    View myview;
    EditText edtSearch;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    String username;

    public productAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myview = inflater.inflate(R.layout.fragment_product, container, false);
        addControlls();
        addEvent();
        layUsername();
        addFireBase();

        return myview;
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

    private void addEvent() {
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                adapter.getFilter().filter(s.toString());
            }
        });
        lvProduct.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                final Products p = adapter.getItem(position);
                switch (index){
                    case 0:
                    Intent i = new Intent(getContext(), ThemProductActivity.class);
                    i.putExtra("products", p);
                    startActivity(i);
                    break;
                    case 1:
                        AlertDialog.Builder b = new AlertDialog.Builder(getContext());
                        b.setTitle("Thông báo");
                        b.setMessage("Bạn có muốn xóa sản phẩm này");
                        b.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                dialog.cancel();
                            }
                        });
                        b.setNegativeButton("Thoát", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        AlertDialog dialog = b.create();
                        dialog.show();
                        break;
                }
                return false;
            }
        });
        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), ThemProductActivity.class);
                i.putExtra("username", username);
                startActivityForResult(i, 100);
            }
        });

    }

    private void addControlls() {
        progressBar = myview.findViewById(R.id.progressBar);
        btnDelete = myview.findViewById(R.id.btnDelete);
        btnNew = myview.findViewById(R.id.btnNew);
        edtSearch = myview.findViewById(R.id.edtSearch);
        lvProduct = myview.findViewById(R.id.lvProduct);
        adapter = new productAdapter(ProductFragment.this.getActivity(), R.layout.item_products);
        lvProduct.setAdapter(adapter);

        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem openItem = new SwipeMenuItem(getActivity().getApplicationContext());
                openItem.setBackground(new ColorDrawable(Color.rgb(94, 172, 255)));
                openItem.setWidth(300);
                openItem.setIcon(R.drawable.icon_info_white);
                openItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(openItem);

                SwipeMenuItem deleteItem = new SwipeMenuItem(getActivity().getApplicationContext());
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                deleteItem.setWidth(300);
                deleteItem.setIcon(R.drawable.icon_delete_white);
                deleteItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(deleteItem);
            }
        };
        lvProduct.setMenuCreator(creator);
    }


}
