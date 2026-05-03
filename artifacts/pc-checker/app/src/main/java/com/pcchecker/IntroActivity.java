package com.pcchecker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private Button btnNext, btnSkip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tab_layout);
        btnNext = findViewById(R.id.btn_next);
        btnSkip = findViewById(R.id.btn_skip);

        List<IntroPage> pages = new ArrayList<>();
        pages.add(new IntroPage("PC Checker", "Your ultimate companion for building and optimizing your next computer.", R.drawable.ic_logo));
        pages.add(new IntroPage(getString(R.string.intro_title_1), getString(R.string.intro_desc_1), R.drawable.ic_intro_tech));
        pages.add(new IntroPage(getString(R.string.intro_title_2), getString(R.string.intro_desc_2), R.drawable.ic_intro_build));
        pages.add(new IntroPage(getString(R.string.intro_title_3), getString(R.string.intro_desc_3), R.drawable.ic_intro_check));

        viewPager.setAdapter(new IntroAdapter(pages));

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {}).attach();

        btnNext.setOnClickListener(v -> {
            if (viewPager.getCurrentItem() < pages.size() - 1) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            } else {
                finishIntro();
            }
        });

        btnSkip.setOnClickListener(v -> finishIntro());

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                if (position == pages.size() - 1) {
                    btnNext.setText(R.string.intro_get_started);
                } else {
                    btnNext.setText(R.string.intro_next);
                }
            }
        });
    }

    private void finishIntro() {
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        prefs.edit().putBoolean("first_run", false).apply();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private static class IntroPage {
        String title;
        String description;
        int imageResId;

        IntroPage(String title, String description, int imageResId) {
            this.title = title;
            this.description = description;
            this.imageResId = imageResId;
        }
    }

    private static class IntroAdapter extends RecyclerView.Adapter<IntroAdapter.ViewHolder> {
        private final List<IntroPage> pages;

        IntroAdapter(List<IntroPage> pages) {
            this.pages = pages;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_intro_page, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            IntroPage page = pages.get(position);
            holder.tvTitle.setText(page.title);
            holder.tvDescription.setText(page.description);
            holder.ivImage.setImageResource(page.imageResId);
        }

        @Override
        public int getItemCount() {
            return pages.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvTitle, tvDescription;
            android.widget.ImageView ivImage;

            ViewHolder(View itemView) {
                super(itemView);
                tvTitle = itemView.findViewById(R.id.tv_intro_title);
                tvDescription = itemView.findViewById(R.id.tv_intro_description);
                ivImage = itemView.findViewById(R.id.iv_intro_image);
            }
        }
    }
}
