package vn.example.do_an;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import vn.example.do_an.model.DataSystem;
import vn.example.do_an.model.Products;

public class MainActivity extends AppCompatActivity  implements BottomNavigationView.OnNavigationItemSelectedListener {
    private DatabaseReference mDatabase;
    ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addControlls();
        openFirebase();

    }

    private void openFirebase() {
        mDatabase = FirebaseDatabase.getInstance().getReference("products").child(DataSystem.username);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return  true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.menuAdd:
                scanCard();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    private void scanCard() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setOrientationLocked(false);
        integrator.setPrompt("SCAN");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(true);
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }

    private void addControlls() {
        defaultFragment(new HomeFragment());
        BottomNavigationView bottomNavigationView = findViewById(R.id.NavigationBar);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#039be5")));
    }


    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            String qr = scanResult.getContents();
            findProductByContent(qr);
        }
        // else continue with any other code you need in the method
    }

    private void findProductByContent(final String key) {
        if (key == null) return;
        if (key.isEmpty()) return;
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild(key)) {
                    Products p = snapshot.child(key).getValue(Products.class);
                    // run some code
                    Toast.makeText(MainActivity.this, "CO ITEM NAY " + p.getNameProduct(), Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(MainActivity.this, "KO CO ITEM NAY", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item){
        Fragment frag = null;
        switch (item.getItemId()){
            case R.id.Home:
                frag = new HomeFragment();
                break;

            case R.id.Product:
                frag = new ProductFragment();
                break;


            case R.id.Setting:
                frag = new SettingFragment();
                break;
        }
        return defaultFragment(frag);
    }
    private boolean defaultFragment(Fragment fragment){
        if(fragment != null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.mainActivity, fragment)
                    .commit();
            return true;
        }
        return false;
    }


}
