/*package br.com.empresa.bonal.depreciadas;

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

import org.apache.logging.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

import br.com.empresa.bonal.entidades.Cargo;
import br.com.empresa.bonal.entidades.Endereco;
import br.com.empresa.bonal.entidades.Fornecedor;
import br.com.empresa.bonal.entidades.Funcionario;
import br.com.empresa.bonal.entidades.Operacao;
import br.com.empresa.bonal.repositorio.CargoRepositorio;
import br.com.empresa.bonal.repositorio.FuncionarioRepositorio;
import br.com.empresa.bonal.util.FacesContextUtil;
import br.com.empresa.bonal.util.logging.Logging;
import br.com.empresa.bonal.util.tx.Transacional;

@Named
@ViewScoped
public class FuncionarioControle implements Serializable {
	private static final long serialVersionUID = 1L;

	private Funcionario funcionario = new Funcionario();

	private Boolean status = true;

	private Long funcionarioId;
	private Long cargoId;

	// Atributos para Consulta
	private String funcionarioNome = "";

	// Listas para Consulta
	private List<Funcionario> funcionarios;
	private List<Funcionario> lista = new ArrayList<>();

	@Inject
	private FuncionarioRepositorio funcionarioRepositorio;

	@Inject
	private CargoRepositorio cargoRepositorio;

	@Inject
	private FacesContextUtil facesContext;

	@Inject
	private RequestContext requestContext;

	@Inject
	private Logger logger;

	// Getters and Setters
	public Funcionario getFuncionario() {
		return funcionario;
	}

	// Adicionado para propriedade de contexto das tabelas do Primefaces
	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public Endereco getEndereco() {
		return funcionario.getEndereco();
	}

	// Adicionado para propriedade de contexto das tabelas do Primefaces
	public void setEndereco(Endereco endereco) {
		this.funcionario.setEndereco(endereco);
	}

	public Long getFuncionarioId() {
		return funcionarioId;
	}

	public void setFuncionarioId(Long funcionarioId) {
		this.funcionarioId = funcionarioId;
	}

	public Long getCargoId() {
		return cargoId;
	}

	public void setCargoId(Long cargoId) {
		this.cargoId = cargoId;
	}

	public String getFuncionarioNome() {
		return funcionarioNome;
	}

	public void setFuncionarioNome(String funcionarioNome) {
		this.funcionarioNome = funcionarioNome;
	}

	public List<Funcionario> getFuncionarios() {
		return funcionarios;
	}

	public List<Funcionario> getLista() {
		return Collections.unmodifiableList(lista);
	}

	// verificar importancia dos m�todos abaixo //verificar se est�o trocados??
	public Integer getTotalFuncionarios() {
		return lista.size();
	}

	public Integer getTotalFuncionariosConsulta() {
		return funcionarios.size();
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	// ----------------- METODOS ----------------------
	@PostConstruct
	@Transacional
	public void listarTabela() {
		if (this.funcionarios == null) {
			lista = funcionarioRepositorio.listarTodos();
			funcionarios = new ArrayList<>(lista);
		}
		filtrarTabela();
	}

	public void filtrarTabela() {
		Stream<Funcionario> stream = lista.stream();

		if (!funcionarioNome.equals(null))
			stream = stream.filter(f -> (f.getNome().toLowerCase().contains(funcionarioNome.toLowerCase().trim()))
					| (f.getDocumento().toLowerCase().contains(funcionarioNome.toLowerCase().trim()))
					| (f.getEmail().toLowerCase().contains(funcionarioNome.toLowerCase().trim()))
					| f.getIdentificacao().toLowerCase().contains(funcionarioNome.toLowerCase().trim()));

		if (status.equals(true))
			stream = stream.filter(f -> (f.getStatus().equals(status)));

		if (cargoId != null)
			stream = stream.filter(c -> (c.getCargo().getId().equals(cargoId)));

		funcionarios = stream.collect(Collectors.toList());
	}

	// M�todo chamado ao carregar pagina de consulta para popular tabela
	public String listar() {
		listarTabela();
		return null;
	}

	// Limpar tabela da consulta
	public String limpar() {
		limparFiltros();
		this.funcionarios = new ArrayList<>(this.lista);
		return null;
	}

	public void limparFiltros() {
		this.funcionarioNome = "";
		this.cargoId = null;
	}

	// M�todos que utilizam m�todos do reposit�rio
	@Transacional
	public String salvar() {
		String message = "";

		this.funcionario.setStatus(true);
		this.funcionario.setTipo("PESSOA_FISICA");

		Cargo cargo = cargoRepositorio.buscarPorId(cargoId);
		this.funcionario.setCargo(cargo);

		if (funcionario.getId() == null) {
			funcionarioRepositorio.adicionar(funcionario);
			message += "Funcionario Cadastrado com Sucesso.";
		} else {
			funcionarioRepositorio.atualizar(funcionario);
			message += "Funcionario Atualizado com Sucesso.";
		}
		facesContext.info(message);
		logger.info(message);
		this.funcionario = new Funcionario();
		return null;
	}

	@Transacional
	public void recuperarFuncionarioPorId() {
		funcionario = funcionarioRepositorio.buscarPorId(funcionarioId);
	}

	// Remove um Funcionario do banco de dados
	@Transacional
	public String remover(Funcionario funcionario) {
		funcionario.setStatus(false);
		funcionarioRepositorio.remover(funcionario);
		this.funcionarios = null;
		listarTabela();
		return null;
	}

	@Logging
	public String editar(Funcionario funcionario) {
		return "funcionario?funcionarioId=" + funcionario.getId();
	}

	public String addCoeficientes() {
		return "coeficientetecnico?funcionarioId=" + this.funcionarioId;
	}

	public boolean FuncionarioIdExiste() {
		if (this.funcionarioId == null)
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

	public void cargoSelecionado(SelectEvent event) {
		Cargo cargo = (Cargo) event.getObject();
		this.funcionario.setCargo(cargo);
		requestContext.update("formFuncionario:cargo");
	}

	// Método usado para carregar objeto para o dialog
	public void selecionarFuncionario(Funcionario funcionario) {
		requestContext.closeDialog(funcionario);
	}

}
*/