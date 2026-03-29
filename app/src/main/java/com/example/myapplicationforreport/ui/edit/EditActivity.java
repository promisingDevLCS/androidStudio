package com.example.myapplicationforreport.ui.edit;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplicationforreport.R;
import com.example.myapplicationforreport.data.local.Mood;
import com.example.myapplicationforreport.data.local.MoodConverter;
import com.google.android.material.appbar.MaterialToolbar;

/*
- diaryId == -1 ? 신규 : 수정
- Mood는 Spinner로 선택, res/values/strings.xml에 정의
 */
public class EditActivity extends AppCompatActivity {

    public static final String EXTRA_DIARY_ID = "extra_diary_id";

    private EditViewModel viewModel;
    private EditText etTitle;
    private EditText etContent;
    private Spinner spMood;

    private int diaryId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        diaryId = getIntent().getIntExtra(EXTRA_DIARY_ID, -1);
        viewModel = new ViewModelProvider(this).get(EditViewModel.class);

        setupViews();
        setupToolbar();
        observeViewModel();

        // 수정 모드이면 기존 데이터 불러오기
        if (diaryId != -1) {
            loadExistingDiary();
        }
    }

    // View 초기화
    private void setupViews() {
        etTitle = findViewById(R.id.etTitle);
        etContent = findViewById(R.id.etContent);
        spMood = findViewById(R.id.spMood);

        ArrayAdapter<CharSequence> moodAdapter = ArrayAdapter.createFromResource(
                this, R.array.mood_array, android.R.layout.simple_spinner_item);

        moodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMood.setAdapter(moodAdapter);
    }
    // Toolbar
    private void setupToolbar() {
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_save) {
                saveDiary();
                return true;
        }
            return false;
        });
    }
    // ViewModel observe
    private void observeViewModel() {
        viewModel.getSaveComplete().observe(this, isSaved -> {
            if (isSaved) {
                finish();
            }
        });
        viewModel.getErrorMsg().observe(this, errorMsg -> {
            if (errorMsg != null) {
                Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }
    // 기존 일기 불러오기
    private void loadExistingDiary() {
        viewModel.getDiary(diaryId).observe(this, diary -> {
            if (diary == null) return;
            etTitle.setText(diary.getTitle());
            etContent.setText(diary.getContent());

            // 기분 Spinner 위치 복원
            ArrayAdapter adapter = (ArrayAdapter) spMood.getAdapter();
            int position = adapter.getPosition(diary.getMood().toString());

            if (position >= 0) {
                spMood.setSelection(position);
            }
        });
    }

    // 저장
    // Mood는 Spinner Position으로 매핑
    private void saveDiary() {
        String title = etTitle.getText().toString();
        String content = etContent.getText().toString();
        Mood mood = MoodConverter.toMood(spMood.getSelectedItem().toString());


        // 수정 모드: original Diary를 LiveData에서 가져와 전달
        if (diaryId != -1) {
            viewModel.getDiary(diaryId).observe(this, original -> {
                if (original == null) return;
                viewModel.save(title, content, mood, original);
            });
        } else {
            viewModel.save(title, content, mood, null);
        }
    }

}
