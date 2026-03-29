package com.example.myapplicationforreport.ui.detail;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplicationforreport.R;
import com.example.myapplicationforreport.ui.edit.EditActivity;
import com.google.android.material.appbar.MaterialToolbar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/*
- EXTRA_DIARY_ID를 Intent로 받아 단건 조회
- 툴바 메뉴에서 수정/삭제를 처리
- EditActivity로 이동 시 id 전달
 */
public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_DIARY_ID = "extra_diary_id";

    private DetailViewModel viewModel;
    private int diaryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        diaryId = getIntent().getIntExtra(EXTRA_DIARY_ID, -1);
        viewModel = new ViewModelProvider(this).get(DetailViewModel.class);

        setupToolbar();
        observeViewModel();
    }

    // Toolbar
    private void setupToolbar() {
         MaterialToolbar toolbar = findViewById(R.id.toolbar);
         setSupportActionBar(toolbar);
         toolbar.setNavigationOnClickListener(v -> finish());

         toolbar.setOnMenuItemClickListener(item -> {
             if (item.getItemId() == R.id.action_edit) {
                 Intent intent = new Intent(this, EditActivity.class);
                 intent.putExtra(EditActivity.EXTRA_DIARY_ID, diaryId);
                 startActivity(intent);
                 return true;
             }
             if (item.getItemId() == R.id.action_delete) {
                 showDeleteDialog();
                 return true;
             }
             return false;
         });
    }
    // ViewModel observe
    private void observeViewModel() {
        viewModel.getDiary(diaryId).observe(this, diary -> {
            if (diary == null) return;

            TextView tvTitle = findViewById(R.id.tvTitle);
            TextView tvContent = findViewById(R.id.tvContent);
            TextView tvMood = findViewById(R.id.tvMood);
            TextView tvDate = findViewById(R.id.tvDate);

            tvTitle.setText(diary.getTitle());
            tvContent.setText(diary.getContent());
            tvMood.setText(diary.getMood().toString());
            tvDate.setText(formatDate(diary.getCreatedAt()));
        });
    }
    // 삭제 확인 Dialog
    private void showDeleteDialog() {
        new AlertDialog.Builder(this)
                .setTitle("일기 삭제")
                .setMessage("정말로 삭제하시겠습니까?")
                .setPositiveButton("삭제", (dialog, which) -> {
                    viewModel.getDiary(diaryId).getValue();

                    // LiveData 최신값 사용
                    viewModel.getDiary(diaryId).observe(this, diary -> {
                        if (diary == null) return;
                        viewModel.delete(diary);
                    });
                    finish();
                })
                .setNegativeButton("취소", null)
                .show();
    }
    // 날짜 포맷
    private String formatDate(long timestamp) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREA);
        return simpleDateFormat.format(new Date(timestamp));
    }

}
