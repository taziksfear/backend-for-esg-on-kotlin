@Service
class FavoriteService(
    private val favoriteRepository: FavoriteRepository,
    private val projectRepository: ProjectRepository,
    private val securityService: SecurityService
) {
    fun getUserFavorites(): List<ProjectCardResponse> {
        val userId = securityService.getCurrentUserId()
        return favoriteRepository.findByUserId(userId).map { it.toCardResponse() }
    }

    fun addFavorite(projectId: Long) {
        val userId = securityService.getCurrentUserId()
        if (!favoriteRepository.existsByUserIdAndProjectId(userId, projectId)) {
            val favorite = Favorite(
                user = User(id = userId),
                project = Project(id = projectId)
            )
            favoriteRepository.save(favorite)
        }
    }
    
    // ... removeFavorite ...
}