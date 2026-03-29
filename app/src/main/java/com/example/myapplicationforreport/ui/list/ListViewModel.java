package com.example.myapplicationforreport.ui.list;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.myapplicationforreport.data.repository.DiaryRepositoryImpl;
import com.example.myapplicationforreport.domain.model.Diary;
import com.example.myapplicationforreport.domain.usecase.DeleteDiaryUseCase;
import com.example.myapplicationforreport.domain.usecase.GetDiariesUseCase;

import java.util.List;

/*
- TransFormations.switchMap()을 사용하여 LiveData가 바뀔 때마다 자동으로 새 쿼리를 실행
- 검색창 입력을 search(keyword)에 연결하면 별도 처리 없이 목록이 자동 갱신
 */
public class ListViewModel extends AndroidViewModel {

    private final GetDiariesUseCase getDiariesUseCase;
    private final DeleteDiaryUseCase deleteDiaryUseCase;
    private final MutableLiveData<String> keyword = new MutableLiveData<>();

    public final LiveData<List<Diary>> diaries;

    public ListViewModel(@NonNull Application application, LiveData<List<Diary>> diaries) {
        super(application);
        DiaryRepositoryImpl repository = new DiaryRepositoryImpl(application);
        this.getDiariesUseCase = new GetDiariesUseCase(repository);
        this.deleteDiaryUseCase = new DeleteDiaryUseCase(repository);
        // method Reference 사용
        // = this.diaries = Transformations.switchMap(keyword, keyword -> getDiariesUseCase.execute(keyword));
        this.diaries = Transformations.switchMap(keyword, getDiariesUseCase::execute);
    }

    // 검색
    public void search(String keyword) {
        this.keyword.setValue(keyword);
    }
    public void clearSearch() {
        this.keyword.setValue(null);
    }
    // 삭제
    public void delete(Diary diary) {
        deleteDiaryUseCase.execute(diary);
    }
    public void deleteAll() {
        deleteDiaryUseCase.execute();
    }
}
