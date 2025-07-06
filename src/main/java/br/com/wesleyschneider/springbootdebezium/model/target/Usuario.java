package br.com.wesleyschneider.springbootdebezium.model.target;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "usuario", schema = "target")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cd_usuario", unique = true)
    private int cdUsuario;

    @Column(name = "nome")
    private String nome;

    @Column(name = "nome_social")
    private String nomeSocial;

    @Column(name = "cd_estudante")
    private int cdEstudante;

    @Column(name = "dt_alteracao")
    private LocalDateTime dataAlteracao;

    @OneToMany(mappedBy = "usuario", fetch = FetchType.EAGER)
    private List<UsuarioEmail> emails;
}
