package br.com.empresa.bonal.controles;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;

import br.com.empresa.bonal.entidades.Produto;
import br.com.empresa.bonal.entidades.UnidadeDeMedida;
import br.com.empresa.bonal.repositorio.ProdutoRepositorio;
import br.com.empresa.bonal.util.FacesContextUtil;

@ManagedBean
@ViewScoped
public class ProdutoControle implements Serializable {
	private static final long serialVersionUID = 1L;

	final static Logger logger = Logger.getLogger(ProdutoControle.class);

	private Produto produto = new Produto();

	private Long produtoId;
	private Long unidadeDeMedidaId;

	// Atributos para Consulta
	private String produtoNome = "";
	
	private Boolean status = true;
	
	// Listas para Consulta
	private List<Produto> produtos;
	private List<Produto> lista = new ArrayList<>();

	// Repositorio
	private ProdutoRepositorio produtoRepositorio;

	// Construtor chamando a classe repositorio
	public ProdutoControle() {
		produtoRepositorio = new ProdutoRepositorio();
	}

	// Getters and Setters
	public Produto getProduto() {
		return produto;
	}

	// Adicionado para propriedade de contexto das tabelas do Primefaces
	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public Long getProdutoId() {
		return produtoId;
	}

	public void setProdutoId(Long produtoId) {
		this.produtoId = produtoId;
	}

	public Long getUnidadeDeMedidaId() {
		return unidadeDeMedidaId;
	}

	public void setUnidadeDeMedidaId(Long unidadeDeMedidaId) {
		this.unidadeDeMedidaId = unidadeDeMedidaId;
	}

	public String getProdutoNome() {
		return produtoNome;
	}

	public void setProdutoNome(String produtoNome) {
		this.produtoNome = produtoNome;
	}

	public List<Produto> getProdutos() {
		return produtos;
	}

	public List<Produto> getLista() {
		return Collections.unmodifiableList(lista);
	}

	// verificar importancia dos m�todos abaixo //verificar se est�o trocados??
	public Integer getTotalProdutos() {
		return lista.size();
	}

	public Integer getTotalProdutosConsulta() {
		return produtos.size();
	}
	
	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	// ----------------- METODOS ----------------------
	@PostConstruct
	public void listarTabela() {
		if (this.produtos == null) {
			lista = produtoRepositorio.listarTodos();
			produtos = new ArrayList<>(lista);
		}
		filtrarTabela();
	}

	public void filtrarTabela() {
		Stream<Produto> stream = lista.stream();

		if (!produtoNome.equals(null)) {
			stream = stream.filter(p -> (p.getNome().toLowerCase().contains(produtoNome.toLowerCase().trim()))
					| (p.getCodigo().toLowerCase().contains(produtoNome.toLowerCase().trim()))
					| p.getDescricao().toLowerCase().contains(produtoNome.toLowerCase().trim()));
		}

		if (unidadeDeMedidaId != null)
			stream = stream.filter(p -> (p.getUnidadeDeMedida().getId().equals(unidadeDeMedidaId)));
		

		stream = stream.filter(p -> (p.getStatus().equals(status)));
		produtos = stream.collect(Collectors.toList());
	}

	// M�todo chamado ao carregar pagina de consulta para popular tabela
	public String listar() {
		listarTabela();
		return null;
	}

	// Limpar tabela da consulta
	public String limpar() {
		limparFiltros();
		this.produtos = new ArrayList<>(this.lista);
		return null;
	}

	public void limparFiltros() {
		this.produtoNome = "";
		this.unidadeDeMedidaId = null;
	}

	public void salvar(Produto produto, UnidadeDeMedida unidade) {
		this.produto = produto;
		this.produtoId = produto.getId();
		this.unidadeDeMedidaId = unidade.getId();
		salvar();
	}

	// M�todos que utilizam m�todos do reposit�rio
	public void salvar() {
		String message = "";
		this.produto.setStatus(true);
		if (produto.getId() == null) {
			Produto existe = produtoRepositorio.codigoExiste(produto);
			if (existe != null) {
				new FacesContextUtil().warn("Já existe um produto registrado com esse código.");
			}
			produtoRepositorio.adicionar(produto, unidadeDeMedidaId);
			message += "Produto Cadastrado com Sucesso.";
		} else {
			produtoRepositorio.atualizar(produto, unidadeDeMedidaId);
			message += "Produto Atualizado com Sucesso.";
		}
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("coeficientetecnico.xhtml?produtoId=" + this.produto.getId());
		} catch (IOException e) {
			new FacesContextUtil().info(message);
			logger.info(message);
			 this.produto = new Produto();
		}
	}

	public void recuperarProdutoPorId() {
		this.produto = produtoRepositorio.buscarPorId(produtoId);
	}

	// Remove um Produto do banco de dados
	public void remover() {

		this.produto.setStatus(false);
		produtoRepositorio.remover(produto);
		produtos = null;
		listarTabela();
		produto = null;
	}

	public void remover(Produto produto) {
		this.produto = produto;
		remover();
	}

	// Editar um Produto
	public String editar() {
		produtoId = this.produto.getId();
		return "produto?produtoId=" + produtoId;
	}

	public String addCoeficientes() {
		return "coeficientetecnico?produtoId=" + this.produtoId;
	}

	public String editar(Produto produto) {
		this.produto = produto;
		return editar();
	}

	public boolean ProdutoIdExiste() {
		if (this.produtoId == null)
			return false;
		return true;
	}

	// vou tirar isso daqui não se preocupe
	public void carregandoDados() {
		try {
			// simulate a long running request
			Thread.sleep(1500);
		} catch (Exception e) {
			// ignore
		}
	}
}
