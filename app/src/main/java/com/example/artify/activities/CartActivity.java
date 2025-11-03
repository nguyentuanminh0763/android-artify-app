package com.example.artify.activities;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.artify.R;
import com.example.artify.adapter.CartAdapter;
import com.example.artify.dao.CartDao;
import com.example.artify.database.AppDbHelper;
import com.example.artify.models.CartItem;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CartActivity extends AppCompatActivity {
    private CartDao dao;
    private CartAdapter adapter;
    private TextView txtTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        MaterialToolbar tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        tb.setNavigationOnClickListener(v -> finish());

        SQLiteDatabase db = new AppDbHelper(this).getWritableDatabase();
        dao = new CartDao(db);

        androidx.recyclerview.widget.RecyclerView rv = findViewById(R.id.rvCart);
        rv.setLayoutManager(new LinearLayoutManager(this));

        adapter = new CartAdapter(new CartAdapter.Listener() {
            @Override public void onInc(CartItem it) { dao.increase(it.getId()); reload(); }
            @Override public void onDec(CartItem it) { dao.decreaseOrRemove(it.getId()); reload(); }
            @Override public void onRemove(CartItem it) { dao.remove(it.getId()); reload(); }
        });
        rv.setAdapter(adapter);

        txtTotal = findViewById(R.id.txtTotal);
        MaterialButton btnCheckout = findViewById(R.id.btnCheckout);

        btnCheckout.setOnClickListener(v -> {
            double total = dao.getTotal();
            if (total <= 0) {
                Toast.makeText(this, "Giỏ hàng trống!", Toast.LENGTH_SHORT).show();
                return;
            }
            startActivityForResult(new Intent(this, CheckoutActivity.class), 123);
            reload();
        });

        reload();
    }

    private void reload() {
        List<CartItem> items = dao.getAll();
        adapter.submit(items);
        txtTotal.setText("Tổng: " + fmt(dao.getTotal()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        reload(); // 🔥 Luôn load lại giỏ hàng từ DB mỗi khi quay lại
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123 && resultCode == RESULT_OK) {
            reload(); // ✅ cập nhật lại UI ngay
        }
    }



    private static String fmt(double v) {
        return NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(v);
    }
}
