package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class Conta extends Model{
	
	@Required
	public String nomeProprietario;
	
	public int agencia;
	
	public int operacao;
	
	public String numeroDaConta;
	
	@Required
	public String senha;
	
}
