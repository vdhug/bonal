package br.com.empresa.bonal.entidades;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@SuppressWarnings("serial")
@Entity
@DiscriminatorValue("FUNCIONARIO")
public class Funcionario extends Pessoa implements Serializable {


	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "cargo")
	private Cargo cargo;


	public Cargo getCargo() {
		return cargo;
	}

	public void setCargo(Cargo cargo) {
		this.cargo = cargo;
	}

}
