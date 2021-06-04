package com.projeto.algafood.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "tb_restaurante")
public class Restaurante implements Serializable {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(name = "taxa_frete", nullable = false)
    private BigDecimal taxaFrete;

    @ManyToOne
    @JoinColumn(name = "cozinha_id", nullable = false)
    private Cozinha cozinha;

    /**
     * @author Dan Araújo
     * @date 03-06-2021
     * Por se tratar de um ManyToMany, precisamos criar uma tabela intermediária
     * Aqui na classe de Restaurante, criamos essa tabela e damos os nomes dos atributos nessa tabela
     * No inverseJoinColumn, informamos o nome do atributo de pagamentos
     */
    @ManyToMany()
    @JoinTable(name = "restaurante_forma_pagamento",
    joinColumns = @JoinColumn(name = "restaurante_id"),
    inverseJoinColumns = @JoinColumn(name = "forma_pagamento_id"))
    List<FormaPagamento> pagamentos = new ArrayList();

}
