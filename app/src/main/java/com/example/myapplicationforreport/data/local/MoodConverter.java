package com.example.myapplicationforreport.data.local;

import androidx.room.TypeConverter;

/*
Room DB는 Enum을 지원하지 않기 때문에 Converter를 통해 enum <-> String
 */
public class MoodConverter {

    @TypeConverter
    public static String fromMood(Mood mood) {
        return mood == null ? null : mood.name(); // enum → String
    }

    @TypeConverter
    public static Mood toMood(String value) {
        return value == null ? null : Mood.valueOf(value); // String → enum
    }
}

