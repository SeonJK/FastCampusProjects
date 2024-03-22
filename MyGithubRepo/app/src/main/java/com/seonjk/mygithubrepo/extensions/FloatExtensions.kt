package com.seonjk.mygithubrepo.extensions

import android.content.res.Resources

// internal 가시성 변경자 - 같은 모듈 내에서만 볼 수 있다. 모듈이라 함은 한꺼번에 컴파일되는 단위
internal fun Float.fromDpToPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()