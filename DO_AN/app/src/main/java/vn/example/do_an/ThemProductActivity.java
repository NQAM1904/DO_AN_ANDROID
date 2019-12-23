package vn.example.do_an;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import vn.example.do_an.model.DataSystem;
import vn.example.do_an.model.Products;

public class ThemProductActivity extends AppCompatActivity {

    private static int RESULT_LOAD_IMAGE = 1;
    Products p;
    ActionBar actionBar;
    EditText edtName, edtGia, edtLoai, edtMoTa;
    ImageView imageView;
    ImageButton btnImage;
    ProgressBar progressBar;
    Button Saveproduct;
    DatabaseReference mDatabase;
    StorageReference mStorageRef;
    private Uri selectedImage;
    String picturePath;
    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_product);

        username = DataSystem.username;
        addControlls();
        openFirebase();
        addValues();
        addEvent();

    }

    private void addValues() {
        Intent i = getIntent();
        p = (Products)i.getSerializableExtra("products");
        if(p == null) p = new Products();

    }

    private void openFirebase() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }


    private void addEvent() {
        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
        // xu ly them san pham.
        Saveproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = getIntent();
                String nameProduct = edtName.getText().toString();
                String gia = edtGia.getText().toString();
                String Mota = edtName.getText().toString();
                String type = edtLoai.getText().toString();
                if (gia == null || gia.isEmpty()) {
                    edtGia.setError("Vui lòng nhập giá tiền");
                    edtGia.requestFocus();
                    return;
                }
                int monney = Integer.parseInt(gia);

                if (nameProduct.isEmpty()) {
                    edtName.setError("Vui lòng nhập tên sản phảm");
                    edtName.requestFocus();

                } else if (type.isEmpty()) {
                    edtLoai.setError("Không được bỏ trống");
                    edtLoai.requestFocus();

                } else {

                    Products p = new Products( null, null, nameProduct, Mota, type, monney, false, null);
                    if (picturePath != null && picturePath != "") {
                        p.base64 = encodeImage(picturePath);
                    }

                   // uploadFile();
                    String key = mDatabase.child("products").child(username).push().getKey();
                    mDatabase.child("products").child(username).child(key).setValue(p);
                    i.putExtra("products", p);
                    finish();
                }

            }
        });
    }

    private void addControlls() {
        imageView = findViewById(R.id.imageView);
        edtName = findViewById(R.id.edtName);
        edtLoai = findViewById(R.id.edtLoai);
        edtGia = findViewById(R.id.edtGia);
        edtMoTa = findViewById(R.id.edtMoTa);
        btnImage = findViewById(R.id.btnImage);
        actionBar = getSupportActionBar();
        Saveproduct = findViewById(R.id.Saveproduct);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#039be5")));
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();
            ImageView imageView = findViewById(R.id.imageView);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }


    }
    private String encodeImage(String path)
    {
        File imagefile = new File(path);
        FileInputStream fis = null;
        try{
            fis = new FileInputStream(imagefile);
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
        Bitmap bm = BitmapFactory.decodeStream(fis);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);
        //Base64.de
        return encImage;

    }
}
