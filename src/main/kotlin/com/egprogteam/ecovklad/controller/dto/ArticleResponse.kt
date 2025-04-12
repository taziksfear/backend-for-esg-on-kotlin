data class ArticleResponse(
    val id: Long,
    val title: String,
    val content: String,
    val publishDate: OffsetDateTime,
    val likesCount: Int,
    val authorName: String,
    val canEdit: Boolean
)