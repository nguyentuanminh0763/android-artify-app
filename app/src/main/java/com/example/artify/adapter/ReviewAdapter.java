package com.example.artify.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artify.R;
import com.example.artify.models.Review;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.VH> {
    private List<Review> data;
    public ReviewAdapter(List<Review> data) { this.data = data; }
    public void submit(List<Review> list) { this.data = list; notifyDataSetChanged(); }

    @NonNull @Override public VH onCreateViewHolder(@NonNull ViewGroup p, int v) {
        View view = LayoutInflater.from(p.getContext())
                .inflate(R.layout.item_review, p, false);
        return new VH(view);
    }

    @Override public void onBindViewHolder(@NonNull VH h, int i) {
        Review r = data.get(i);
        h.tvName.setText(r.getUserName());
        h.rating.setRating(r.getRating());
        h.tvComment.setText(r.getComment());
        // avatar → lấy chữ cái đầu
        String first = r.getUserName() != null && !r.getUserName().isEmpty()
                ? r.getUserName().substring(0,1).toUpperCase() : "?";
        h.ava.setText(first);
    }

    @Override public int getItemCount() { return data == null ? 0 : data.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView ava, tvName, tvComment; RatingBar rating;
        VH(@NonNull View v) {
            super(v);
            ava = v.findViewById(R.id.ava);
            tvName = v.findViewById(R.id.tvName);
            tvComment = v.findViewById(R.id.tvComment);
            rating = v.findViewById(R.id.rating);
        }
    }
}
