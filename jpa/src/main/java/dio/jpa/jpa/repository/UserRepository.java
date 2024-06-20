package dio.jpa.jpa.repository;

import java.util.Optional;

//import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dio.jpa.jpa.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    
    @Query("SELECT u FROM User u JOIN FETCH u.roles WHERE u.username = (:username)")
    public Optional<User> findByUsername(@Param("username") String username);

    public boolean existsByUsername(String username);
}
