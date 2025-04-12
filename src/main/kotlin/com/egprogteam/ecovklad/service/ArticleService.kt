@Service
class ArticleService(
    private val articleRepository: ArticleRepository,
    private val projectRepository: ProjectRepository,
    private val securityService: SecurityService
) {
    fun createArticle(projectId: Long, request: CreateArticleRequest): ArticleResponse {
        val project = projectRepository.findByIdOrThrow(projectId)
        val userId = securityService.getCurrentUserId()
        
        // Проверка что пользователь админ проекта
        if (!project.admins.any { it.id == userId }) {
            throw AccessDeniedException("Only project admins can create articles")
        }

        val article = Article(
            title = request.title,
            content = request.content,
            publishDate = OffsetDateTime.now(),
            project = project,
            author = User(id = userId)
        )
        
        return articleRepository.save(article).toResponse()
    }
    
    // ... другие методы ...
}