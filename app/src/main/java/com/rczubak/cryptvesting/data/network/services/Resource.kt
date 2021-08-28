package com.rczubak.cryptvesting.data.network.services

class Resource<out T>(val status: Status, val code: Int, val data: T?, val message: String?) {
    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }

    companion object {
        fun <T> success(data: T? = null, code: Int = 200): Resource<T> {
            return Resource(Status.SUCCESS, code, data, null)
        }

        fun <T> error(message: String?, code: Int = -1, data: T? = null): Resource<T> {
            return Resource(Status.ERROR, code, data, message)
        }

        fun <T> loading(data: T, code: Int = 0): Resource<T> {
            return Resource(Status.LOADING, code, data, null)
        }
    }

    fun isSuccess() = status == Status.SUCCESS
}