package br.com.empresa.bonal.testes.entidades;

import java.math.BigDecimal;

import br.com.empresa.bonal.entidades.Produto;
import br.com.empresa.bonal.entidades.Categoria;
import br.com.empresa.bonal.entidades.UnidadeDeMedida;
import br.com.empresa.bonal.repositorio.ProdutoRepositorio;
import br.com.empresa.bonal.repositorio.CategoriaRepositorio;
import br.com.empresa.bonal.repositorio.UnidadeDeMedidaRepositorio;

public class ProdutoTeste {
	
	
	public static void main(String[] args) {
		
		ProdutoRepositorio pr = new ProdutoRepositorio();
		
		
		//cuidado com essas buscas por id. Buscar sempre por ids existentes no banco
	
		
		Produto p = new Produto();
		p.setCodigo("001");
		p.setNome("Produto de Teste");
		p.setDescricao("Produto usado nos testes unitarios");
		p.setQuantidade(new BigDecimal("0"));
		
		pr.adicionar(p, 1L, 1L);

		System.out.println("Produto adicionado");
		System.out.println(p.toString());

		Produto aux = new Produto();
		aux = pr.buscarPorId(p.getId());
		System.out.println("Produto recuperado");
		System.out.println(aux.toString());
		
		System.out.println("Codigo irá ser editado para '002'");
		aux.setCodigo("002");
		pr.atualizar(aux, 1L, 1L);
		
		aux = pr.buscarPorId(aux.getId());
		System.out.println("Produto editado");
		System.out.println(aux.toString());
		
		
	}

}
