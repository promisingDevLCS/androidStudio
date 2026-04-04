package com.example.myapplicationforreport.ui.list;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplicationforreport.R;

public class ListFragment extends Fragment {

    private ListViewModel viewModel;
    private DiaryAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ListViewModel.class);
        setupRecyclerView(view);
        setupSearchBar(view);
        observeViewModel();
    }

    private void setupRecyclerView(View view) {
        adapter = new DiaryAdapter(
                diary -> {
                    // Navigation Component로 diaryId 전달하며 상세 화면 이동
                    ListFragmentDirections.ActionListToDetail action =
                            ListFragmentDirections.actionListToDetail(diary.getId());
                    Navigation.findNavController(view).navigate(action);
                },
                diary -> viewModel.delete(diary)
        );
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);
    }

    private void setupSearchBar(View view) {
        EditText editSearch = view.findViewById(R.id.editSearch);
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.search(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });
        ImageButton btnClear = view.findViewById(R.id.btnClear);
        btnClear.setOnClickListener(v -> {
            editSearch.setText("");
            viewModel.clearSearch();
        });
    }

    private void observeViewModel() {
        viewModel.diaries.observe(getViewLifecycleOwner(),
                diaries -> adapter.submitList(diaries));
    }
}