package se.vgregion.arbetsplatskoder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.vgregion.arbetsplatskoder.domain.jpa.Link;

import java.util.List;

/**
 * @author Patrik Björk
 */
public interface LinkRepository extends JpaRepository<Link, Integer> {

    List<Link> findAllByOrderById();

}