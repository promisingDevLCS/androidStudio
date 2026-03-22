# 📌주제: 일기장 앱, 모의 주식 게임 앱

# 📌디렉토리 구조
```
com.example.앱이름
│
├── data (앱 내의 데이터 처리)
│   ├── local
│   │   ├── DiaryDatabase.kt
│   │   ├── DiaryDao.kt
│   │   └── DiaryEntity.kt
│   │
│   └── repository
│       └── DiaryRepository.kt
│
├── ui (앱 화면 구성)
│   ├── main
│   │   └── MainActivity.kt
│   │
│   ├── diarylist
│   │   ├── DiaryListFragment.kt
│   │   ├── DiaryListAdapter.kt
│   │   └── DiaryListViewModel.kt
│   │
│   ├── diarydetail
│   │   ├── DiaryDetailFragment.kt
│   │   └── DiaryDetailViewModel.kt
│   │
│   └── write
│       ├── WriteDiaryFragment.kt
│       └── WriteDiaryViewModel.kt
│
├── utils (공통 기능)
│   ├── DateConverter.kt
│   └── Constants.kt
│
└── di (의존성 주입, 선택사항)
    └── AppModule.kt
```


# 📌기능
## - 일기장
  - 일기 생성, 수정, 삭제 기능
  - 하단의 메뉴 탭을 이용한 화면 전환
## - 모의 주식 게임
  - 실시간 가상 뉴스 피드
  - 가상 경제 지표 반영(금리, 환율, GDP 성장률 등)
  - 포트폴리오 구현(차트, 그래프)
  - 매수, 매도 기능 구현

# 📌목표
안드로이드 스튜디오 기능을 익히고 실력 향상을 위함
