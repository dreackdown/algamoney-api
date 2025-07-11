package dev.hugofaria.algamoney.api.repository;

import dev.hugofaria.algamoney.api.model.Categoria;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    
}
