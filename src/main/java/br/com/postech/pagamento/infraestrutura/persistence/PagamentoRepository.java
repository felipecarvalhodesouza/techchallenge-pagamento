package br.com.postech.pagamento.infraestrutura.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PagamentoRepository extends JpaRepository<PagamentoEntity, Long> {

}
