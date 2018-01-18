package br.com.empresa.bonal.entidades;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@SuppressWarnings("serial")
@Entity
@DiscriminatorValue("servico")
public class Servico extends ItemDeProducao implements Serializable{
	
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "unidade_de_medida")
	private UnidadeDeMedida unidadeDeMedida;	
	
	
	
	
	// @PrePersist
	// @PreUpdate
	// public void gerarCodigo() {
	// StringBuilder builder = new StringBuilder();
	// builder.append(this.tipoBem);
	// builder.append(codigo);
	// this.codigo = builder.toString();
	// }

	
	public UnidadeDeMedida getUnidadeDeMedida() {
		return unidadeDeMedida;
	}




	public void setUnidadeDeMedida(UnidadeDeMedida unidadeDeMedida) {
		this.unidadeDeMedida = unidadeDeMedida;
	}

	
	public String resumo(){
		return "";
	}


	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(getClass().getSimpleName() + " {");
		builder.append("\n\tid= " + getId());
		builder.append("\n\tnome= " + getNome());
		builder.append("\n\tcodigo= " + getCodigo());
		builder.append("\n\tdescricao= " + getDescricao());
		builder.append("\n\tcategoria= " + getSubCategoria().getNome());
		builder.append("\n\tunidadeDeMedida= " + getUnidadeDeMedida().getNome());
		builder.append("\n\tdataCadastro=" + getDataCadastro().getTime());
		builder.append("\n\tversion=" + getVersion());
		builder.append("\n }");
		return builder.toString();
	}

}
