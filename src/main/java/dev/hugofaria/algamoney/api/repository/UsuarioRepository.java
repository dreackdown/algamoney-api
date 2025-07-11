package dev.hugofaria.algamoney.api.repository;

import java.util.Optional;

import dev.hugofaria.algamoney.api.model.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    public Optional<Usuario> findByEmail(String email);
}
