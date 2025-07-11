package dev.hugofaria.algamoney.api.repository.lancamento;

import dev.hugofaria.algamoney.api.dto.LancamentoEstatisticaCategoria;
import dev.hugofaria.algamoney.api.dto.LancamentoEstatisticaDia;
import dev.hugofaria.algamoney.api.dto.LancamentoEstatisticaPessoa;
import dev.hugofaria.algamoney.api.model.Lancamento;
import dev.hugofaria.algamoney.api.repository.filter.LancamentoFilter;
import dev.hugofaria.algamoney.api.repository.projection.ResumoLancamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface LancamentoRepositoryQuery {

    public List<LancamentoEstatisticaPessoa> porPessoa(LocalDate inicio, LocalDate fim);

    public List<LancamentoEstatisticaCategoria> porCategoria(LocalDate mesReferencia);

    public List<LancamentoEstatisticaDia> porDia(LocalDate mesReferencia);

    public Page<Lancamento> filtrar(LancamentoFilter lancamentoFilter, Pageable pageable);

    public Page<ResumoLancamento> resumir(LancamentoFilter lancamentoFilter, Pageable pageable);

}