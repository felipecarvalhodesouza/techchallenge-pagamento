package br.com.postech.pagamento.application.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.postech.pagamento.application.gateway.PagamentoGateway;
import br.com.postech.pagamento.domain.Pagamento;
import br.com.postech.pagamento.domain.enumeration.StatusPagamento;
import br.com.postech.pagamento.domain.exception.PagamentoInexistenteException;
import br.com.postech.pagamento.domain.exception.StatusPagamentoInvalidoException;
import br.com.postech.pagamento.domain.repository.IPagamentoQueueAdapter;

class PagamentoInteractorTest {

    @Mock
    private PagamentoGateway pagamentoGateway;
    
    @Mock
    private IPagamentoQueueAdapter pagamentoQueueAdapter;

    @InjectMocks
    private PagamentoInteractor pagamentoInteractor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveInserirPagamento() {
        Pagamento pagamento = new Pagamento();
        pagamento.setId(Long.valueOf(123456));
        when(pagamentoGateway.inserirPagamento(any(Pagamento.class))).thenReturn(pagamento);

        Pagamento result = pagamentoInteractor.inserirPagamento(pagamento);

        assertThat(result).isNotNull();
        verify(pagamentoGateway).inserirPagamento(pagamento);
    }

    @Test
    void deveAprovarPagamento() throws StatusPagamentoInvalidoException, PagamentoInexistenteException, IOException {
        String pagamentoId = "123456";
        Pagamento pagamento = new Pagamento();
        pagamento.setId(Long.valueOf(pagamentoId));
        pagamento.setStatusPagamento(StatusPagamento.PENDENTE);
        
        when(pagamentoGateway.getPagamentoPor(pagamentoId)).thenReturn(pagamento);

        pagamentoInteractor.aprovarPagamento(pagamentoId);

        verify(pagamentoGateway).getPagamentoPor(pagamentoId);
        verify(pagamentoGateway).aprovarPagamento(pagamentoId);
    }

    @Test
    void deveRecusarPagamento() throws StatusPagamentoInvalidoException, PagamentoInexistenteException {
    	String pagamentoId = "123456";
        Pagamento pagamento = new Pagamento();
        pagamento.setId(Long.valueOf(pagamentoId));
        pagamento.setStatusPagamento(StatusPagamento.PENDENTE);
        
        when(pagamentoGateway.getPagamentoPor(pagamentoId)).thenReturn(pagamento);

        pagamentoInteractor.recusarPagamento(pagamentoId);

        verify(pagamentoGateway).getPagamentoPor(pagamentoId);
        verify(pagamentoGateway).recusarPagamento(pagamentoId);
    }

    @Test
    void deveFalharAoAprovarPagamentoInexistente() throws PagamentoInexistenteException, StatusPagamentoInvalidoException {
        String pagamentoId = UUID.randomUUID().toString();
        when(pagamentoGateway.getPagamentoPor(pagamentoId)).thenReturn(null);

        assertThrows(PagamentoInexistenteException.class, () -> {
            pagamentoInteractor.aprovarPagamento(pagamentoId);
        });

        verify(pagamentoGateway).getPagamentoPor(pagamentoId);
        verify(pagamentoGateway, never()).aprovarPagamento(any(String.class));
    }

    @Test
    void deveFalharAoRecusarPagamentoInexistente() throws PagamentoInexistenteException, StatusPagamentoInvalidoException {
        String pagamentoId = UUID.randomUUID().toString();
        when(pagamentoGateway.getPagamentoPor(pagamentoId)).thenReturn(null);

        assertThrows(PagamentoInexistenteException.class, () -> {
            pagamentoInteractor.recusarPagamento(pagamentoId);
        });

        verify(pagamentoGateway).getPagamentoPor(pagamentoId);
        verify(pagamentoGateway, never()).recusarPagamento(any(String.class));
    }

    @Test
    void deveFalharAoAprovarPagamentoComStatusDiferenteDePendente() throws PagamentoInexistenteException, StatusPagamentoInvalidoException {
    	String pagamentoId = "123456";
        Pagamento pagamento = new Pagamento();
        pagamento.setId(Long.valueOf(pagamentoId));
        pagamento.setStatusPagamento(StatusPagamento.APROVADO);
        
        when(pagamentoGateway.getPagamentoPor(pagamentoId)).thenReturn(pagamento);

        assertThrows(StatusPagamentoInvalidoException.class, () -> {
            pagamentoInteractor.aprovarPagamento(pagamentoId);
        });

        verify(pagamentoGateway).getPagamentoPor(pagamentoId);
        verify(pagamentoGateway, never()).aprovarPagamento(any(String.class));
    }

    @Test
    void deveFalharAoRecusarPagamentoComStatusDiferenteDePendente() throws PagamentoInexistenteException, StatusPagamentoInvalidoException {
        String pagamentoId = "123456";
        Pagamento pagamento = new Pagamento();
        pagamento.setId(Long.valueOf(pagamentoId));
        pagamento.setStatusPagamento(StatusPagamento.APROVADO);
        
        when(pagamentoGateway.getPagamentoPor(pagamentoId)).thenReturn(pagamento);
        
        assertThrows(StatusPagamentoInvalidoException.class, () -> {
            pagamentoInteractor.recusarPagamento(pagamentoId);
        });

        verify(pagamentoGateway).getPagamentoPor(pagamentoId);
        verify(pagamentoGateway, never()).recusarPagamento(any(String.class));
    }
}
