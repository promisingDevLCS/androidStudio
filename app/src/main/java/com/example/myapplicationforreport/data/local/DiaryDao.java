package com.example.myapplicationforreport.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/*
Room DAO (쿼리 메서드)
 */
@Dao
public interface DiaryDao {

    // 전체 조회(최신순)
    @Query("SELECT * FROM diary ORDER BY created_at DESC")
    LiveData<List<DiaryEntity>> getAll();

    // 단건 조회
    @Query("SELECT * FROM diary WHERE id = :id LIMIT 1")
    LiveData<DiaryEntity> getDiaryById(int id);

    // 키워드 검색(제목 + 내용)
    @Query("SELECT * FROM diary WHERE title LIKE '%' || :keyword || '%' OR content LIKE '%' || :keyword || '%' ORDER BY created_at DESC")
    LiveData<List<DiaryEntity>> search(String keyword);

    // 삽입(동일한 ID로 삽입 시 덮어쓰기)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(DiaryEntity diary);
    // 수정
    @Update
    void update(DiaryEntity diary);
    // 삭제(단건)
    @Delete
    void delete(DiaryEntity diary);
    // 전체 삭제
    @Query("DELETE FROM diary")
    void deleteAll();

}
