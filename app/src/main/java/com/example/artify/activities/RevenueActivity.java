package com.example.artify.activities;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artify.R;
import com.example.artify.adapter.OrderHistoryAdapter;
import com.example.artify.dao.OrderDao;
import com.example.artify.database.AppDbHelper;
import com.example.artify.utils.Formatter;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.List;

public class RevenueActivity extends AppCompatActivity {

    private TextView txtRevenue, txtEmpty;
    private RecyclerView rv;
    private OrderHistoryAdapter adapter;
    private OrderDao orderDao;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revenue);

        MaterialToolbar tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        tb.setNavigationOnClickListener(v -> onBackPressed());


        txtRevenue = findViewById(R.id.txtRevenue);
        txtEmpty = findViewById(R.id.txtEmpty);
        rv = findViewById(R.id.rvOrders);
        rv.setLayoutManager(new LinearLayoutManager(this));

        SQLiteDatabase db = new AppDbHelper(this).getReadableDatabase();
        orderDao = new OrderDao(db);

        adapter = new OrderHistoryAdapter(orderId -> {
            // TODO: mở OrderDetailActivity nếu muốn
        });
        rv.setAdapter(adapter);

        loadData();
    }

    @Override protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        double total = orderDao.getTotalRevenue();
        txtRevenue.setText(Formatter.vnd(total));

        List<OrderDao.OrderRow> rows = orderDao.listOrders();
        adapter.submit(rows);
        txtEmpty.setVisibility(rows.isEmpty() ? View.VISIBLE : View.GONE);
    }
}
