@RestController
@RequestMapping("/api/v1/projects/{projectId}/articles")
class ArticleController(
    private val articleService: ArticleService
) {
    @GetMapping
    fun getProjectArticles(@PathVariable projectId: Long): List<ArticleResponse> {
        return articleService.getArticles(projectId)
    }

    @PostMapping
    fun createArticle(
        @PathVariable projectId: Long,
        @RequestBody request: CreateArticleRequest
    ): ArticleResponse {
        return articleService.createArticle(projectId, request)
    }

    @PostMapping("/{articleId}/like")
    fun likeArticle(@PathVariable articleId: Long) {
        articleService.likeArticle(articleId)
    }

    @PostMapping("/{articleId}/share")
    fun shareArticle(@PathVariable articleId: Long) {
        articleService.shareArticle(articleId)
    }
}