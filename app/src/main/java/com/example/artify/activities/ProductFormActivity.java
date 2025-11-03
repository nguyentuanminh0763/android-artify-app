package com.example.artify.activities;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.artify.R;
import com.example.artify.dao.ProductDao;
import com.example.artify.database.AppDbHelper;
import com.example.artify.models.Product;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.button.MaterialButton;

public class ProductFormActivity extends AppCompatActivity {

    private TextInputEditText edtName, edtDesc, edtPrice, edtImage;
    private ProductDao dao;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_form);
        setTitle("Thêm sản phẩm");

        edtName  = findViewById(R.id.edtName);
        edtDesc  = findViewById(R.id.edtDesc);
        edtPrice = findViewById(R.id.edtPrice);
        edtImage = findViewById(R.id.edtImage);
        MaterialButton btnSave = findViewById(R.id.btnSave);

        SQLiteDatabase db = new AppDbHelper(this).getWritableDatabase();
        dao = new ProductDao(db);


        btnSave.setOnClickListener(v -> save());
    }

    private void save() {
        // 1) kiểm tra click có chạy
        // Toast.makeText(this, "Save pressed", Toast.LENGTH_SHORT).show();

        String name = get(edtName);
        String desc = get(edtDesc);
        String image = get(edtImage);
        String priceStr = get(edtPrice);

        if (TextUtils.isEmpty(name)) { edtName.setError("Nhập tên"); return; }
        if (TextUtils.isEmpty(priceStr)) { edtPrice.setError("Nhập giá"); return; }

        // 2) Chuẩn hóa giá: bỏ dấu chấm phẩy ngăn cách hàng nghìn
        priceStr = priceStr.replace(".", "").replace(",", "");
        double price;
        try {
            price = Double.parseDouble(priceStr);
            if (price < 0) { edtPrice.setError("Giá phải ≥ 0"); return; }
        } catch (Exception e) {
            edtPrice.setError("Giá không hợp lệ (vd: 120000)");
            return;
        }

        Product p = new Product(0, name, desc, price, image);

        try {
            long id = dao.add(p); // insertOrThrow ở DAO
            if (id > 0) {
                Toast.makeText(this, "Đã thêm #" + id, Toast.LENGTH_SHORT).show();
                finish(); // quay về list
            } else {
                Toast.makeText(this, "Thêm thất bại (id=-1)", Toast.LENGTH_LONG).show();
            }
        } catch (Exception ex) {
            // Hiện nguyên nhân thực (tên cột sai, ràng buộc, v.v.)
            Toast.makeText(this, "Lỗi DB: " + ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private String get(TextInputEditText e){
        return e.getText()==null ? "" : e.getText().toString().trim();
    }
}
