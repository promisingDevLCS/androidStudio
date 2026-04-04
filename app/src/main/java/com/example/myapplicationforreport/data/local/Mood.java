package com.example.myapplicationforreport.data.local;

public enum Mood {
    HAPPY("😊 행복"),
    GOOD("🙂 좋음"),
    NEUTRAL("😐 보통"),
    SAD("😢 슬픔"),
    ANGRY("😠 화남");

    private final String label;

    Mood(String label) {
        this.label = label;
    }

    // Spinner 표시용 라벨
    public String getLabel() {
        return label;
    }

    // 라벨 문자열 → enum 변환 (Spinner 선택값 → enum)
    public static Mood fromLabel(String label) {
        for (Mood mood : values()) {
            if (mood.label.equals(label)) return mood;
        }
        return NEUTRAL; // 기본값
    }

    @Override
    public String toString() {
        return label; // Spinner 어댑터가 toString()을 사용하므로 라벨 반환
    }
}