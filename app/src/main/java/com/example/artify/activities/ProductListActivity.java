package com.example.artify.activities;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artify.R;
import com.example.artify.adapter.ProductAdapter;
import com.example.artify.dao.CartDao;              // ⬅️ thêm
import com.example.artify.dao.ProductDao;
import com.example.artify.database.AppDbHelper;
import com.example.artify.models.Product;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar; // ⬅️ thêm

import java.util.List;

public class ProductListActivity extends AppCompatActivity {

    private EditText edtSearch;
    private Spinner spSort;
    private RecyclerView rv;
    private ProductAdapter adapter;
    private ProductDao productDao;
    private CartDao cartDao;                       // ⬅️ thêm

    private String keyword = "";
    private boolean sortDesc = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        edtSearch = findViewById(R.id.edtSearch);
        spSort = findViewById(R.id.spSort);
        rv = findViewById(R.id.rvProducts);
        FloatingActionButton fabCart = findViewById(R.id.fabCart);

        SQLiteDatabase db = new AppDbHelper(this).getWritableDatabase();
        productDao = new ProductDao(db);
        cartDao    = new CartDao(db);              // ⬅️ khởi tạo

        // Seed dữ liệu lần đầu
        productDao.seedIfEmpty();

        // RecyclerView
        adapter = new ProductAdapter(new ProductAdapter.Listener() {
            @Override public void onClick(Product p) {
                Toast.makeText(ProductListActivity.this, p.getName(), Toast.LENGTH_SHORT).show();
                // TODO: mở ProductDetailActivity sau
                Intent it = new Intent(ProductListActivity.this, ProductDetailActivity.class);
                it.putExtra(ProductDetailActivity.EXTRA_ID, p.getId());
                startActivity(it);
            }

            @Override public void onAddToCart(Product p) {
                // ⬅️ THÊM VÀO GIỎ + snackbar có nút 'Xem giỏ'
                cartDao.add(p);
                Snackbar.make(findViewById(android.R.id.content),
                                "Đã thêm: " + p.getName(),
                                Snackbar.LENGTH_SHORT)
                        .setAction("Xem giỏ", v ->
                                startActivity(new Intent(ProductListActivity.this, CartActivity.class)))
                        .show();
            }


        });
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        FloatingActionButton fabRevenue = findViewById(R.id.fabRevenue);
        fabRevenue.setOnClickListener(v ->
                startActivity(new Intent(this, RevenueActivity.class)));





        // Sort spinner
        String[] options = {"Giá ↑", "Giá ↓"};
        ArrayAdapter<String> spAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, options);
        spSort.setAdapter(spAdapter);
        spSort.setSelection(0);
        spSort.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                sortDesc = position == 1;
                reload();
            }
            @Override public void onNothingSelected(android.widget.AdapterView<?> parent) { }
        });

        // Search realtime
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                keyword = s.toString();
                reload();
            }
            @Override public void afterTextChanged(Editable s) { }
        });

        fabCart.setOnClickListener(v ->
                startActivity(new Intent(this, CartActivity.class)));

        ExtendedFloatingActionButton fabAddProduct = findViewById(R.id.fabAddProduct);
        fabAddProduct.setOnClickListener(v ->
                startActivity(new Intent(this, ProductFormActivity.class)));


        // lần đầu tải
        reload();
    }



    private void reload() {
        List<Product> items = productDao.list(keyword, sortDesc);
        adapter.submit(items);
    }

    @Override
    protected void onResume() {
        super.onResume();
        reload();                 // <- luôn tải lại list khi quay về
        // (tuỳ chọn) nếu có ô search đang lọc làm “mất” item mới
        // edtSearch.setText("");  // xoá keyword để thấy hết
    }

}
