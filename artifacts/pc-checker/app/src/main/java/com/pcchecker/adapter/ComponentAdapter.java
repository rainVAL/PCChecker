package com.pcchecker.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pcchecker.DetailActivity;
import com.pcchecker.R;
import com.pcchecker.model.PCComponent;

import java.util.List;

public class ComponentAdapter extends RecyclerView.Adapter<ComponentAdapter.ViewHolder> {

    public interface OnSelectListener {
        void onSelect(PCComponent component);
    }

    private List<PCComponent> items;
    private OnSelectListener listener;
    private String selectedId;

    public ComponentAdapter(List<PCComponent> items, String selectedId, OnSelectListener listener) {
        this.items = items;
        this.selectedId = selectedId;
        this.listener = listener;
    }

    public void updateList(List<PCComponent> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_component, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PCComponent component = items.get(position);
        holder.bind(component, listener, selectedId);
    }

    @Override
    public int getItemCount() { return items.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvBrand, tvSpecs, tvPrice, tvScore, tvTier, tvDescription;
        private Button btnSelect;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_component_name);
            tvBrand = itemView.findViewById(R.id.tv_component_brand);
            tvSpecs = itemView.findViewById(R.id.tv_component_specs);
            tvPrice = itemView.findViewById(R.id.tv_component_price);
            tvScore = itemView.findViewById(R.id.tv_component_score);
            tvTier = itemView.findViewById(R.id.tv_component_tier);
            tvDescription = itemView.findViewById(R.id.tv_component_description);
            btnSelect = itemView.findViewById(R.id.btn_select_component);
        }

        public void bind(PCComponent c, OnSelectListener listener, String selectedId) {
            tvName.setText(c.getName());
            tvBrand.setText(c.getBrand());
            tvSpecs.setText(c.getSpecSummary());
            tvPrice.setText(c.getPriceRange());
            tvScore.setText("Score: " + c.getPerformanceScore());
            tvTier.setText(tierLabel(c.getPriceTier()));
            tvDescription.setText(c.getDescription());

            boolean isSelected = c.getId().equals(selectedId);
            btnSelect.setText(isSelected ? "Selected" : "Select");
            btnSelect.setEnabled(!isSelected);

            btnSelect.setOnClickListener(v -> listener.onSelect(c));

            // Make the whole card clickable to view details
            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), DetailActivity.class);
                intent.putExtra("component", c);
                v.getContext().startActivity(intent);
            });
        }

        private String tierLabel(PCComponent.PriceTier tier) {
            switch (tier) {
                case BUDGET: return "Budget";
                case MID: return "Mid-range";
                case HIGH_END: return "High-end";
                default: return "";
            }
        }
    }
}
