package com.example.artify.activities;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.artify.R;
import com.example.artify.adapter.CheckoutAdapter;
import com.example.artify.dao.CartDao;
import com.example.artify.dao.OrderDao;
import com.example.artify.database.AppDbHelper;
import com.example.artify.models.CartItem;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CheckoutActivity extends AppCompatActivity {

    private List<CartItem> cart;
    private TextView txtTotal;
    private EditText edtName, edtPhone, edtAddress, edtNote;

    private OrderDao orderDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        MaterialToolbar tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        tb.setNavigationOnClickListener(v -> finish());

        SQLiteDatabase db = new AppDbHelper(this).getWritableDatabase();
        CartDao cartDao = new CartDao(db);
        orderDao = new OrderDao(db);

        // lấy dữ liệu giỏ
        cart = cartDao.getAll();

        // Recycler
        androidx.recyclerview.widget.RecyclerView rv = findViewById(R.id.rvItems);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new CheckoutAdapter(cart));

        // View
        txtTotal = findViewById(R.id.txtTotal);
        edtName = findViewById(R.id.edtName);
        edtPhone = findViewById(R.id.edtPhone);
        edtAddress = findViewById(R.id.edtAddress);
        edtNote = findViewById(R.id.edtNote);
        MaterialButton btnPlaceOrder = findViewById(R.id.btnPlaceOrder);

        // tổng tiền
        double total = 0;
        for (CartItem it : cart) total += it.getLineTotal();
        txtTotal.setText("Tổng: " + fmt(total));

        btnPlaceOrder.setOnClickListener(v -> {
            String name = edtName.getText().toString().trim();
            String phone = edtPhone.getText().toString().trim();
            String address = edtAddress.getText().toString().trim();
            String note = edtNote.getText().toString().trim();

            if (TextUtils.isEmpty(name)) { edtName.setError("Nhập họ tên"); return; }
            if (TextUtils.isEmpty(address)) { edtAddress.setError("Nhập địa chỉ"); return; }
            if (cart == null || cart.isEmpty()) {
                Toast.makeText(this, "Giỏ hàng trống", Toast.LENGTH_SHORT).show();
                return;
            }

            long orderId = orderDao.placeOrder(name, phone, address, note, cart);
            if (orderId > 0) {
                Toast.makeText(this, "Đặt hàng thành công #" + orderId, Toast.LENGTH_LONG).show();
                setResult(RESULT_OK);  // ✅ gửi tín hiệu về
                finish();


        } else {
                Toast.makeText(this, "Không thể đặt hàng, thử lại!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private static String fmt(double v) {
        return NumberFormat.getCurrencyInstance(new Locale("vi","VN")).format(v);
    }
}
