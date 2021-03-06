package br.com.empresa.bonal.repositorio;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import br.com.empresa.bonal.entidades.Cargo;
import br.com.empresa.bonal.entidades.Fornecedor;

public class FornecedorRepositorio implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	EntityManager em;

	// m�todo que persiste um registro
	public void adicionar(Fornecedor fornecedor) {

		em.persist(fornecedor);
	}

	// m�todo que atualiza um registro
	public void atualizar(Fornecedor fornecedor) {

		em.merge(fornecedor);
	}

	// m�todo que remove um registro
	public void remover(Fornecedor fornecedor) {
		em.merge(fornecedor);
	}

	// m�todo que recupera um objeto pelo id
	public Fornecedor buscarPorId(Long id) {
		return em.find(Fornecedor.class, id);
	}

	// m�todo que lista todos os registros
	public List<Fornecedor> listarTodos() {
		try {
			return em.createQuery("select s from Fornecedor s", Fornecedor.class).getResultList();
		} catch (Exception e) {
			return null;
		}
	}

	// m�todo que lista com crit�rios todos os registros
	public List<Fornecedor> listarPorCriterios(String nome) {
		String jpql = "select s from Fornecedor s where ";

		if (nome != null)
			jpql += "(s.nome like :pnome or s.documento like :pdocumento or s.identificacao like :pidentificacao);";
		jpql += "1 = 1";

		TypedQuery<Fornecedor> query = em.createQuery(jpql, Fornecedor.class);

		if (nome != null)
			query.setParameter("pnome", '%' + nome + '%').setParameter("pdocumento", '%' + nome + '%')
					.setParameter("pidentificacao", '%' + nome + '%');

		return query.getResultList();
	}



	// método que verifica se elemento existe
	public Fornecedor getFornecedorPorDocumento(String documento) {
		TypedQuery<Fornecedor> query = em
				.createQuery("select c from Fornecedor c where c.documento = :pdocumento", Fornecedor.class)
				.setParameter("pdocumento", documento.toUpperCase());

		try {
			return query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}

}
