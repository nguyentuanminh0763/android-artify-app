package com.example.artify.activities;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.artify.R;
import com.example.artify.database.AppDbHelper;
import com.example.artify.services.AuthService;
import com.example.artify.utils.Prefs;
import com.example.artify.utils.Validator;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail, edtPassword;
    private Button btnLogin, btnRemember, btnGoRegister;
    private AuthService auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail     = findViewById(R.id.edtEmail);
        edtPassword  = findViewById(R.id.edtPassword);
        btnLogin     = findViewById(R.id.btnLogin);
        btnRemember  = findViewById(R.id.btnRemember);
        btnGoRegister= findViewById(R.id.btnGoRegister);

        SQLiteDatabase db = new AppDbHelper(this).getWritableDatabase();
        auth = new AuthService(db);

        // điền nhớ mật khẩu
        edtEmail.setText(Prefs.getEmail(this));
        edtPassword.setText(Prefs.getPwd(this));

        btnLogin.setOnClickListener(v -> doLogin());
        btnRemember.setOnClickListener(v -> {
            Prefs.saveRemember(this,
                    edtEmail.getText().toString(),
                    edtPassword.getText().toString());
            Toast.makeText(this, "Đã lưu", Toast.LENGTH_SHORT).show();
        });
        btnGoRegister.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class)));
    }

    private void doLogin() {
        String email = edtEmail.getText().toString();
        String pwd = edtPassword.getText().toString();

        if (!Validator.isEmail(email)) {
            Toast.makeText(this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        if (auth.login(email, pwd)) {
            Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, ProductListActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Sai thông tin đăng nhập", Toast.LENGTH_SHORT).show();
        }
    }
}
