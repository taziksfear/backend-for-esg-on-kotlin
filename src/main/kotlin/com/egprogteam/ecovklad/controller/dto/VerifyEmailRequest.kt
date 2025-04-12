data class VerifyEmailRequest(
    @field:NotBlank
    @field:Size(min = 6, max = 6)
    val code: String
)

data class MessageResponse(val message: String)