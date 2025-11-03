package com.example.artify.adapter;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.artify.R;
import com.example.artify.activities.ProductDetailActivity;
import com.example.artify.activities.ProductListActivity;
import com.example.artify.models.Product;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.VH> {

    public interface Listener {
        void onClick(Product p);
        void onAddToCart(Product p);
    }

    private final List<Product> data = new ArrayList<>();
    private final Listener listener;

    public ProductAdapter(Listener listener) { this.listener = listener; }

    public void submit(List<Product> items) {
        data.clear();
        if (items != null) data.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull @Override public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new VH(v);
    }

    @Override public void onBindViewHolder(@NonNull VH h, int pos) {
        Product p = data.get(pos);
        h.txtName.setText(p.getName());
        h.txtDesc.setText(p.getDescription());
        h.txtPrice.setText(fmt(p.getPrice()));

        Glide.with(h.img.getContext())
                .load(p.getImageUrl())
                .transform(new CenterCrop(), new RoundedCorners(16)) // bo góc mềm mại
                .thumbnail(0.15f)                                    // preview nhỏ khi tải
                .placeholder(android.R.drawable.ic_menu_gallery)     // hiển thị tạm trong lúc load
                .error(android.R.drawable.ic_menu_report_image)      // ảnh lỗi
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)      // cache thông minh
                .into(h.img);

        h.itemView.setOnClickListener(v -> { if (listener != null) listener.onClick(p); });
        h.btnAdd.setOnClickListener(v -> { if (listener != null) listener.onAddToCart(p); });
    }

    @Override public int getItemCount() { return data.size(); }




    static class VH extends RecyclerView.ViewHolder {
        ImageView img; TextView txtName, txtDesc, txtPrice; ImageButton btnAdd;
        VH(@NonNull View v) {
            super(v);
            img = v.findViewById(R.id.imgProduct);
            txtName = v.findViewById(R.id.txtName);
            txtDesc = v.findViewById(R.id.txtDesc);
            txtPrice = v.findViewById(R.id.txtPrice);
            btnAdd = v.findViewById(R.id.btnAddToCart);
        }
    }

    private static String fmt(double v){
        return NumberFormat.getCurrencyInstance(new Locale("vi","VN")).format(v);
        // ví dụ: 120.000 ₫
    }
}
