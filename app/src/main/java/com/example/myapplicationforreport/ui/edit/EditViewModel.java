package com.example.myapplicationforreport.ui.edit;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplicationforreport.data.local.Mood;
import com.example.myapplicationforreport.data.repository.DiaryRepositoryImpl;
import com.example.myapplicationforreport.domain.model.Diary;
import com.example.myapplicationforreport.domain.usecase.GetDiariesUseCase;
import com.example.myapplicationforreport.domain.usecase.SaveDiaryUseCase;

/*
- 신규/수정을 save() 하나로 통합
- original == null ? 신규(save) : 수정(update)
 */
public class EditViewModel extends AndroidViewModel {

    private final SaveDiaryUseCase saveDiaryUseCase;
    private final GetDiariesUseCase getDiariesUseCase;

    // 저장 완료 이벤트(Activity에서 observe 후 finish() 호출)
    private final MutableLiveData<Boolean> saveComplete = new MutableLiveData<>();
    // 유효성 오류 메시지
    private final MutableLiveData<String> errorMsg = new MutableLiveData<>();

    public EditViewModel(@NonNull Application application) {
        super(application);
        DiaryRepositoryImpl repository = new DiaryRepositoryImpl(application);
        this.saveDiaryUseCase = new SaveDiaryUseCase(repository);
        this.getDiariesUseCase = new GetDiariesUseCase(repository);
    }

    // 기존 일기 불러오기(수정 모드)
    public LiveData<Diary> getDiary(int id) {
        return getDiariesUseCase.executeById(id);
    }

    // 저장(신규 + 수정 통합)
    public void save(String title, String content, Mood mood, Diary original) {
        try {
            Diary diary;
            // 신규 작성
            if (original == null) {
                diary = new Diary(title, content, mood, 0L, 0L);
            }
            // 수정
            else {
                diary = original.withUpdatedContent(title, content, mood, System.currentTimeMillis());
            }
            saveDiaryUseCase.execute(diary);
            saveComplete.setValue(true);
        }
        catch (IllegalArgumentException e) {
            errorMsg.setValue(e.getMessage());
        }
    }

    // LiveData 접근자
    public MutableLiveData<Boolean> getSaveComplete() {
        return saveComplete;
    }
    public MutableLiveData<String> getErrorMsg() {
        return errorMsg;
    }
}
