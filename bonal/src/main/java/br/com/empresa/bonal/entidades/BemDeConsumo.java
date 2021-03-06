package br.com.empresa.bonal.entidades;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


@SuppressWarnings("serial")
@Entity
@DiscriminatorValue("bem_consumo")
public class BemDeConsumo extends ItemDeProducao implements Serializable{
	
	
	private BigDecimal quantidade = new BigDecimal("0");
	
	
	public BigDecimal getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(BigDecimal quantidade) {
		this.quantidade = quantidade;
	}
	
	@Override
	public String toString(){
		return "Nome: "+getNome()+
				"subcategoria: "+getSubCategoria().getNome()+
				"unidade de medida: "+getUnidadeDeMedida().getNome();
	}
}
