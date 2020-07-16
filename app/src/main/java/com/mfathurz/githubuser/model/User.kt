package com.mfathurz.githubuser.model

data class User(
    val avatar: String,
    val company: String,
    val location: String,
    val name: String,
    val repository: Int,
    val username: String
)