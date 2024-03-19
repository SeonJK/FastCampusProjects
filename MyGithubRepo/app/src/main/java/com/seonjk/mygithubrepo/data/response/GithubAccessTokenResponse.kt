package com.seonjk.mygithubrepo.data.response

import retrofit2.Response

data class GithubAccessTokenResponse(
    val accessToken: String,
    val scope: String,
    val tokenType: String
)
