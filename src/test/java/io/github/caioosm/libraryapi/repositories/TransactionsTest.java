package io.github.caioosm.libraryapi.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import io.github.caioosm.libraryapi.services.TransacaoService;

@SpringBootTest
public class TransactionsTest {
    
    @Autowired
    TransacaoService transacaoService;

    /*
     * Commit -> confirmar as alteracoes
     * Rollback -> desfazer as alteracoes
     */
    @Test
    void transacaoSimples(){
        transacaoService.executar();
    }

    @Test
    void transacaoEstadoManaged(){
        transacaoService.atualizacaoSemAtualizar();;
    }
}
