package com.seonjk.mygithubrepo.data.response

import com.seonjk.mygithubrepo.data.database.GithubRepoEntity

data class GithubRepoSearchResponse(
    val totalCount: Int,
    val items: List<GithubRepoEntity>
)
