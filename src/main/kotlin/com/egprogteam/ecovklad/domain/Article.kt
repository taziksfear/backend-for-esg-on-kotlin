@Entity
@Table(name = "articles")
class Article(
    @Column(nullable = false, length = 255)
    var title: String,
    
    @Column(nullable = false, length = 5000)
    var content: String,
    
    @Column(nullable = false)
    var publishDate: OffsetDateTime,
    
    @Column(nullable = false)
    var likesCount: Int = 0,
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    var project: Project,
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    var author: User
) : BaseEntity()