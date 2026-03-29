package com.example.myapplicationforreport.domain.usecase;

import androidx.lifecycle.LiveData;

import com.example.myapplicationforreport.domain.model.Diary;
import com.example.myapplicationforreport.domain.repository.DiaryRepository;

import java.util.List;

public class GetDiariesUseCase {

    private final DiaryRepository repository;

    public GetDiariesUseCase(DiaryRepository repository) {
        this.repository = repository;
    }

    public LiveData<List<Diary>> execute() {
        return repository.getAllDiaries();
    }
    // 키워드 검색
    public LiveData<List<Diary>> execute(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return repository.getAllDiaries();
        }
        return repository.searchDiaries(keyword);
    }
    // DetailViewModel, EditViewModel에서 사용
    public LiveData<Diary> executeById(int id) {
        return repository.getDiaryById(id);
    }
}
