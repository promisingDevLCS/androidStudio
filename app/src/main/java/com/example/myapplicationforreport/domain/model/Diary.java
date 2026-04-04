package com.example.myapplicationforreport.domain.model;

import com.example.myapplicationforreport.data.local.Mood;

/*
일기 데이터 클래스
- 두 개의 생성자
    - 새로 작성시엔 id=0으로 초기화하고 Room이 autoGenerate로 채우도록 둠
    - DB에서 불러올 땐 id가 있는 생성자를 사용
- withUpdatedContent()
    - 기존 객체를 직접 수정하는 대신 수정된 새 객체를 반환
    - createdAt은 유지하고 updatedAt만 갱신할 때 사용
 */
public class Diary {

    private int id;
    private String title;
    private String content;
    private Mood mood;
    private long createdAt;
    private long updatedAt;

    // 신규 작성(id 없음)
    public Diary(String title, String content, Mood mood, long createdAt, long updatedAt) {
        this.id = 0;
        this.title = title;
        this.content = content;
        this.mood = mood;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // DB에서 불러오는 경우
    public Diary(int id, String title, String content, Mood mood, long createdAt, long updatedAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.mood = mood;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getter / Setter
    public int getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }

//    void setId(int id) {
//        this.id = id;
//    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Mood getMood() {
        return mood;
    }

    public void setMood(Mood mood) {
        this.mood = mood;
    }

    public long getCreatedAt() {
        return createdAt;
    }
//
//    public void setCreatedAt(long createdAt) {
//        this.createdAt = createdAt;
//    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    // 수정본 생성
    public Diary withUpdatedContent(String newTitle, String newContent, Mood newMood, long updatedAt) {
        return new Diary(
                this.id,
                newTitle,
                newContent,
                newMood,
                this.createdAt,
                updatedAt
        );
    }

    @Override
    public String toString() {
        return "Diary{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", mood='" + mood + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
