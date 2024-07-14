package br.com.postech.pagamento.infraestrutura.gateway;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.postech.pagamento.domain.Pagamento;
import br.com.postech.pagamento.domain.enumeration.StatusPagamento;
import br.com.postech.pagamento.infraestrutura.persistence.PagamentoEntity;

class PagamentoEntityMapperTest {

    private PagamentoEntityMapper mapper;

    @BeforeEach
    void setUp() {
    	mapper = new PagamentoEntityMapper();
    }

    @Test
    void deveConverterDomainParaEntity() {
        Pagamento pagamentoDomain = new Pagamento();
        pagamentoDomain.setId(1L);
        pagamentoDomain.setValorTotal(100.0);
        pagamentoDomain.setStatusPagamento(StatusPagamento.PENDENTE);

        PagamentoEntity entity = mapper.toEntity(pagamentoDomain);

        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(pagamentoDomain.getId());
        assertThat(entity.getValorTotal()).isEqualTo(pagamentoDomain.getValorTotal());
        assertThat(entity.getStatusPagamento()).isEqualTo(pagamentoDomain.getStatusPagamento());
    }

    @Test
    void deveConverterEntityParaDomain() {
        PagamentoEntity entity = new PagamentoEntity();
        entity.setId(1L);
        entity.setValorTotal(100.0);
        entity.setStatusPagamento(StatusPagamento.APROVADO);

        Pagamento pagamento = mapper.toDomainObject(entity);

        assertThat(pagamento).isNotNull();
        assertThat(pagamento.getId()).isEqualTo(entity.getId());
        assertThat(pagamento.getValorTotal()).isEqualTo(entity.getValorTotal());
        assertThat(pagamento.getStatusPagamento()).isEqualTo(entity.getStatusPagamento());
    }
}
