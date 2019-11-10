package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import play.data.validation.Min;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class Transferencia extends Model{
	
	@Required
	@Min(1)
	public double valor;
	
	@Required
	public String numC;
	
	@Required
	public long ag;
	
	@Required
	public long op;
	
	@Temporal(TemporalType.DATE)
	public Date dataTransferencia;
	
	@ManyToOne
	public Conta conta;


}
