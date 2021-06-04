package com.projeto.algafood.controllers.testes;

import com.projeto.algafood.entities.Cozinha;
import com.projeto.algafood.entities.Restaurante;
import com.projeto.algafood.repositories.CozinhaRepository;
import com.projeto.algafood.repositories.RestauranteRepository;
import com.projeto.algafood.repositories.spec.RestauranteSpecs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/teste")
public class TesteController {

    @Autowired
    private CozinhaRepository repository;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @GetMapping("/cozinhas/por-nome")
    public List<Cozinha> cozinhasPorNome(String nome) {
        return repository.findTodasByNome(nome);
    }

    @GetMapping("/cozinhas/teste-variado")
    public Optional<Cozinha> cozinhaPorNome(String nome) {
        return repository.findByNome(nome);
    }

    @GetMapping("/cozinhas/primeiro-por-nome")
    public Optional<Cozinha> cozinhaFirstPorNome(String nome) {
        return repository.findFirstByNomeContaining(nome);
    }

    @GetMapping("/cozinhas/top2-por-nome")
    public List<Cozinha> cozinhaTopDoisPorNome(String nome) {
        return repository.findTop2ByNomeContaining(nome);
    }

    @GetMapping("/restaurantes/por-nome-e-frete")
    public List<Restaurante> restaurantesPorNomeFrete(String nome,
                                                      BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal) {
        return restauranteRepository.find(nome, taxaFreteInicial, taxaFreteFinal);
    }

    @GetMapping("/restaurantes/com-frete-grátis")
    public List<Restaurante> restaurantesComFreteGratis(String nome) {

        return restauranteRepository.findComFreteGratis(nome);
    }

    @GetMapping("/restaurantes/primeiro-nome")
    public Optional<Restaurante> restaurantesPrimeiroNome() {
        return restauranteRepository.buscarPrimeiro();
    }
}
