package br.com.empresa.bonal.controles;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.faces.event.ComponentSystemEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

import br.com.empresa.bonal.entidades.Cliente;
import br.com.empresa.bonal.entidades.ItemDaVenda;
import br.com.empresa.bonal.entidades.PedidoVenda;
import br.com.empresa.bonal.entidades.Produto;
import br.com.empresa.bonal.repositorio.PedidoVendaRepositorio;
import br.com.empresa.bonal.util.FacesContextUtil;
import br.com.empresa.bonal.util.logging.Logging;
import br.com.empresa.bonal.util.tx.Transacional;

@Named
@ViewScoped
public class PedidoVendaControle implements Serializable {
	private static final long serialVersionUID = 1L;

	private PedidoVenda pedidoVenda = new PedidoVenda();
	private Cliente cliente = new Cliente();
	private String clienteDocumento;
	
	private Produto produto = new Produto();
	private String produtoCodigo;

	private String message = "";
	
	private ItemDaVenda itemDaVenda = new ItemDaVenda();

	private Long pedidoVendaId;

	// Atributos para Consulta
	private String pedidoVendaNome = "";

	// Listas para Consulta
	private List<PedidoVenda> pedidos;
	private List<PedidoVenda> lista = new ArrayList<>();
	
	private List<ItemDaVenda> itensDaVenda = new ArrayList<>();
	
	private Boolean status = true;

	@Inject
	private PedidoVendaRepositorio pedidoVendaRepositorio;

	@Inject
	private RequestContext requestContext;

	@Inject
	private FacesContextUtil facesContext;

	@Inject
	private Logger logger;
	
	private int contador = 0;

	// Getters and Setters
	public PedidoVenda getPedidoVenda() {
		return pedidoVenda;
	}

	// Adicionado para propriedade de contexto das tabelas do Primefaces
	public void setPedidoVenda(PedidoVenda pedidoVenda) {
		this.pedidoVenda = pedidoVenda;
	}

	public Long getPedidoVendaId() {
		return pedidoVendaId;
	}

	public void setPedidoVendaId(Long pedidoVendaId) {
		this.pedidoVendaId = pedidoVendaId;
	}

	public String getPedidoVendaNome() {
		return pedidoVendaNome;
	}

	public void setPedidoVendaNome(String pedidoVendaNome) {
		this.pedidoVendaNome = pedidoVendaNome;
	}

	public List<PedidoVenda> getPedidos() {
		return pedidos;
	}

	public List<PedidoVenda> getLista() {
		return Collections.unmodifiableList(lista);
	}

	// verificar importancia dos m�todos abaixo //verificar se est�o trocados??
	public Integer getTotalPedidos() {
		return lista.size();
	}

	public Integer getTotalPedidosConsulta() {
		return pedidos.size();
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}
	
	public List<ItemDaVenda> getItensDaVenda() {
		return itensDaVenda;
	}

	public void setItensDaVenda(List<ItemDaVenda> itensDaVenda) {
		this.itensDaVenda = itensDaVenda;
	}
	
	public void addItem(ItemDaVenda item){
		itensDaVenda.add(item);
	}
	
	public void delItem(ItemDaVenda item){
		itensDaVenda.remove(item);
	}
	
