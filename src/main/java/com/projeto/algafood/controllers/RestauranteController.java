package com.projeto.algafood.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projeto.algafood.entities.Restaurante;
import com.projeto.algafood.exceptions.EntidadeNaoEncontradaException;
import com.projeto.algafood.repositories.RestauranteRepository;
import com.projeto.algafood.services.RestauranteService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/algafood-api-v1/restaurantes")
public class RestauranteController {

    @Autowired
    public RestauranteService service;

    @Autowired
    public RestauranteRepository repository;

    @GetMapping
    public ResponseEntity<Page<Restaurante>> listar(Pageable pageable) {

        Page restaurantes = repository.findAll(pageable);

        if (!restaurantes.isEmpty()) {
            return ResponseEntity.ok(restaurantes);
        }

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Restaurante> buscar(@PathVariable Long id) {

        Optional<Restaurante> restaurante = repository.findById(id);

        if (restaurante.isPresent()) {
            return ResponseEntity.ok(restaurante.get());
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> adicionar(@RequestBody Restaurante restaurante) {
        try {
            restaurante = service.salvar(restaurante);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(restaurante);
        } catch (EntidadeNaoEncontradaException e) {
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id,
                                       @RequestBody Restaurante restaurante) {
        try {
            Restaurante restauranteAtual = repository
                    .findById(id).orElse(null);

            if (restauranteAtual != null) {
                BeanUtils.copyProperties(restaurante, restauranteAtual,
                        "id", "pagamentos");

                restauranteAtual = service.salvar(restauranteAtual);
                return ResponseEntity.ok(restauranteAtual);
            }

            return ResponseEntity.notFound().build();

        } catch (EntidadeNaoEncontradaException e) {
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> atualizarParcial(@PathVariable Long id,
                                              @RequestBody Map<String, Object> campos) {
        Restaurante restauranteAtual = repository
                .findById(id).orElse(null);

        if (restauranteAtual == null) {
            return ResponseEntity.notFound().build();
        }

        merge(campos, restauranteAtual);

        return atualizar(id, restauranteAtual);
    }

    private void merge(Map<String, Object> dadosOrigem, Restaurante restauranteDestino) {
        ObjectMapper objectMapper = new ObjectMapper();
        Restaurante restauranteOrigem = objectMapper.convertValue(dadosOrigem, Restaurante.class);

        dadosOrigem.forEach((nomePropriedade, valorPropriedade) -> {
            Field field = ReflectionUtils.findField(Restaurante.class, nomePropriedade);
            field.setAccessible(true);

            Object novoValor = ReflectionUtils.getField(field, restauranteOrigem);

//			System.out.println(nomePropriedade + " = " + valorPropriedade + " = " + novoValor);

            ReflectionUtils.setField(field, restauranteDestino, novoValor);
        });
    }
}
