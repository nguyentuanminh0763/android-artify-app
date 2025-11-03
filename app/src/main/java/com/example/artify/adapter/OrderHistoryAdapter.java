package com.example.artify.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artify.R;
import com.example.artify.dao.OrderDao;
import com.example.artify.utils.Formatter;

import java.util.ArrayList;
import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.Holder> {

    public interface Listener { void onClick(int orderId); }
    private final Listener listener;
    private final List<OrderDao.OrderRow> data = new ArrayList<>();

    public OrderHistoryAdapter(Listener l) { this.listener = l; }

    public void submit(List<OrderDao.OrderRow> items) {
        data.clear();
        if (items != null) data.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order, parent, false);
        return new Holder(v);
    }

    @Override public void onBindViewHolder(@NonNull Holder h, int pos) {
        OrderDao.OrderRow r = data.get(pos);
        h.txtOrderId.setText("#" + r.id);
        h.txtCustomer.setText(r.customer == null ? "(Khách lẻ)" : r.customer);
        h.txtTotal.setText(Formatter.vnd(r.total));
        h.txtTime.setText(Formatter.dateTime(r.createdAt));
        h.txtItems.setText(" • " + r.itemCount + " sản phẩm");

        h.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onClick(r.id);
        });
    }

    @Override public int getItemCount() { return data.size(); }

    static class Holder extends RecyclerView.ViewHolder {
        TextView txtOrderId, txtCustomer, txtTotal, txtTime, txtItems;
        Holder(@NonNull View v) {
            super(v);
            txtOrderId = v.findViewById(R.id.txtOrderId);
            txtCustomer = v.findViewById(R.id.txtCustomer);
            txtTotal = v.findViewById(R.id.txtTotal);
            txtTime = v.findViewById(R.id.txtTime);
            txtItems = v.findViewById(R.id.txtItems);
        }
    }
}
