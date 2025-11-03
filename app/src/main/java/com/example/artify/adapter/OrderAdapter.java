//package com.example.artify.adapter;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//import com.example.artify.R;
//import com.example.artify.models.Order;
//import com.example.artify.utils.Datex;
//import com.example.artify.utils.Fmt;
//import java.util.ArrayList;
//import java.util.List;
//
//public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.VH> {
//    private List<Order> data = new ArrayList<>();
//    public void submit(List<Order> list) { data = list; notifyDataSetChanged(); }
//
//    @NonNull @Override public VH onCreateViewHolder(@NonNull ViewGroup p, int v) {
//        View view = LayoutInflater.from(p.getContext())
//                .inflate(R.layout.item_order, p, false);
//        return new VH(view);
//    }
//
//    @Override public void onBindViewHolder(@NonNull VH h, int i) {
//        Order o = data.get(i);
//        h.tvTitle.setText("#" + o.getId() + " • " + (o.getCustomerName()==null?"Khách lẻ":o.getCustomerName()));
//        h.tvSub.setText(Datex.fmtDateTime(o.getCreatedAt()));
//        h.tvTotal.setText(Fmt.vnd(o.getTotal()));
//    }
//
//    @Override public int getItemCount() { return data==null?0:data.size(); }
//
//    static class VH extends RecyclerView.ViewHolder {
//        TextView tvTitle, tvSub, tvTotal;
//        VH(@NonNull View v){
//            super(v);
//            tvTitle = v.findViewById(R.id.tvTitle);
//            tvSub = v.findViewById(R.id.tvSub);
//            tvTotal = v.findViewById(R.id.tvTotal);
//        }
//    }
//}
