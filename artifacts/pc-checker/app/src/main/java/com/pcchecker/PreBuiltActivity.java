package com.pcchecker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pcchecker.data.PreBuiltDatabase;
import com.pcchecker.model.PCComponent;
import com.pcchecker.model.PreBuiltPC;

import java.util.List;
import java.util.Locale;

public class PreBuiltActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prebuilt);

        RecyclerView recyclerView = findViewById(R.id.recycler_prebuilt);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new PreBuiltAdapter(PreBuiltDatabase.getPreBuilts()));
    }

    private class PreBuiltAdapter extends RecyclerView.Adapter<PreBuiltAdapter.ViewHolder> {
        private final List<PreBuiltPC> templates;

        PreBuiltAdapter(List<PreBuiltPC> templates) {
            this.templates = templates;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_prebuilt, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            PreBuiltPC pc = templates.get(position);
            holder.tvName.setText(pc.getName());
            holder.tvDesc.setText(pc.getDescription());
            holder.tvParts.setText(pc.getPartsSummary());
            holder.tvPrice.setText(String.format(Locale.US, "Total Price: ₱%,.0f", pc.getTotalPrice()));

            Glide.with(holder.itemView.getContext())
                    .load(pc.getImageUrl())
                    .placeholder(R.color.primary_light)
                    .error(R.color.primary_light)
                    .into(holder.ivImage);

            holder.btnUse.setOnClickListener(v -> {
                BuildManager manager = BuildManager.getInstance();
                manager.clearBuild();
                for (PCComponent c : pc.getComponents()) {
                    manager.setComponent(c);
                }
                Toast.makeText(PreBuiltActivity.this, pc.getName() + " selected!", Toast.LENGTH_SHORT).show();
                finish();
            });
        }

        @Override
        public int getItemCount() {
            return templates.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvName, tvDesc, tvParts, tvPrice;
            android.widget.ImageView ivImage;
            Button btnUse;

            ViewHolder(View itemView) {
                super(itemView);
                tvName = itemView.findViewById(R.id.tv_pb_name);
                tvDesc = itemView.findViewById(R.id.tv_pb_desc);
                tvParts = itemView.findViewById(R.id.tv_pb_parts);
                tvPrice = itemView.findViewById(R.id.tv_pb_price);
                ivImage = itemView.findViewById(R.id.iv_pb_image);
                btnUse = itemView.findViewById(R.id.btn_select_prebuilt);
            }
        }
    }
}
