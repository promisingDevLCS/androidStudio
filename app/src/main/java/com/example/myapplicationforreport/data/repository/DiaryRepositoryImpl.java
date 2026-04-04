package com.example.myapplicationforreport.data.repository;


import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.example.myapplicationforreport.data.local.DiaryDB;
import com.example.myapplicationforreport.data.local.DiaryDao;
import com.example.myapplicationforreport.data.local.DiaryEntity;
import com.example.myapplicationforreport.domain.model.Diary;
import com.example.myapplicationforreport.domain.repository.DiaryRepository;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/*
domain의 Repository Interface 구현
domain이 data에 직접적으로 의존하는 것을 방지하기 위해 매핑을 처리
Transformations.map을 통해 DAO가 반환하는 LiveData<DiaryEntity>를 LiveData<Diary>로 변환
ExecutorService - Room의 쓰기 작업, 메인 스레드에서 호출하면 앱이 Crashed -> 쓰기 작업은 별도의 스레드에서 처리
toDomain/toEntity - Domain 모델과 DB 모델을 분리해서 각 레이어의 변경이 서로 영향주지 않도록 방지
 */
public class DiaryRepositoryImpl implements DiaryRepository {

    private final DiaryDao diaryDao;
    private final ExecutorService executorService;

    public DiaryRepositoryImpl(Application application) {
        DiaryDB db = DiaryDB.getInstance(application);
        this.diaryDao = db.diaryDao();
        this.executorService = Executors.newSingleThreadExecutor();
    }

    // 전체 조회
    @Override
    public LiveData<List<Diary>> getAllDiaries() {
        return Transformations.map(diaryDao.getAll(), diaryEntities ->
            diaryEntities.stream()
                    .map(this::toDomain)
                    .collect(Collectors.toList())
        );
    }

    // 단건 조회
    @Override
    public LiveData<Diary> getDiaryById(int id) {
        return Transformations.map(diaryDao.getDiaryById(id), this::toDomain);
    }

    // 키워드 검색
    @Override
    public LiveData<List<Diary>> searchDiaries(String keyword) {
        return Transformations.map(diaryDao.search(keyword), diaryEntities ->
                diaryEntities.stream()
                        .map(this::toDomain)
                        .collect(Collectors.toList())
        );
    }

    // 저장
    @Override
    public void saveDiary(Diary diary) {
        executorService.execute(() -> diaryDao.insert(toEntity(diary)));
    }
    // 수정
    @Override
    public void updateDiary(Diary diary) {
        executorService.execute(() -> diaryDao.update(toEntity(diary)));
    }
    // 삭제
    @Override
    public void deleteDiary(Diary diary) {
        executorService.execute(() -> diaryDao.delete(toEntity(diary)));
    }
    // 전체 삭제
    @Override
    public void deleteAllDiaries() {
        executorService.execute(diaryDao::deleteAll);
    }

    // 매핑 Entity -> Domain
    private Diary toDomain(DiaryEntity entity) {
        if (entity == null) return null;
        return new Diary(
                entity.getId(),
                entity.getTitle(),
                entity.getContent(),
                entity.getMood(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
    // 매핑 Domain -> Entity
    private DiaryEntity toEntity(Diary diary) {
        return new DiaryEntity(
                diary.getId(),
                diary.getTitle(),
                diary.getContent(),
                diary.getMood(),
                diary.getCreatedAt(),
                diary.getUpdatedAt()
        );
    }

}
