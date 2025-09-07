package dev.hugofaria.algamoney.api.service;

import dev.hugofaria.algamoney.api.dto.LancamentoEstatisticaPessoa;
import dev.hugofaria.algamoney.api.mail.Mailer;
import dev.hugofaria.algamoney.api.model.Lancamento;
import dev.hugofaria.algamoney.api.model.Pessoa;
import dev.hugofaria.algamoney.api.model.Usuario;
import dev.hugofaria.algamoney.api.repository.LancamentoRepository;
import dev.hugofaria.algamoney.api.repository.PessoaRepository;
import dev.hugofaria.algamoney.api.repository.UsuarioRepository;
import dev.hugofaria.algamoney.api.service.exception.PessoaInexistenteOuInativaException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

@Service
public class LancamentoService {

    private static final String DESTINATARIOS = "ROLE_PESQUISAR_LANCAMENTO";

    private static final Logger logger = LoggerFactory.getLogger(LancamentoService.class);

    private final PessoaRepository pessoaRepository;

    private final LancamentoRepository lancamentoRepository;

    private final UsuarioRepository usuarioRepository;

    private final Mailer mailer;

    public LancamentoService(PessoaRepository pessoaRepository, LancamentoRepository lancamentoRepository, UsuarioRepository usuarioRepository, Mailer mailer) {
        this.pessoaRepository = pessoaRepository;
        this.lancamentoRepository = lancamentoRepository;
        this.usuarioRepository = usuarioRepository;
        this.mailer = mailer;
    }

    @Scheduled(cron = "0 0 6 * * *")
    public void avisarSobreLancamentosVencidos() {
        if (logger.isDebugEnabled()) {
            logger.debug("Preparando envio de "
                    + "e-mails de aviso de lançamentos vencidos.");
        }

        List<Lancamento> vencidos = lancamentoRepository
                .findByDataVencimentoLessThanEqualAndDataPagamentoIsNull(LocalDate.now());

        if (vencidos.isEmpty()) {
            logger.info("Sem lançamentos vencidos para aviso.");
            return;
        }

        logger.info("Existem {} lançamentos vencidos.", vencidos.size());

        List<Usuario> destinatarios = usuarioRepository
                .findByPermissoesDescricao(DESTINATARIOS);

        if (destinatarios.isEmpty()) {
            logger.warn("Existem lançamentos vencidos, mas o "
                    + "sistema não encontrou destinatários.");
            return;
        }

        mailer.avisarSobreLancamentosVencidos(vencidos, destinatarios);

        logger.info("Envio de e-mail de aviso concluído.");
    }

    public byte[] relatorioPorPessoa(LocalDate inicio, LocalDate fim) throws Exception {
        List<LancamentoEstatisticaPessoa> dados = lancamentoRepository.porPessoa(inicio, fim);

        Map<String, Object> parametros = new HashMap<>();
        parametros.put("DT_INICIO", Date.valueOf(inicio));
        parametros.put("DT_FIM", Date.valueOf(fim));
        parametros.put("REPORT_LOCALE", new Locale("pt", "BR"));

        InputStream inputStream = this.getClass().getResourceAsStream(
                "/relatorios/lancamentos-por-pessoa.jasper");

        JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, parametros,
                new JRBeanCollectionDataSource(dados));

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    public Lancamento salvar(Lancamento lancamento) {
        Optional<Pessoa> pessoa = pessoaRepository.findById(lancamento.getPessoa().getCodigo());
        if (pessoa.isEmpty() || pessoa.get().isInativo()) {
            throw new PessoaInexistenteOuInativaException();
        }

        return lancamentoRepository.save(lancamento);
    }

    public Lancamento atualizar(Long codigo, Lancamento lancamento) {
        Lancamento lancamentoSalvo = buscarLancamentoExistente(codigo);
        if (!lancamento.getPessoa().equals(lancamentoSalvo.getPessoa())) {
            validarPessoa(lancamento);
        }

        BeanUtils.copyProperties(lancamento, lancamentoSalvo, "codigo");

        return lancamentoRepository.save(lancamentoSalvo);
    }

    private void validarPessoa(Lancamento lancamento) {
        Optional<Pessoa> pessoa = null;
        if (lancamento.getPessoa().getCodigo() != null) {
            pessoa = pessoaRepository.findById(lancamento.getPessoa().getCodigo());
        }

        if (pessoa.isEmpty() || pessoa.get().isInativo()) {
            throw new PessoaInexistenteOuInativaException();
        }
    }

    private Lancamento buscarLancamentoExistente(Long codigo) {
/* 		Optional<Lancamento> lancamentoSalvo = lancamentoRepository.findById(codigo);
		if (lancamentoSalvo.isEmpty()) {
			throw new IllegalArgumentException();
		} */
        return lancamentoRepository.findById(codigo).orElseThrow(() -> new IllegalArgumentException());
    }

}