package se.vgregion.arbetsplatskoder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.vgregion.arbetsplatskoder.domain.jpa.FileBlob;

/**
 * @author Patrik Björk
 */
public interface FileBlobRepository extends JpaRepository<FileBlob, String> {

}