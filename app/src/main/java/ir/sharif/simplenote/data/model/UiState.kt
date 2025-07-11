package ir.sharif.simplenote.data.model

data class UiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)