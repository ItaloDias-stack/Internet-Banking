package models;

import java.util.Date;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.data.validation.InPast;
import play.data.validation.Min;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class Deposito extends Model{
	
	@Required
	@Min(1)
	public double saldo;
	
	@Temporal(TemporalType.DATE)
	public Date dataDeposito;
	
	@ManyToOne
	public Conta conta;
}
