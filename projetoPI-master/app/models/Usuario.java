package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import play.data.validation.Required;
import play.db.jpa.Blob;
import play.db.jpa.Model;

@Entity
public class Usuario extends Model{
	
	public Blob foto;
	@Required
	public String email;
	
	@Required
	public String nomeUsuario;
	
	@Required
	public String senha;
	
	@OneToOne
	public Conta conta;
	
	
	
}
