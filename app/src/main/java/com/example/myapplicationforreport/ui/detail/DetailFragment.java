package com.example.myapplicationforreport.ui.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.myapplicationforreport.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DetailFragment extends Fragment {

    private DetailViewModel viewModel;
    private int diaryId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // getArguments 대신 requireArguments 사용
        diaryId = DetailFragmentArgs.fromBundle(requireArguments()).getDiaryId();
    }

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(DetailViewModel.class);
        setupMenu();
        observeViewModel(view);
    }

    private void setupMenu() {
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
                inflater.inflate(R.menu.menu_detail, menu);
            }
            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.action_edit) {
                    DetailFragmentDirections.ActionDetailToEdit action =
                            DetailFragmentDirections.actionDetailToEdit().setDiaryId(diaryId);
                    Navigation.findNavController(requireView()).navigate(action);
                    return true;
                }
                if (item.getItemId() == R.id.action_delete) {
                    showDeleteDialog();
                    return true;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    private void observeViewModel(View view) {
        viewModel.getDiary(diaryId).observe(getViewLifecycleOwner(), diary -> {
            if (diary == null) return;
            ((TextView) view.findViewById(R.id.tvTitle)).setText(diary.getTitle());
            ((TextView) view.findViewById(R.id.tvContent)).setText(diary.getContent());
            ((TextView) view.findViewById(R.id.tvMood)).setText(diary.getMood().toString());
            ((TextView) view.findViewById(R.id.tvDate)).setText(
                    new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm", Locale.KOREA)
                            .format(new Date(diary.getCreatedAt())));
        });
    }

    private void showDeleteDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle("일기 삭제")
                .setMessage("이 일기를 삭제하시겠습니까?")
                .setPositiveButton("삭제", (dialog, which) ->
                        viewModel.getDiary(diaryId).observe(getViewLifecycleOwner(), diary ->
                        {
                            if (diary != null) {
                                viewModel.delete(diary);
                                Navigation.findNavController(requireView()).popBackStack();
                            }
                        }))
                .setNegativeButton("취소", null)
                .show();
    }
}