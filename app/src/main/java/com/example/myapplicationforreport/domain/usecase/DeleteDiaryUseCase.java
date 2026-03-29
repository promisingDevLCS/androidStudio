package com.example.myapplicationforreport.domain.usecase;

import com.example.myapplicationforreport.domain.model.Diary;
import com.example.myapplicationforreport.domain.repository.DiaryRepository;

/*
일기의 조회, 저장, 수정, 삭제를 담당하는 UseCase
 */
public class DeleteDiaryUseCase {

    private final DiaryRepository repository;

    public DeleteDiaryUseCase(DiaryRepository repository) {
        this.repository = repository;
    }

    // 단건 삭제
    public void execute(Diary diary) {
        if (diary == null || diary.getId() == 0) {
            throw new IllegalArgumentException("Invalid diary/ 삭제할 일기가 유효하지 않습니다.");
        }
        repository.deleteDiary(diary);
    }
    // 전체 삭제
    public void execute() {
        repository.deleteAllDiaries();
    }
}
