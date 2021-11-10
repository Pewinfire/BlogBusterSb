package net.ausiasmarch.blogBusterSB;

import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PostRepository extends JpaRepository<PostEntity, Long> {
    Page<PostEntity> findById (Long id, Pageable pageable);
    Page <PostEntity> findByTitulo(String titulo, Pageable pageable);
    Page <PostEntity> findByCuerpo(String cuerpo, Pageable pageable);
    Page <PostEntity> findByFecha(LocalDateTime fecha, Pageable pageable);
    Page <PostEntity> findByEtiquetas(String etiquetas, Pageable pageable);
}
