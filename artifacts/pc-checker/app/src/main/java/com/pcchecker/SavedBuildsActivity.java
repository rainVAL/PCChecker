package com.pcchecker;

import android.content.Intent;
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

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pcchecker.db.SavedBuild;
import com.pcchecker.model.PCComponent;
import com.pcchecker.data.ComponentDatabase;
import com.pcchecker.utils.BuildStorage;

import java.util.List;

public class SavedBuildsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView tvEmpty;
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
        tvEmpty = findViewById(R.id.tv_empty_saved);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        setupNavigation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadBuilds();
    }

    private void setupNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_saved);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_builder) {
                startActivity(new Intent(this, MainActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (id == R.id.nav_saved) {
                return true;
            } else if (id == R.id.nav_shops) {
                startActivity(new Intent(this, StoreMapActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (id == R.id.nav_about) {
                startActivity(new Intent(this, AboutActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            }
            return false;
        });
    }

    private void loadBuilds() {
        BuildStorage.getSavedBuilds(this, builds -> {
            if (builds.isEmpty()) {
                tvEmpty.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                tvEmpty.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                adapter = new SavedBuildAdapter(builds);
                recyclerView.setAdapter(adapter);
            }
        });
    }

    private class SavedBuildAdapter extends RecyclerView.Adapter<SavedBuildAdapter.ViewHolder> {
        private final List<SavedBuild> savedBuilds;

        SavedBuildAdapter(List<SavedBuild> savedBuilds) {
            this.savedBuilds = savedBuilds;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_saved_build, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            SavedBuild build = savedBuilds.get(position);
            holder.tvName.setText(build.name);
            
            int count = build.componentIds != null ? build.componentIds.size() : 0;
            holder.tvInfo.setText(getString(R.string.saved_build_count, count));

            holder.btnEdit.setOnClickListener(v -> {
                BuildManager manager = BuildManager.getInstance();
                manager.clearBuild();
                
                List<PCComponent> all = ComponentDatabase.getAll();
                if (build.componentIds != null) {
                    for (String compId : build.componentIds.values()) {
                        for (PCComponent c : all) {
                            if (c.getId().equals(compId)) {
                                manager.setComponent(c);
                                break;
                            }
                        }
                    }
                }
                
                Toast.makeText(SavedBuildsActivity.this, "Build loaded for editing!", Toast.LENGTH_SHORT).show();
                finish();
            });

            holder.btnDelete.setOnClickListener(v -> {
                BuildStorage.deleteBuild(SavedBuildsActivity.this, build);
                savedBuilds.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, savedBuilds.size());
                
                if (savedBuilds.isEmpty()) {
                    tvEmpty.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
            });
        }

        @Override
        public int getItemCount() {
            return savedBuilds.size();
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
