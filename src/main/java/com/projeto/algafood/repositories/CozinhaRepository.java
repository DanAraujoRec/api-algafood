package com.projeto.algafood.repositories;

import com.projeto.algafood.entities.Cozinha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CozinhaRepository extends JpaRepository<Cozinha, Long> {

    List<Cozinha> findTodasByNome(String nome);
    Optional<Cozinha> findByNome(String nome);
    Optional<Cozinha> findFirstByNomeContaining(String nome);
    List<Cozinha> findTop2ByNomeContaining(String nome);
    //retorna true ou false
    boolean existsByNome(String nome);
    List<Cozinha> findTodasByNomeContaining(String nome);
}