	public Integer getTotalDeItens(){
		return itensDaVenda.size();
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public String getClienteDocumento() {
		return clienteDocumento;
	}

	public void setClienteDocumento(String clienteDocumento) {
		this.clienteDocumento = clienteDocumento;
	}

	public ItemDaVenda getItemDaVenda() {
		return itemDaVenda;
	}

	public void setItemDaVenda(ItemDaVenda itemDaVenda) {
		this.itemDaVenda = itemDaVenda;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public String getProdutoCodigo() {
		return produtoCodigo;
	}

	public void setProdutoCodigo(String produtoCodigo) {
		this.produtoCodigo = produtoCodigo;
	}
	
	

	// ----------------- METODOS ----------------------

	


	@Transacional
	public void listarTabela() {
		if (this.pedidos == null) {
			lista = pedidoVendaRepositorio.listarTodos();
			pedidos = new ArrayList<>(lista);
		}
		filtrarTabela();
	}

	@Transacional
	public void preRenderView(ComponentSystemEvent event) {
		if (this.pedidos == null) {
			lista = pedidoVendaRepositorio.listarTodos();
			pedidos = new ArrayList<>(lista);
		}
		filtrarTabela();
	}

	public void filtrarTabela() {
		Stream<PedidoVenda> stream = lista.stream();

		stream = stream
				.filter(o -> (o.getCliente().getNome().toLowerCase().contains(pedidoVendaNome.toLowerCase().trim()))
						| o.getFuncionario().getNome().toLowerCase().contains(pedidoVendaNome.toLowerCase().trim())
						| o.getPrecoTotal().toString().toLowerCase().contains(pedidoVendaNome.toLowerCase().trim()));

		if (status.equals(true))
			stream = stream.filter(o -> (o.getStatus().equals(status)));

		pedidos = stream.collect(Collectors.toList());
	}

	// M�todo chamado ao carregar tabela
	public String listar() {
		listarTabela();
		return null;
	}

	// Limpar tabela da consulta
	public String limpar() {
		limparFiltros();
		this.pedidos = new ArrayList<>(this.lista);
		return null;
	}

	public void limparFiltros() {
		this.pedidoVendaNome = "";
	}

	// Remove um cargo do banco de dados
	@Transacional
	public String salvar(PedidoVenda pedidoVenda) {
		pedidoVenda.setStatus(true);
		pedidoVendaRepositorio.atualizar(pedidoVenda);
		this.pedidos = null;
		listar();
		return null;
	}

	public String produtoConsultar() {
		if (message.equals("")) {

			pedidoVenda = new PedidoVenda();
			return "produtoConsultar";
		} else {

			facesContext.info(message);
			return null;

		}
	}

	// M�todos que utilizam m�todos do reposit�rio
	@Transacional
	public void salvar() {
		String message = "";
		this.pedidoVenda.setStatus(true);
		if (pedidoVenda.getId() == null) {
			pedidoVendaRepositorio.adicionar(pedidoVenda);
		} else {
			pedidoVendaRepositorio.atualizar(pedidoVenda);
		}
		logger.info(message);

	}

	@Transacional
	public void recuperarPedidoVendaPorId() {
		pedidoVenda = pedidoVendaRepositorio.buscarPorId(pedidoVendaId);
	}

	// Remove um cargo do banco de dados
	@Transacional
	public String remover(PedidoVenda pedidoVenda) {
		pedidoVenda.setStatus(false);
		pedidoVendaRepositorio.remover(pedidoVenda);
		this.pedidos = null;
		listar();
		return null;
	}
	

	@Transacional
	public void getClientePorDocumento() {
		cliente = pedidoVendaRepositorio.getClientePorDocumento(clienteDocumento);
		pedidoVenda.setCliente(cliente);
	}
	
	public void clienteSelecionado(SelectEvent event) {
		cliente = (Cliente) event.getObject();
		clienteDocumento = cliente.getDocumento();
		pedidoVenda.setCliente(cliente);
		requestContext.update("formDadosCliente:cliente");
	}
	
	public void produtoSelecionado(SelectEvent event) {
		produto = (Produto) event.getObject();
		produtoCodigo = produto.getCodigo();
		itemDaVenda.setProduto(produto);
		itemDaVenda.setUnidadeDeMedida(produto.getUnidadeDeMedida());
		requestContext.update("formDadosItem:produto");
	}
	
	
	@Transacional
	public void getProdutoPorCodigo() {
		produto = pedidoVendaRepositorio.getProdutoPorCodigo(produtoCodigo);
		itemDaVenda.setProduto(produto);
		itemDaVenda.setUnidadeDeMedida(produto.getUnidadeDeMedida());
	}
	
	

	@Logging
	public String editar(PedidoVenda pedidoVenda) {
		return "pedidoVenda?pedidoVendaId=" + pedidoVenda.getId();
	}

	public String cancelar() {
		return "index";
	}

	public boolean pedidoVendaIdExiste() {
		if (this.pedidoVendaId == null)
			return false;
		return true;
	}

	// Método usado para carregar objeto para o dialog
	public void selecionarPedidoVenda(PedidoVenda pedidoVenda) {
		requestContext.closeDialog(pedidoVenda);
	}
	
	
	public void calculaPrecoTotal(){
		if(itemDaVenda.getQuantidade()!=null && itemDaVenda.getPrecoUnitario()!=null){
			itemDaVenda.setPrecoTotal(itemDaVenda.getPrecoUnitario().multiply(itemDaVenda.getQuantidade()));
		}
		
	}
	
	
	public void adicionarItem(){
		if(pedidoVenda.getPrecoTotal()==null){
			pedidoVenda.setPrecoTotal(new BigDecimal("0"));
		}
		
		BigDecimal aux = itemDaVenda.getPrecoTotal().add(pedidoVenda.getPrecoTotal());
		pedidoVenda.setPrecoTotal(aux);
		itemDaVenda.setId(new Long(contador));
		contador++;
		itensDaVenda.add(itemDaVenda);
		itemDaVenda = new ItemDaVenda();
		produto = new Produto();
		produtoCodigo = null;
		requestContext.update("formTabelaItens:itens");
		requestContext.update("formDadosPagamento");
		
	}
	
	public void removerItem(ItemDaVenda itemDaVenda){
		if(pedidoVenda.getPrecoTotal()==null){
			pedidoVenda.setPrecoTotal(new BigDecimal("0"));
		}
		
		BigDecimal aux = pedidoVenda.getPrecoTotal().subtract(itemDaVenda.getPrecoTotal());
		pedidoVenda.setPrecoTotal(aux);
		itensDaVenda.remove(itemDaVenda);
		requestContext.update("formTabelaItens:itens");
		requestContext.update("formDadosPagamento");
		
	}

}
