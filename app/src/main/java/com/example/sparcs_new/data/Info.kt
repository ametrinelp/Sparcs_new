package com.example.sparcs_new.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
data class Info(
    val title: String,
    val time: String,
    val location: String
)

val infos = listOf(
    Info("집에 갈 사람","2025.04.29. 13시", "서산시 동문동"),
    Info("콘서트에 갈 사람","2025.04.29. 14시", "고양 종합운동장"),
    Info("학교에 갈 사람","2025.04.29. 15시", "카이스트 아소사"),
    Info("놀러 갈 사람","2025.04.29. 16시", "용인 에버랜드"),
    Info("술 마시러 갈 사람","2025.04.29. 17시", "궁동"),
    Info("술 퍼마시러 갈 사람","2025.04.29. 18시", "어은동"),
    Info("갈 사람","2025.04.29. 19시", "관짝"),
    Info("놀러 갈 사람","2025.04.29. 20시", "서울 남산타워"),
    Info("집에 갈 사람","2025.04.29. 21시", "아오 집갈래"),
    Info("집에 ㄹㅇ로 갈 사람","2025.04.29. 22시", "나도 집"),
    Info("도망갈 사람","2025.04.29. 23시", "교수님과제좀그만주세요"),
)

