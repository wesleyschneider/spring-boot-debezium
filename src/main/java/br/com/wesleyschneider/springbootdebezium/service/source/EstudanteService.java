package br.com.wesleyschneider.springbootdebezium.service.source;

import br.com.wesleyschneider.springbootdebezium.mapper.source.EstudanteMapper;
import br.com.wesleyschneider.springbootdebezium.model.source.Estudante;
import br.com.wesleyschneider.springbootdebezium.model.target.Usuario;
import br.com.wesleyschneider.springbootdebezium.model.target.UsuarioEmail;
import br.com.wesleyschneider.springbootdebezium.repository.target.UsuarioEmailRepository;
import br.com.wesleyschneider.springbootdebezium.repository.target.UsuarioRepository;
import br.com.wesleyschneider.springbootdebezium.service.CreateService;
import br.com.wesleyschneider.springbootdebezium.service.UpdateService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service(value = "EstudanteService")
public class EstudanteService implements CreateService, UpdateService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioEmailRepository usuarioEmailRepository;

    @Transactional
    public void create(Map<String, Object> payload) {
        var estudante = EstudanteMapper.map(payload);

        var usuario = new Usuario();
        usuario.setDataAlteracao(LocalDateTime.now());

        updateUsuario(usuario, estudante);
    }

    @Transactional
    public void update(Map<String, Object> payload) {
        var estudante = EstudanteMapper.map(payload);

        var usuarioOptional = usuarioRepository.findByCdEstudanteAndDataAlteracaoBefore(estudante.getCdEstudante(), estudante.getDataAlteracaoCdc());

        if (usuarioOptional.isEmpty()) {
            return;
        }

        var usuario = usuarioOptional.orElseGet(Usuario::new);

        updateUsuario(usuario, estudante);
    }

    private void updateUsuario(Usuario usuario, Estudante estudante) {
        usuario.setNome(estudante.getNome());
        usuario.setNomeSocial(estudante.getNomeSocial());
        usuario.setCdEstudante(estudante.getCdEstudante());
        usuario.setDataAlteracao(estudante.getDataAlteracaoCdc());

        usuarioRepository.save(usuario);

        var estudanteEmail = estudante.getEmail();
        UsuarioEmail email = new UsuarioEmail();

        var usuarioEmails = usuario.getEmails();

        if (usuarioEmails != null){
            var emailOptional = usuarioEmails.stream().filter(e -> e.getEmail().equals(estudanteEmail)).findFirst();

            if (emailOptional.isPresent()) {
                email = emailOptional.get();
            }
        }

        email.setEmail(estudanteEmail);
        email.setDataAlteracao(estudante.getDataAlteracaoCdc());
        email.setUsuario(usuario);

        usuarioEmailRepository.save(email);
    }
}
