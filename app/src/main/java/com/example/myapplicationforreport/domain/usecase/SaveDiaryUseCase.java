package com.example.myapplicationforreport.domain.usecase;

import com.example.myapplicationforreport.domain.model.Diary;
import com.example.myapplicationforreport.domain.repository.DiaryRepository;

/*
신규/수정을 'id==0' 으로 구분하고 저장 전에 제목, 내용 유효성 검사 수행
createdAt은 신규일 때만 설정하고 수정 시엔  withUpdatedContent()로 기존 값 유지
 */
public class SaveDiaryUseCase {

    private final DiaryRepository repository;

    public SaveDiaryUseCase(DiaryRepository repository) {
        this.repository = repository;
    }

    public void execute(Diary diary) {
        // 유효성 검사
        if (!isValid(diary)){
            throw new IllegalArgumentException("Invalid diary/ 제목 또는 내용이 비어있습니다.");
        }
        long now = System.currentTimeMillis();
        // 신규
        if (diary.getId() == 0){
            Diary toSave = new Diary(
                    diary.getTitle().trim(),
                    diary.getContent().trim(),
                    diary.getMood(),
                    now,
                    now
            );
            repository.saveDiary(toSave);
        }
        // 수정
        else {
            Diary toUpdate = diary.withUpdatedContent(
                    diary.getTitle().trim(),
                    diary.getContent().trim(),
                    diary.getMood(),
                    now
            );
            repository.updateDiary(toUpdate);
        }
    }

    // 입력 유효성 검사(제목, 내용이 빈 칸인지 검사)
    private boolean isValid(Diary diary) {
        return diary != null
                && diary.getTitle() != null
                && !diary.getTitle().trim().isEmpty()
                && diary.getContent() != null
                && !diary.getContent().trim().isEmpty();
    }
}
