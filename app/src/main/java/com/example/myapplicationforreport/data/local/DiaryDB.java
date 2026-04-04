package com.example.myapplicationforreport.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

/*
Room DB 설정
현재는 fallbackToDestructiveMigration를 사용해 DB 버전 변경 시 기존 데이터 삭제
만약 스키마가 바뀔 때 Migration 전략이 필요하다면 아래와 같이 구현
.addMigrations(new Migration(1, 2) { ... }
 */
@Database(entities = {DiaryEntity.class}, version = 1, exportSchema = false)
@TypeConverters({MoodConverter.class})  // MOOD enum <-> String 변환
public abstract class DiaryDB extends RoomDatabase{

    private static final String DB_NAME = "diary_db";
    // 멀티스레드 환경에서 안전한 싱글톤 구현을 위한 Volatile
    private static volatile DiaryDB instance;
    // DAO 접근자
    public abstract DiaryDao diaryDao();
    // Singleton
    public static DiaryDB getInstance(Context context) {
        if (instance == null) {
            synchronized (DiaryDB.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            DiaryDB.class,
                            DB_NAME)
                            // true: Migration 실패 시 모든 테이블 삭제 후 재생성, false: 해당 테이블만 삭제 후 재생성
                            .fallbackToDestructiveMigration(false)
                            .build();
                }
            }
        }
        return instance;
    }
}
