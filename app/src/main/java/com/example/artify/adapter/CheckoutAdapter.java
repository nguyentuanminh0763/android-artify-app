package com.example.artify.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artify.R;
import com.example.artify.models.CartItem;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CheckoutAdapter extends RecyclerView.Adapter<CheckoutAdapter.VH> {
    private final List<CartItem> data;
    public CheckoutAdapter(List<CartItem> data) { this.data = data; }

    @NonNull @Override public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_checkout_row, parent, false);
        return new VH(v);
    }

    @Override public void onBindViewHolder(@NonNull VH h, int position) {
        CartItem it = data.get(position);
        h.txtName.setText(it.getName());
        h.txtQty.setText("x" + it.getQuantity());
        h.txtLineTotal.setText(fmt(it.getLineTotal()));
    }

    @Override public int getItemCount() { return data == null ? 0 : data.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView txtName, txtQty, txtLineTotal;
        VH(@NonNull View v) {
            super(v);
            txtName = v.findViewById(R.id.txtName);
            txtQty = v.findViewById(R.id.txtQty);
            txtLineTotal = v.findViewById(R.id.txtLineTotal);
        }
    }

    private static String fmt(double v) {
        return NumberFormat.getCurrencyInstance(new Locale("vi","VN")).format(v);
    }
}
