package com.example.artify.activities;

import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.artify.R;
import com.example.artify.database.AppDbHelper;
import com.example.artify.services.AuthService;
import com.example.artify.utils.Validator;
import com.google.android.material.button.MaterialButton;

public class RegisterActivity extends AppCompatActivity {

    private EditText edtFullName, edtEmail, edtPassword, edtConfirm;
    private Button btnRegister;
    private AuthService auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtFullName = findViewById(R.id.edtFullName);
        edtEmail    = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirm  = findViewById(R.id.edtConfirm);
        btnRegister = findViewById(R.id.btnRegister);

        MaterialButton btnGoLogin = findViewById(R.id.btnGoLogin);
        btnGoLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish(); // để không quay lại Register khi back
        });


        SQLiteDatabase db = new AppDbHelper(this).getWritableDatabase();
        auth = new AuthService(db);

        btnRegister.setOnClickListener(v -> doRegister());


    }

    private void doRegister() {
        String name = edtFullName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String pwd = edtPassword.getText().toString();
        String confirm = edtConfirm.getText().toString();

        if (!Validator.notEmpty(name)) { toast("Vui lòng nhập họ tên"); return; }
        if (!Validator.isEmail(email)) { toast("Email không hợp lệ"); return; }
        if (!Validator.notEmpty(pwd) || !Validator.notEmpty(confirm)) { toast("Nhập mật khẩu"); return; }
        if (!pwd.equals(confirm)) { toast("Mật khẩu không khớp"); return; }

        try {
            auth.register(name, email, pwd);
            toast("Đăng ký thành công!");
            finish(); // quay lại Login
        } catch (SQLiteConstraintException e) {
            toast("Email đã tồn tại");
        } catch (Exception e) {
            toast("Lỗi: " + e.getMessage());
        }
    }

    private void toast(String m) {
        Toast.makeText(this, m, Toast.LENGTH_SHORT).show();
    }
}
