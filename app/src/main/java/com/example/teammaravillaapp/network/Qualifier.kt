package com.example.teammaravillaapp.network

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class FakeRepo

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RemoteRepo