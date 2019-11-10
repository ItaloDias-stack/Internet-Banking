package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import play.data.validation.InPast;
import play.data.validation.Min;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class Pagamento extends Model{

	@Required
	@Min(1)
	public double valorDoPagamento;
	
	@Temporal(TemporalType.DATE)
	public Date dataPagamento;
	
	@ManyToOne
	public Conta conta;
}
