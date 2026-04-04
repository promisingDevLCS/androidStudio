//package com.example.myapplicationforreport.ui.list;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.ImageButton;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.lifecycle.ViewModelProvider;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.myapplicationforreport.R;
//import com.example.myapplicationforreport.ui.detail.DetailActivity;
//import com.example.myapplicationforreport.ui.edit.EditActivity;
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//
///*
//- TextWatcher로 검색창 입력을 실시간으로 viewModel.search()에 연결
//- adapter.submitList()는 ListAdapter가 DiffUtil로 변경된 항목만 갱신
// */
//public class ListActivity extends AppCompatActivity {
//
//    private ListViewModel viewModel;
//    private DiaryAdapter adapter;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_list);
//
//        viewModel = new ViewModelProvider(this).get(ListViewModel.class);
//
//        setupRecyclerView();
//        setupSearchBar();
//        setupFab();
//        observeViewModel();
//    }
//
//    // RecyclerView 설정
//    private void setupRecyclerView() {
//        adapter = new DiaryAdapter(
//                diary -> {
//                    // 항목 클릭 -> 상세 화면
//                    Intent intent = new Intent(this, DetailActivity.class);
//                    intent.putExtra(DetailActivity.EXTRA_DIARY_ID, diary.getId());
//                    startActivity(intent);
//                },
//                diary -> {
//                    // 항목 길게 클릭 -> 삭제
//                    viewModel.delete(diary);
//                }
//        );
//
//        RecyclerView recyclerView = findViewById(R.id.recyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(adapter);
//    }
//
//    // 검색창 세팅
//    private void setupSearchBar() {
//        EditText searchBar = findViewById(R.id.editSearch);
//        searchBar.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void afterTextChanged(Editable editable) {}
//
//            @Override
//            public void beforeTextChanged(CharSequence sequence, int start, int count, int after) {
//                viewModel.search(sequence.toString());
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
//        });
//
//        ImageButton btnClear = findViewById(R.id.btnClear);
//        btnClear.setOnClickListener(v -> {
//            searchBar.setText("");
//            viewModel.clearSearch();
//        });
//    }
//
//    // Fab 세팅(새 일기 작성)
//    private void setupFab() {
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(v ->
//            startActivity(new Intent(this, EditActivity.class))
//        );
//    }
//
//    // ViewModel observe
//    private void observeViewModel() {
//        viewModel.diaries.observe(this, diaries -> {
//            adapter.submitList(diaries);
//        });
//    }
//}
