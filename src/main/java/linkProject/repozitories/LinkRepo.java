package linkProject.repozitories;

import linkProject.models.Link;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LinkRepo extends JpaRepository<Link, Long> {
    Optional<Link> findByShortUrl(String shortUrl);
    List<Link> findByUserId(long userId);

    @Modifying
    @Query(value = "delete from links where expiration_time < :currentTime", nativeQuery = true)
    void deleteByExpirationTimeLessThan(@Param("currentTime") long currentTime);

    @Query(value = "SELECT * FROM links WHERE expiration_time < :currentTime", nativeQuery = true)
    List<Link> findExpiredLinks(@Param("currentTime") long currentTime);

}
