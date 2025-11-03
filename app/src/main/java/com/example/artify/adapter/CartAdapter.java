package com.example.artify.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.artify.R;
import com.example.artify.models.CartItem;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.VH> {

    public interface Listener {
        void onInc(CartItem it);
        void onDec(CartItem it);
        void onRemove(CartItem it);
    }

    private final List<CartItem> data = new ArrayList<>();
    private final Listener listener;

    public CartAdapter(Listener listener) {
        this.listener = listener;
    }

    public void submit(List<CartItem> items) {
        data.clear();
        if (items != null) data.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        CartItem it = data.get(position);
        h.txtName.setText(it.getName());
        h.txtPrice.setText(fmt(it.getPrice()));
        h.txtQty.setText(String.valueOf(it.getQuantity()));

        Glide.with(h.img.getContext())
                .load(it.getImageUrl())
                .transform(new CenterCrop(), new RoundedCorners(12))
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_report_image)
                .into(h.img);

        h.btnInc.setOnClickListener(v -> listener.onInc(it));
        h.btnDec.setOnClickListener(v -> listener.onDec(it));
        h.btnRemove.setOnClickListener(v -> listener.onRemove(it));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        ImageView img;
        TextView txtName, txtPrice, txtQty;
        ImageButton btnInc, btnDec, btnRemove;

        VH(@NonNull View v) {
            super(v);
            img = v.findViewById(R.id.img);
            txtName = v.findViewById(R.id.txtName);
            txtPrice = v.findViewById(R.id.txtPrice);
            txtQty = v.findViewById(R.id.txtQty);
            btnInc = v.findViewById(R.id.btnInc);
            btnDec = v.findViewById(R.id.btnDec);
            btnRemove = v.findViewById(R.id.btnRemove);
        }
    }

    private static String fmt(double v) {
        return NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(v);
    }
}
