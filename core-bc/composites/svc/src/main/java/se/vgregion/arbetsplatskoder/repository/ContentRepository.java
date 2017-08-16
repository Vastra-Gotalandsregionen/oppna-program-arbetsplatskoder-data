package se.vgregion.arbetsplatskoder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.vgregion.arbetsplatskoder.domain.jpa.Content;

/**
 * @author Patrik Björk
 */
public interface ContentRepository extends JpaRepository<Content, String> {

}