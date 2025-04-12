@Service
class EmailService(
    @Value("\${app.email.sender}") private val senderEmail: String,
    private val javaMailSender: JavaMailSender
) {
    fun sendVerificationCode(email: String, code: String) {
        val message = SimpleMailMessage().apply {
            setFrom(senderEmail)
            setTo(email)
            subject = "Код подтверждения для EcoVklad"
            text = """
                Ваш код подтверждения: $code
                Код действителен в течение 5 минут.
            """.trimIndent()
        }
        javaMailSender.send(message)
    }
}