package br.com.postech.pagamento.infraestrutura.gateway;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.postech.pagamento.domain.Pagamento;
import br.com.postech.pagamento.domain.enumeration.StatusPagamento;
import br.com.postech.pagamento.domain.exception.PagamentoInexistenteException;
import br.com.postech.pagamento.infraestrutura.persistence.PagamentoEntity;
import br.com.postech.pagamento.infraestrutura.persistence.PagamentoRepository;

public class PagamentoRepositoryGatewayTest {

    @Mock
    private PagamentoRepository pagamentoRepository;

    @Mock
    private PagamentoEntityMapper mapper;

    @InjectMocks
    private PagamentoRepositoryGateway pagamentoGateway;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveRetornarPagamentoExistente() throws PagamentoInexistenteException {
        when(pagamentoRepository.findById(any(Long.class))).thenReturn(Optional.of(new PagamentoEntity()));
        when(mapper.toDomainObject(any(PagamentoEntity.class))).thenCallRealMethod();

        Pagamento pagamento = pagamentoGateway.getPagamentoPor("1");

        assertThat(pagamento).isNotNull();
    }

    @Test
    void deveLancarPagamentoInexistenteException() {
        when(pagamentoRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(PagamentoInexistenteException.class, () -> {
            pagamentoGateway.getPagamentoPor("1");
        });
    }

    @Test
    void deveRetornarStatusPagamento() throws PagamentoInexistenteException {
    	when(pagamentoRepository.findById(any(Long.class))).thenReturn(Optional.of(new PagamentoEntity()));
    	when(mapper.toDomainObject(any(PagamentoEntity.class))).thenCallRealMethod();
        Pagamento pagamentoMock = new Pagamento();
        pagamentoMock.setStatusPagamento(StatusPagamento.APROVADO);

        when(pagamentoGateway.getPagamentoPor("1")).thenReturn(pagamentoMock);

        String status = pagamentoGateway.getStatusPagamentoPedido("1");

        assertThat(status).isEqualTo(StatusPagamento.APROVADO.getDescricao());
    }

    @Test
    void deveInserirPagamento() {
        Pagamento pagamento = new Pagamento();

        PagamentoEntity entity = new PagamentoEntity();
        entity.setStatusPagamento(StatusPagamento.PENDENTE);

        when(mapper.toEntity(pagamento)).thenReturn(entity);
        when(pagamentoRepository.save(entity)).thenReturn(entity);
        when(mapper.toDomainObject(entity)).thenReturn(pagamento);

        Pagamento resultado = pagamentoGateway.inserirPagamento(pagamento);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getStatusPagamento()).isEqualTo(StatusPagamento.PENDENTE);
    }

    @Test
    void deveAprovarPagamento() throws PagamentoInexistenteException {
        PagamentoEntity pagamentoEntity = new PagamentoEntity();
        pagamentoEntity.setStatusPagamento(StatusPagamento.PENDENTE);

        when(pagamentoRepository.findById(1L)).thenReturn(Optional.of(pagamentoEntity));

        pagamentoGateway.aprovarPagamento("1");

        assertThat(pagamentoEntity.getStatusPagamento()).isEqualTo(StatusPagamento.APROVADO);
        verify(pagamentoRepository).save(pagamentoEntity);
    }

    @Test
    void deveRecusarPagamento() throws PagamentoInexistenteException {
        PagamentoEntity pagamentoEntity = new PagamentoEntity();
        pagamentoEntity.setStatusPagamento(StatusPagamento.PENDENTE);

        when(pagamentoRepository.findById(1L)).thenReturn(Optional.of(pagamentoEntity));

        pagamentoGateway.recusarPagamento("1");

        assertThat(pagamentoEntity.getStatusPagamento()).isEqualTo(StatusPagamento.RECUSADO);
        verify(pagamentoRepository).save(pagamentoEntity);
    }
}
