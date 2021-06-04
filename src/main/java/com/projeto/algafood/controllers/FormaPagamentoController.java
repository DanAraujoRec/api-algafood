package com.projeto.algafood.controllers;

import com.projeto.algafood.entities.FormaPagamento;
import com.projeto.algafood.repositories.FormaPagamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/algafood-api-v1/pagamentos")
public class FormaPagamentoController {

    @Autowired
    private FormaPagamentoRepository repository;

    @GetMapping
    public ResponseEntity<Page<FormaPagamento>> findAll(Pageable pageable) {
        Page<FormaPagamento> pagamentos = repository.findAll(pageable);

        if(!pagamentos.isEmpty()) {
            return ResponseEntity.ok(pagamentos);
        }

        return ResponseEntity.noContent().build();
    }
}
