package br.com.wesleyschneider.springbootdebezium.model.source;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "Estudante", schema = "dbo")
public class Estudante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cd_estudante", unique = true)
    @JsonProperty("cd_estudante")
    private int cdEstudante;

    @Column(name = "nome")
    private String nome;

    @Column(name = "nome_social")
    @JsonProperty("nome_social")
    private String nomeSocial;

    @Column(name = "email")
    private String email;

    @Column(name = "dt_alteracao_cdc")
    @JsonProperty("dt_alteracao_cdc")
    private LocalDateTime dataAlteracaoCdc;
}
