package br.com.wesleyschneider.springbootdebezium.mapper.source;

import br.com.wesleyschneider.springbootdebezium.converter.DateToLocalDateTime;
import br.com.wesleyschneider.springbootdebezium.model.source.Estudante;

import java.util.Date;
import java.util.Map;

public class EstudanteMapper {
    public static Estudante map(Map<String, Object> payload) {
        var estudante = new Estudante();

        estudante.setCdEstudante((Integer) payload.get("cd_estudante"));
        estudante.setNome((String) payload.get("nome"));
        estudante.setNomeSocial((String) payload.get("nome_social"));
        estudante.setEmail((String) payload.get("email"));

        var dataAlteracaoCdc = DateToLocalDateTime.convert((Date) payload.get("dt_alteracao_cdc"));
        estudante.setDataAlteracaoCdc(dataAlteracaoCdc);

        return estudante;
    }
}
