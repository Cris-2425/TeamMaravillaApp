package com.example.teammaravillaapp.data.repository

interface IUserRepo {
    fun getUser(id: Int, onError: (Throwable) -> Unit, onSuccess: (UserDTO) -> Unit)
    fun login(email: String, password: String, onError: (Throwable) -> Unit, onSuccess: (UserDTO) -> Unit)
}