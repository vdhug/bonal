package br.com.empresa.bonal.controles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import com.sun.faces.action.RequestMapping;

import br.com.empresa.bonal.entidades.UnidadeDeMedida;
import br.com.empresa.bonal.repositorio.UnidadeDeMedidaRepositorio;
import br.com.empresa.bonal.util.FacesContextUtil;


public class UnidadeDeMedidaControle {
	
	private UnidadeDeMedida unidadeDeMedida = new UnidadeDeMedida();

	private Long unidadeDeMedidaId;

	// Atributos para Consulta
	private String unidadeDeMedidaNome = "";

	// Listas para Consulta
	private List<UnidadeDeMedida> unidadesDeMedida;
	private List<UnidadeDeMedida> lista = new ArrayList<>();

	// Repositorio
	private UnidadeDeMedidaRepositorio unidadeDeMedidaRepositorio;

	// Construtor chamando a classe repositorio
	public UnidadeDeMedidaControle() {
		unidadeDeMedidaRepositorio = new UnidadeDeMedidaRepositorio();
	}

	// Getters and Setters
	public UnidadeDeMedida getCargo() {
		return unidadeDeMedida;
	}

	// Adicionado para propriedade de contexto das tabelas do Primefaces
	public void setCargo(UnidadeDeMedida cargo) {
		this.unidadeDeMedida = cargo;
	}

	public Long getCargoId() {
		return unidadeDeMedidaId;
	}

	public void setCargoId(Long cargoId) {
		this.unidadeDeMedidaId = cargoId;
	}

	public String getCargoNome() {
		return unidadeDeMedidaNome;
	}

	public void setCargoNome(String cargoNome) {
		this.unidadeDeMedidaNome = cargoNome;
	}

	public List<UnidadeDeMedida> getCargos() {
		return unidadesDeMedida;
	}

	public List<UnidadeDeMedida> getLista() {
		return Collections.unmodifiableList(lista);
	}

	// verificar importancia dos m�todos abaixo //verificar se est�o trocados??
	public Integer getTotalCargos() {
		return lista.size();
	}

	public Integer getTotalCargosConsultados() {
		return unidadesDeMedida.size();
	}

	// ----------------- METODOS ----------------------
	@PostConstruct
	public void listarTabela() {
		if (this.unidadesDeMedida == null) {
			lista = unidadeDeMedidaRepositorio.listarUnidadesDeMedida(unidadeDeMedidaNome);
			unidadesDeMedida = new ArrayList<>(lista);
		}
		filtrarTabela();
	}

	public void filtrarTabela() {
		Stream<UnidadeDeMedida> filter = lista.stream()
				.filter(c -> (c.getNome().toLowerCase().contains(unidadeDeMedidaNome.toLowerCase().trim())));

		unidadesDeMedida = filter.collect(Collectors.toList());
	}

	// M�todo chamado ao carregar pagina de consulta para popular tabela
	public String listar() {
		listarTabela();
		return null;
	}

	// Limpar tabela da consulta,
	public String limpar() {
		this.unidadeDeMedidaNome = "";
		// listarCargos(); // Realiza nova consulta ao repositorio
		filtrarTabela(); // Retorna a lista unmodifiablelist offline armazenada
		return null;
	}

	// M�todos que utilizam m�todos do reposit�rio
	public String salvar() {
		String message = "";
		if (unidadeDeMedida.getId() == null) {
			unidadeDeMedidaRepositorio.adicionar(unidadeDeMedida);
			message += "Cargo Cadastrado com Sucesso.";
		} else {
			unidadeDeMedidaRepositorio.atualizar(unidadeDeMedida);
			message += "Cargo Atualizado com Sucesso.";
		}
		new FacesContextUtil().info(message);
		// System.out.println(cargo);
		unidadeDeMedida = new UnidadeDeMedida();
		return null;
	}

	public void recuperarCargoPorId() {
		unidadeDeMedida = unidadeDeMedidaRepositorio.getUnidadeDeMedida(unidadeDeMedidaId);
	}

	// Remove um cargo do banco de dados
	public void remover() {
		unidadeDeMedidaRepositorio.remover(unidadeDeMedida);
		unidadesDeMedida = null;
		listarTabela();
		unidadeDeMedida = null;
	}

	public void remover(UnidadeDeMedida c) {
		this.unidadeDeMedida = c;
		remover();
	}

	// Editar um cargo
	public String editar() {
		unidadeDeMedidaId = this.unidadeDeMedida.getId();
		return "cargo?cargoid=" + unidadeDeMedidaId;
	}

	public String editar(UnidadeDeMedida c) {
		this.unidadeDeMedida = c;
		return editar();
	}

	public boolean cargoIdExiste() {
		if (this.unidadeDeMedidaId == null)
			return false;
		return true;
	}
	
}
