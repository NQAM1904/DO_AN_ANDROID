package vn.example.do_an;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

import vn.example.do_an.model.DataSystem;

public class LoginActivity extends AppCompatActivity {
    EditText edtEmail, edtPassword;
    Button btnSignIn;
    TextView tvSignUp;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        addControlls();
        addEvent();

    }

    private void addEvent() {
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
            }
        });
    }

    private void addControlls() {
        firebaseAuth = FirebaseAuth.getInstance();
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnSignIn = findViewById(R.id.btnSignIn);
        tvSignUp = findViewById(R.id.tvSignUp);
        progressBar = findViewById(R.id.progressBar);
    }
    

    public void login(View view) {

        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();
        if (email.isEmpty()) {
            edtEmail.setError("Vui lòng nhập email");
            edtEmail.requestFocus();
            return;
        } else if (password.isEmpty()) {
            edtPassword.setError("Vui lòng mật khẩu");
            edtPassword.requestFocus();
            return;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edtEmail.setError("Vui lòng nhập đúng địa chỉ email");
            edtEmail.requestFocus();
            return;

        }
        if(password.length()>10){
            edtPassword.setError("Mật khẩu dưới 10 ký tự");
            edtPassword.requestFocus();
            return;
        }
        else {
            progressBar.setVisibility(View.VISIBLE);

            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressBar.setVisibility(View.GONE);
                    if(task.isSuccessful()){

                        DataSystem.username = edtEmail.getText().toString().replace(".", "");
                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                        MainActivity();
                    }
                    else {
                        Toast.makeText(LoginActivity.this, "Email hoặc mật khẩu không chính xác!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }
    public void MainActivity(){
        Intent intent = new Intent(this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}



