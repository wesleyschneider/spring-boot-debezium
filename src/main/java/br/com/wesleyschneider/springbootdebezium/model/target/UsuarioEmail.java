package br.com.wesleyschneider.springbootdebezium.model.target;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "usuario_email", schema = "target")
public class UsuarioEmail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cd_email", unique = true)
    private int cdEmail;

    @Column(name = "email")
    private String email;

    @Column(name = "dt_alteracao")
    private LocalDateTime dataAlteracao;

    @ManyToOne
    @JoinColumn(name = "cd_usuario")
    private Usuario usuario;
}
