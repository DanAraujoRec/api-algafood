package com.projeto.algafood.controllers;

import com.projeto.algafood.entities.Cozinha;
import com.projeto.algafood.exceptions.EntidadeEmUsoException;
import com.projeto.algafood.exceptions.EntidadeNaoEncontradaException;
import com.projeto.algafood.repositories.CozinhaRepository;
import com.projeto.algafood.services.CozinhaService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/algafood-api-v1/cozinhas")
public class CozinhaController {

    @Autowired
    public CozinhaService service;

    @Autowired
    public CozinhaRepository repository;

    @GetMapping
    public ResponseEntity<Page<Cozinha>> listar(Pageable pageable) {
        Page cozinha = repository.findAll(pageable);

        if(!cozinha.isEmpty()) {
            return ResponseEntity.ok(cozinha);
        }

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscar(@PathVariable Long id) {

        try {
           service.buscar(id);
        }catch (EntidadeNaoEncontradaException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Cozinha> adicionar(@RequestBody Cozinha cozinha) {

        service.salvar(cozinha);
        return ResponseEntity.status(HttpStatus.CREATED).body(cozinha);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id,
                                             @RequestBody Cozinha cozinha) {
        try {
            Cozinha cozinhaAtual = service.buscar(id);

            if (cozinhaAtual != null) {
                BeanUtils.copyProperties(cozinha, cozinhaAtual, "id");

                Cozinha cozinhaSalva = service.salvar(cozinhaAtual);
                return ResponseEntity.ok(cozinhaSalva);
            }
        }catch (EntidadeNaoEncontradaException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> remover(@PathVariable Long id) {
        try {
            service.excluir(id);
            return ResponseEntity.noContent().build();

        } catch (EntidadeNaoEncontradaException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

        } catch (EntidadeEmUsoException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(e.getMessage());
        }
    }
}
