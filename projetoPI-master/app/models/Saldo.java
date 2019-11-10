package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class Saldo extends Model{
	
	
	public double valor;
	@ManyToOne
	public Conta conta;
}
