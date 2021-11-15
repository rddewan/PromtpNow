package dev.rdewan.promptnowtest.util

sealed class ResultState<out T> {
    data class  Added<out T>(val data: T): ResultState<T>()
    object  Loading: ResultState<Nothing>()
    object  Updated: ResultState<Nothing>()
    data class  Deleted<out T>(val data: T): ResultState<T>()
    data class Error(val exception: Exception) : ResultState<Nothing>()
    object  Empty: ResultState<Nothing>()
}
