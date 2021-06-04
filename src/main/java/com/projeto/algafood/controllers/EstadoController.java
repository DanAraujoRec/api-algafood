package com.projeto.algafood.controllers;

import com.projeto.algafood.entities.Estado;
import com.projeto.algafood.exceptions.EntidadeEmUsoException;
import com.projeto.algafood.exceptions.EntidadeNaoEncontradaException;
import com.projeto.algafood.repositories.EstadoRepository;
import com.projeto.algafood.services.EstadoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/algafood-api-v1/estados")
public class EstadoController {

    @Autowired
    public EstadoService service;

    @Autowired
    public EstadoRepository repository;

    @GetMapping
    public ResponseEntity<Page<Estado>> listar(Pageable pageable) {

        Page estados = repository.findAll(pageable);

        if(!estados.isEmpty()) {
            return ResponseEntity.ok(estados);
        }

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Estado> buscar(@PathVariable Long id) {
        Optional<Estado> estado = repository.findById(id);

        if (estado.isPresent()) {
            return ResponseEntity.ok(estado.get());
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Estado> adicionar(@RequestBody Estado estado) {

        service.salvar(estado);
        return ResponseEntity.status(HttpStatus.CREATED).body(estado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Estado> atualizar(@PathVariable Long id,
                                            @RequestBody Estado estado) {
        Estado estadoAtual = repository.findById(id).orElse(null);

        if (estadoAtual != null) {
            BeanUtils.copyProperties(estado, estadoAtual, "id");

            estadoAtual = service.salvar(estadoAtual);
            return ResponseEntity.ok(estadoAtual);
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> remover(@PathVariable Long id) {
        try {
            service.excluir(id);
            return ResponseEntity.noContent().build();

        } catch (EntidadeNaoEncontradaException e) {
            return ResponseEntity.notFound().build();

        } catch (EntidadeEmUsoException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(e.getMessage());
        }
    }
}
