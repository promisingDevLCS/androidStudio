package com.example.myapplicationforreport.domain.repository;

import androidx.lifecycle.LiveData;

import com.example.myapplicationforreport.domain.model.Diary;

import java.util.List;

/*
DB 인터페이스
- Data레이어의 구현체를 직접 참조하지 않도록 분리
 */
public interface DiaryRepository {

    LiveData<List<Diary>> getAllDiaries();
    LiveData<Diary> getDiaryById(int id);
    LiveData<List<Diary>> searchDiaries(String keyword);
    void saveDiary(Diary diary);
    void updateDiary(Diary diary);
    void deleteDiary(Diary diary);
    void deleteAllDiaries();
}
