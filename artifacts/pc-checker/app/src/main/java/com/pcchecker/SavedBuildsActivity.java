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

import com.pcchecker.model.PCComponent;
import com.pcchecker.utils.BuildStorage;

import java.util.List;

public class SavedBuildsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SavedBuildAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_builds);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar_saved);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        recyclerView = findViewById(R.id.recycler_saved);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        loadBuilds();
    }

    private void loadBuilds() {
        List<String> names = BuildStorage.getSavedBuildNames(this);
        adapter = new SavedBuildAdapter(names);
        recyclerView.setAdapter(adapter);
    }

    private class SavedBuildAdapter extends RecyclerView.Adapter<SavedBuildAdapter.ViewHolder> {
        private final List<String> buildNames;

        SavedBuildAdapter(List<String> buildNames) {
            this.buildNames = buildNames;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_saved_build, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            String name = buildNames.get(position);
            holder.tvName.setText(name);
            
            List<PCComponent> components = BuildStorage.loadBuild(SavedBuildsActivity.this, name);
            holder.tvInfo.setText(components.size() + " components installed");

            holder.btnEdit.setOnClickListener(v -> {
                BuildManager manager = BuildManager.getInstance();
                manager.clearBuild();
                for (PCComponent c : components) {
                    manager.setComponent(c);
                }
                Toast.makeText(SavedBuildsActivity.this, "Build loaded for editing!", Toast.LENGTH_SHORT).show();
                finish();
            });

            holder.btnDelete.setOnClickListener(v -> {
                BuildStorage.deleteBuild(SavedBuildsActivity.this, name);
                buildNames.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, buildNames.size());
            });
        }

        @Override
        public int getItemCount() {
            return buildNames.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvName, tvInfo;
            Button btnEdit, btnDelete;

            ViewHolder(View itemView) {
                super(itemView);
                tvName = itemView.findViewById(R.id.tv_saved_name);
                tvInfo = itemView.findViewById(R.id.tv_saved_info);
                btnEdit = itemView.findViewById(R.id.btn_edit_saved);
                btnDelete = itemView.findViewById(R.id.btn_delete_saved);
            }
        }
    }
}
