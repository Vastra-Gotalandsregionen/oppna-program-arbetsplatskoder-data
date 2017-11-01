package se.vgregion.arbetsplatskoder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.vgregion.arbetsplatskoder.domain.jpa.User;

import java.util.List;

/**
 * @author Patrik Björk
 */
public interface UserRepository extends JpaRepository<User, String> {

    List<User> findAllByOrderById();
}