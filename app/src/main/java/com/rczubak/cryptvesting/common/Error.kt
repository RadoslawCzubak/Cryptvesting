package com.rczubak.cryptvesting.common

enum class Error(val code: Int){
    NO_CRYPTO_OWNED(1),
    UNKNOWN(-1),
    DATABASE_READ_ERROR(2),
    INTERNET_ERROR(3),
    NO_PRICES_FOR_ALL_OWNED_CRYPTO(4),
    DATABASE_WRITE_ERROR(5)
}