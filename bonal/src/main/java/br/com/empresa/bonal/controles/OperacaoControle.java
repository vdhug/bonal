package br.com.empresa.bonal.controles;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;

import br.com.empresa.bonal.entidades.Operacao;
import br.com.empresa.bonal.repositorio.OperacaoRepositorio;
import br.com.empresa.bonal.util.FacesContextUtil;
import br.com.empresa.bonal.util.tx.transacional;

@Named
@ViewScoped
public class OperacaoControle implements Serializable {
	private static final long serialVersionUID = 1L;

	static Logger logger = Logger.getLogger(OperacaoControle.class);

	private Operacao operacao = new Operacao();

	private Long operacaoId;

	// Atributos para Consulta
	private String operacaoNome = "";

	// Listas para Consulta
	private List<Operacao> operacoes;
	private List<Operacao> lista = new ArrayList<>();

	private Boolean status = true;

	@Inject
	private OperacaoRepositorio operacaoRepositorio;

	@Inject
	private FacesContextUtil facesContext;

	// Getters and Setters
	public Operacao getOperacao() {
		return operacao;
	}

	// Adicionado para propriedade de contexto das tabelas do Primefaces
	public void setOperacao(Operacao operacao) {
		this.operacao = operacao;
	}

	public Long getOperacaoId() {
		return operacaoId;
	}

	public void setOperacaoId(Long operacaoId) {
		this.operacaoId = operacaoId;
	}

	public String getOperacaoNome() {
		return operacaoNome;
	}

	public void setOperacaoNome(String operacaoNome) {
		this.operacaoNome = operacaoNome;
	}

	public List<Operacao> getOperacoes() {
		return operacoes;
	}

	public List<Operacao> getLista() {
		return Collections.unmodifiableList(lista);
	}

	// verificar importancia dos m�todos abaixo //verificar se est�o trocados??
	public Integer getTotalOperacoes() {
		return lista.size();
	}

	public Integer getTotalOperacoesConsulta() {
		return operacoes.size();
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	// ----------------- METODOS ----------------------
	@PostConstruct
	@transacional
	public void listarTabela() {
		if (this.operacoes == null) {
			lista = operacaoRepositorio.listarTodos();
			operacoes = new ArrayList<>(lista);
		}
		filtrarTabela();
	}

	public void filtrarTabela() {
		Stream<Operacao> stream = lista.stream();

		stream = stream.filter(o -> (o.getNome().toLowerCase().contains(operacaoNome.toLowerCase().trim()))
				| o.getCodigo().toLowerCase().contains(operacaoNome.toLowerCase().trim())
				| o.getDescricao().toLowerCase().contains(operacaoNome.toLowerCase().trim()));

		if (status.equals(true))
			stream = stream.filter(o -> (o.getStatus().equals(status)));
		
		operacoes = stream.collect(Collectors.toList());
	}

	// M�todo chamado ao carregar tabela
	public String listar() {
		listarTabela();
		return null;
	}

	// Limpar tabela da consulta
	public String limpar() {
		limparFiltros();
		this.operacoes = new ArrayList<>(this.lista);
		return null;
	}

	public void limparFiltros() {
		this.operacaoNome = "";
	}

	public void salvar(Operacao o) {
		this.operacao = o;
		salvar();
	}

	// M�todos que utilizam m�todos do reposit�rio
	@transacional
	public String salvar() {
		String message = "";
		this.operacao.setStatus(true);
		if (operacao.getId() == null) {
			operacaoRepositorio.adicionar(operacao);
			message += "Operacao Cadastrada com Sucesso.";
		} else {
			operacaoRepositorio.atualizar(operacao);
			message += "Operacao Atualizada com Sucesso.";
		}
		logger.info(message);
		facesContext.info(message);
		operacao = new Operacao();
		return null;
	}

	@transacional
	public void recuperarOperacaoPorId() {
		operacao = operacaoRepositorio.buscarPorId(operacaoId);
	}

	// Remove um cargo do banco de dados
	@transacional
	public String remover(Operacao operacao) {
		operacao.setStatus(false);
		operacaoRepositorio.remover(operacao);
		this.operacoes = null;
		this.operacao = null;
		listar();
		return null;
	}

	// Editar um cargo
	public String editar(Operacao operacao) {
		return "operacao?operacaoId=" + operacao.getId();
	}

	public String cancelar() {
		return "index";
	}

	public boolean operacaoIdExiste() {
		if (this.operacaoId == null)
			return false;
		return true;
	}

}
