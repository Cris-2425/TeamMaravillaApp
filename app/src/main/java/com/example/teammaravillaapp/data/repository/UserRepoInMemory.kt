package com.example.teammaravillaapp.data.repository

class UserRepoInMemory: IUserRepo {

    private val users: ArrayList<UserDTO> = ArrayList()

    constructor()

    override fun getUser(
        id: Int,
        onError: (Throwable) -> Unit,
        onSuccess: (UserDTO) -> Unit
    ) {
        val u = users.find { it.id == id }

        if( u != null) {

        onSuccess(u)
        } else {

        onError(Throwable("User not found"))
        }
    }

    override fun login(
        email: String,
        password: String,
        onError: (Throwable) -> Unit,
        onSuccess: (UserDTO) -> Unit
    ) {
        TODO("Not yet implemented")
    }
}