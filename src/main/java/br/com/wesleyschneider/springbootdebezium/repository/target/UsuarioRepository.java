package br.com.wesleyschneider.springbootdebezium.repository.target;

import br.com.wesleyschneider.springbootdebezium.model.target.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByCdEstudanteAndDataAlteracaoBefore(int cdEstudante, LocalDateTime data);
}
