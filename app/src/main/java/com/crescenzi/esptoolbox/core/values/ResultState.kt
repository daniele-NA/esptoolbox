package com.crescenzi.esptoolbox.core.values

/**
 * Represents UI states for handling loading, success, and error
 */
sealed class ResultState<out T> {
    data object Idle : ResultState<Nothing>()
    data object Loading : ResultState<Nothing>()
    data object Success : ResultState<Nothing>()


    /**
     * Used when we want to return an object (Android Room)
     */
    data class SuccessWithData<out T>(val data:T) :
        ResultState<T>()


    /**
     * The failure class carries a generic Exception containing a message
     */
    data class Failure<out T>(val exception:Throwable) :
        ResultState<T>()
}