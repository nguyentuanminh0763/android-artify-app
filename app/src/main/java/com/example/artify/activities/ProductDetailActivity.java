package com.example.artify.activities;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.artify.R;
import com.example.artify.adapter.ReviewAdapter;
import com.example.artify.dao.CartDao;
import com.example.artify.dao.ProductDao;
import com.example.artify.dao.ReviewDao;
import com.example.artify.database.AppDbHelper;
import com.example.artify.models.Product;
import com.example.artify.models.Review;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ProductDetailActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "product_id";

    private ProductDao productDao;
    private CartDao cartDao;

    private ImageView img;
    private TextView txtName, txtPrice, txtDesc, txtQty;
    private ImageButton btnInc, btnDec;
    private MaterialButton btnAdd;

    private Product product;
    private int qty = 1;

    private androidx.recyclerview.widget.RecyclerView rvReviews;
    private TextView tvReviewMeta;
    private ReviewDao reviewDao;
    private ReviewAdapter reviewAdapter;

    private MaterialButton btnMoreReviews;
    private static final int PREVIEW_COUNT = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        MaterialToolbar tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        tb.setNavigationOnClickListener(v -> finish());

        img = findViewById(R.id.img);
        txtName = findViewById(R.id.txtName);
        txtPrice = findViewById(R.id.txtPrice);
        txtDesc = findViewById(R.id.txtDesc);
        txtQty = findViewById(R.id.txtQty);
        btnInc = findViewById(R.id.btnInc);
        btnDec = findViewById(R.id.btnDec);
        btnAdd = findViewById(R.id.btnAdd);
        MaterialButton btnEdit      = findViewById(R.id.btnEdit);
        MaterialButton btnDelete    = findViewById(R.id.btnDelete);



        SQLiteDatabase db = new AppDbHelper(this).getWritableDatabase();
        productDao = new ProductDao(db);
        cartDao = new CartDao(db);
        reviewDao = new ReviewDao(db);
        rvReviews = findViewById(R.id.rvReviews);
        tvReviewMeta = findViewById(R.id.tvReviewMeta);
        btnMoreReviews = findViewById(R.id.btnMoreReviews);

        rvReviews.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));
        reviewAdapter = new ReviewAdapter(java.util.Collections.emptyList());
        rvReviews.setAdapter(reviewAdapter);


        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id <= 0) {
            Toast.makeText(this, "Không tìm thấy sản phẩm", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        product = productDao.getById(id);
        if (product == null) {
            Toast.makeText(this, "Sản phẩm không tồn tại", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }



        bind(product);
        loadReviews(product.getId());



        btnEdit.setOnClickListener(v -> {
            Intent i = new Intent(this, ProductFormActivity.class);
            i.putExtra("product_id", product.getId());
            startActivity(i);
        });

        btnDelete.setOnClickListener(v -> {
                    new AlertDialog.Builder(this)
                            .setTitle("Xoá sản phẩm")
                            .setMessage("Bạn có chắc muốn xoá sản phẩm này?")
                            .setPositiveButton("Xoá", (d, w) -> {
                                new CartDao(db).removeByProductId(product.getId()); // dọn giỏ (tuỳ chọn)
                                int n = productDao.remove(product.getId());
                                if (n > 0) {
                                    Toast.makeText(this, "Đã xoá sản phẩm", Toast.LENGTH_SHORT).show();
                                    setResult(RESULT_OK);
                                    finish(); // quay lại danh sách
                                } else {
                                    Toast.makeText(this, "Xoá thất bại", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("Huỷ", null)
                            .show();
                });

        setupActions();
    }

    private void bind(Product p) {
        txtName.setText(p.getName());
        txtPrice.setText(fmtVnd(p.getPrice()));
        txtDesc.setText(p.getDescription() == null ? "" : p.getDescription());

        Glide.with(this)
                .load(p.getImageUrl())
                .transform(new CenterCrop(), new RoundedCorners(24))
                .thumbnail(0.2f)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_report_image)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(img);
    }

    private void loadReviews(int productId) {
           try {
               reviewDao.seedIfEmpty(productId);
               List<Review> all = reviewDao.listByProduct(productId);

               float avg = reviewDao.averageRating(productId);
               int cnt = reviewDao.count(productId);
               tvReviewMeta.setText(String.format("(%.1f • %d lượt)", avg, cnt));

               // preview 3 dòng
               if (all.size() > PREVIEW_COUNT) {
                   reviewAdapter.submit(all.subList(0, PREVIEW_COUNT));
                   btnMoreReviews.setVisibility(View.VISIBLE);
                   btnMoreReviews.setOnClickListener(v -> {
                       reviewAdapter.submit(all);
                       btnMoreReviews.setVisibility(View.GONE);
                   });
               } else {
                   reviewAdapter.submit(all);
                   btnMoreReviews.setVisibility(View.GONE);
               }
        } catch (Exception e) {
            // Log.e("Review", "load failed", e);
            tvReviewMeta.setText("(0.0 • 0 lượt)");
            reviewAdapter.submit(java.util.Collections.emptyList());
        }
    }




    private void setupActions() {
        txtQty.setText(String.valueOf(qty));
        btnInc.setOnClickListener(v -> {
            qty++;
            txtQty.setText(String.valueOf(qty));
        });
        btnDec.setOnClickListener(v -> {
            if (qty > 1) qty--;
            txtQty.setText(String.valueOf(qty));
        });

        btnAdd.setOnClickListener(v -> {
            for (int i = 0; i < qty; i++) {
                cartDao.add(product); //
            }
            Toast.makeText(this, "Đã thêm " + qty + " • " + product.getName(), Toast.LENGTH_SHORT).show();
        });
    }

    private static String fmtVnd(double v) {
        return NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(v);
    }
}
