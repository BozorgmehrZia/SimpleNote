package ir.sharif.simplenote.data.model

data class ErrorResponse(
    val type: String,
    val errors: List<ApiError>
)

data class ApiError(
    val attr: String,
    val code: String,
    val detail: String
)
