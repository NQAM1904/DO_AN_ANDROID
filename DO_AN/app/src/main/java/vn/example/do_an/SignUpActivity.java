package vn.example.do_an;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import vn.example.do_an.model.User;

public class SignUpActivity extends AppCompatActivity {
    EditText edtusername, edtemail, edtpassword;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        addControlls();
    }

    private void addControlls() {
        edtusername = findViewById(R.id.edtusername);
        edtemail = findViewById(R.id.edtemail);
        edtpassword = findViewById(R.id.edtpassword);
        progressBar = findViewById(R.id.progressBar);
        databaseReference = FirebaseDatabase.getInstance().getReference("User");
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void SignUp(View view) {
        final String username = edtusername.getText().toString();
        final String email = edtemail.getText().toString();
        final String password = edtpassword.getText().toString();
        if (username.isEmpty()) {
            edtusername.setError("Vui lòng nhập tên!");
            edtusername.requestFocus();
            return;
        } else if (email.isEmpty()) {
            edtemail.setError("Vui lòng nhập email!");
            edtemail.requestFocus();
            return;
        } else if (password.isEmpty()) {
            edtpassword.setError("Vui lòng nhập mật khẩu");
            edtpassword.requestFocus();
            return;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edtemail.setError("Vui lòng nhập đúng địa chỉ email");
            edtemail.requestFocus();
            return;

        }
        if(password.length()>10){
            edtpassword.setError("Mật khẩu không quá 10 ký tự");
            edtpassword.requestFocus();
            return;
        }
        else {
            progressBar.setVisibility(View.VISIBLE);
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        User user = new User(
                                username,
                                email,
                                password
                        );
                        FirebaseDatabase.getInstance().getReference("User")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(SignUpActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);

                            }
                        });
                    }
                     if(task.getException() instanceof FirebaseAuthUserCollisionException){
                         Toast.makeText(getApplicationContext(), "Tài khoản này đã tồn tại!!", Toast.LENGTH_SHORT).show();
                    }


                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}
