package com.pcchecker;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.pcchecker.model.PCComponent;
import com.pcchecker.utils.SuggestionUtils;

import java.util.List;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity {

    private PCComponent component;
    private BuildManager buildManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        buildManager = BuildManager.getInstance();

        component = (PCComponent) getIntent().getSerializableExtra("component");

        if (component == null) {
            finish();
            return;
        }

        ImageView ivImage = findViewById(R.id.iv_detail_image);
        TextView tvName = findViewById(R.id.tv_detail_name);
        TextView tvBrand = findViewById(R.id.tv_detail_brand);
        TextView tvPrice = findViewById(R.id.tv_detail_price);
        TextView tvDescription = findViewById(R.id.tv_detail_description);
        TextView tvSpecs = findViewById(R.id.tv_detail_specs);
        TextView tvScore = findViewById(R.id.tv_detail_score);
        TextView tvPairing = findViewById(R.id.tv_detail_pairing);
        Button btnSelect = findViewById(R.id.btn_detail_select);

        tvName.setText(component.getName());
        tvBrand.setText(component.getBrand());
        tvPrice.setText(component.getPriceRange());
        tvDescription.setText(component.getDescription());
        tvSpecs.setText(component.getSpecSummary());
        tvScore.setText("Performance Score: " + component.getPerformanceScore());

        // Setup Pairing Suggestions
        List<String> suggestions = SuggestionUtils.getPairingSuggestions(component);
        if (suggestions.isEmpty()) {
            findViewById(R.id.card_suggestions).setVisibility(android.view.View.GONE);
        } else {
            StringBuilder sb = new StringBuilder();
            for (String s : suggestions) {
                sb.append("• ").append(s).append("\n");
            }
            tvPairing.setText(sb.toString().trim());
        }

        Glide.with(this)
                .load(component.getImageUrl())
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_report_image)
                .into(ivImage);

        btnSelect.setOnClickListener(v -> {
            buildManager.setComponent(component);
            Toast.makeText(this, component.getName() + " added to build!", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
