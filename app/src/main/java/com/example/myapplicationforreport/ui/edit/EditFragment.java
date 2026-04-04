package com.example.myapplicationforreport.ui.edit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.myapplicationforreport.R;
import com.example.myapplicationforreport.data.local.Mood;
import com.example.myapplicationforreport.domain.model.Diary;
import com.google.android.material.textfield.TextInputEditText;

public class EditFragment extends Fragment {

    private EditViewModel viewModel;
    private TextInputEditText etTitle;
    private TextInputEditText etContent;
    private Spinner spinnerMood;
    private int diaryId = -1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            diaryId = EditFragmentArgs.fromBundle(getArguments()).getDiaryId();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(EditViewModel.class);

        setupViews(view);
        observeViewModel(view);

        // 수정 모드: 기존 데이터 로드
        if (diaryId != -1) {
            viewModel.getDiary(diaryId).observe(getViewLifecycleOwner(), this::onChanged);
        }
    }

    private void setupViews(View view) {
        etTitle     = view.findViewById(R.id.etTitle);
        etContent   = view.findViewById(R.id.etContent);
        spinnerMood = view.findViewById(R.id.spinnerMood);

        ArrayAdapter<Mood> moodAdapter = new ArrayAdapter<>(
                requireContext(), android.R.layout.simple_spinner_item, Mood.values());

        moodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMood.setAdapter(moodAdapter);

        view.findViewById(R.id.btnSave).setOnClickListener(v -> saveDiary());
    }

    private void observeViewModel(View view) {
        viewModel.getSaveComplete().observe(getViewLifecycleOwner(), done -> {
            if (Boolean.TRUE.equals(done)) {
                // 저장 후 이전 화면으로 복귀
                Navigation.findNavController(view).popBackStack();
            }
        });
        viewModel.getErrorMsg().observe(getViewLifecycleOwner(), msg -> {
            if (msg != null) Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
        });
    }

    private void saveDiary() {
        String title = etTitle.getText() != null ? etTitle.getText().toString() : "";
        String content = etContent.getText() != null ? etContent.getText().toString() : "";
        Mood mood = (Mood) spinnerMood.getSelectedItem();

        if (diaryId != -1) {
            viewModel.getDiary(diaryId).observe(getViewLifecycleOwner(),
                    original -> viewModel.save(title, content, mood, original));
        } else {
            viewModel.save(title, content, mood, null);
        }
    }

    private void onChanged(Diary diary) {
        if (diary == null) return;
        etTitle.setText(diary.getTitle());
        etContent.setText(diary.getContent());
        Mood [] moods = Mood.values();
        for (int i = 0; i < moods.length; i++) {
            if (moods[i] == diary.getMood()) {
                spinnerMood.setSelection(i);
                break;
            }
        }
    }
}