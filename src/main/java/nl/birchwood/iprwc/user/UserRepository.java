package nl.birchwood.iprwc.user;

import nl.birchwood.iprwc.user.model.AppUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends PagingAndSortingRepository<AppUser, UUID> {
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM AppUser u WHERE u.username like :username")
    boolean existsUserByUsername(@Param("username") String username);

    Optional<AppUser> findByUsername(@Param("username") String username);
}
