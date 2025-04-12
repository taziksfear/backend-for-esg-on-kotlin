@RestController
@RequestMapping("/api/v1/favorites")
class FavoriteController(
    private val favoriteService: FavoriteService
) {
    @GetMapping
    fun getUserFavorites(): List<ProjectCardResponse> {
        return favoriteService.getUserFavorites()
    }

    @PostMapping("/{projectId}")
    fun addFavorite(@PathVariable projectId: Long) {
        favoriteService.addFavorite(projectId)
    }

    @DeleteMapping("/{projectId}")
    fun removeFavorite(@PathVariable projectId: Long) {
        favoriteService.removeFavorite(projectId)
    }
}