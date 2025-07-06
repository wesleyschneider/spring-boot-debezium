package br.com.wesleyschneider.springbootdebezium.repository.target;

import br.com.wesleyschneider.springbootdebezium.model.target.UsuarioEmail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioEmailRepository extends JpaRepository<UsuarioEmail, Integer> {
}
