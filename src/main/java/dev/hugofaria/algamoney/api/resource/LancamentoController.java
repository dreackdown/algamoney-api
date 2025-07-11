package dev.hugofaria.algamoney.api.resource;

import dev.hugofaria.algamoney.api.dto.LancamentoEstatisticaCategoria;
import dev.hugofaria.algamoney.api.dto.LancamentoEstatisticaDia;
import dev.hugofaria.algamoney.api.event.RecursoCriadoEvent;
import dev.hugofaria.algamoney.api.exceptionHandler.AlgamoneyExceptionHandler.Erro;
import dev.hugofaria.algamoney.api.model.Lancamento;
import dev.hugofaria.algamoney.api.repository.LancamentoRepository;
import dev.hugofaria.algamoney.api.repository.filter.LancamentoFilter;
import dev.hugofaria.algamoney.api.repository.projection.ResumoLancamento;
import dev.hugofaria.algamoney.api.service.LancamentoService;
import dev.hugofaria.algamoney.api.service.exception.PessoaInexistenteOuInativaException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/lancamentos")
public class LancamentoController {

    private final LancamentoRepository lancamentoRepository;

    private final LancamentoService lancamentoService;

    private final ApplicationEventPublisher publisher;

    private final MessageSource messageSource;

    public LancamentoController(LancamentoRepository lancamentoRepository, LancamentoService lancamentoService, ApplicationEventPublisher publisher, MessageSource messageSource) {
        this.lancamentoRepository = lancamentoRepository;
        this.lancamentoService = lancamentoService;
        this.publisher = publisher;
        this.messageSource = messageSource;
    }

    @GetMapping("/relatorios/por-pessoa")
    @PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and hasAuthority('SCOPE_read')")
    public ResponseEntity<byte[]> relatorioPorPessoa(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate inicio,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fim) throws Exception {
        byte[] relatorio = lancamentoService.relatorioPorPessoa(inicio, fim);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
                .body(relatorio);
    }

    @GetMapping("/estatisticas/por-dia")
    @PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and hasAuthority('SCOPE_read')")
    public List<LancamentoEstatisticaDia> porDia() {
        return this.lancamentoRepository.porDia(LocalDate.now());
    }

    @GetMapping("/estatisticas/por-categoria")
    @PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and hasAuthority('SCOPE_read')")
    public List<LancamentoEstatisticaCategoria> porCategoria() {
        return this.lancamentoRepository.porCategoria(LocalDate.now());
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and hasAuthority('SCOPE_read')")
    public Page<Lancamento> pesquisar(LancamentoFilter lancamentoFilter, Pageable pageable) {
        return lancamentoRepository.filtrar(lancamentoFilter, pageable);
    }

    @GetMapping(params = "resumo")
    @PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and hasAuthority('SCOPE_read')")
    public Page<ResumoLancamento> resumir(LancamentoFilter lancamentoFilter, Pageable pageable) {
        return lancamentoRepository.resumir(lancamentoFilter, pageable);
    }

    @GetMapping("/{codigo}")
    @PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and hasAuthority('SCOPE_read')")
    public ResponseEntity<Lancamento> buscarPeloCodigo(@PathVariable Long codigo) {
        Optional<Lancamento> lancamento = lancamentoRepository.findById(codigo);
        return lancamento.isPresent() ? ResponseEntity.ok(lancamento.get()) : ResponseEntity.notFound().build();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_LANCAMENTO') and hasAuthority('SCOPE_write')")
    public ResponseEntity<Lancamento> criar(@Valid @RequestBody Lancamento lancamento, HttpServletResponse response) {
        Lancamento lancamentoSalvo = lancamentoService.salvar(lancamento);
        publisher.publishEvent(new RecursoCriadoEvent(this, response, lancamentoSalvo.getCodigo()));
        return ResponseEntity.status(HttpStatus.CREATED).body(lancamentoSalvo);
    }

    @ExceptionHandler({PessoaInexistenteOuInativaException.class})
    public ResponseEntity<Object> handlePessoaInexistenteOuInativaException(PessoaInexistenteOuInativaException ex) {
        String mensagemUsuario = messageSource.getMessage("pessoa.inexistente-ou-inativa", null, LocaleContextHolder.getLocale());
        String mensagemDesenvolvedor = ex.toString();
        List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
        return ResponseEntity.badRequest().body(erros);
    }

    @DeleteMapping("/{codigo}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ROLE_REMOVER_LANCAMENTO') and hasAuthority('SCOPE_write')")
    public void remover(@PathVariable Long codigo) {
        lancamentoRepository.deleteById(codigo);
    }


    @PutMapping("/{codigo}")
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_LANCAMENTO')")
    public ResponseEntity<Lancamento> atualizar(@PathVariable Long codigo, @Valid @RequestBody Lancamento lancamento) {
        try {
            Lancamento lancamentoSalvo = lancamentoService.atualizar(codigo, lancamento);
            return ResponseEntity.ok(lancamentoSalvo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
