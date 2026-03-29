package com.example.myapplicationforreport.ui.detail;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.myapplicationforreport.data.repository.DiaryRepositoryImpl;
import com.example.myapplicationforreport.domain.model.Diary;
import com.example.myapplicationforreport.domain.usecase.DeleteDiaryUseCase;
import com.example.myapplicationforreport.domain.usecase.GetDiariesUseCase;

/*
- 단건 조회와 삭제 담당
- 삭제 후 Activity에서 finish()를 호출하여 이전 화면으로 이동
 */
public class DetailViewModel extends AndroidViewModel {

    private final GetDiariesUseCase getDiariesUseCase;
    private final DeleteDiaryUseCase deleteDiaryUseCase;

    public DetailViewModel(@NonNull Application application) {
        super(application);
        DiaryRepositoryImpl repository = new DiaryRepositoryImpl(application);
        this.getDiariesUseCase = new GetDiariesUseCase(repository);
        this.deleteDiaryUseCase = new DeleteDiaryUseCase(repository);
    }

    // 단건 조회
    public LiveData<Diary> getDiary(int id) {
        return getDiariesUseCase.executeById(id);
    }
    // 삭제 후 뒤로가기
    public void delete(Diary diary) {
        deleteDiaryUseCase.execute(diary);
    }
}
