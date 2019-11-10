package controllers;

import java.util.List;
import java.util.Random;

import models.Conta;
import models.Deposito;
import models.Pagamento;
import models.Saldo;
import models.Saques;
import models.Usuario;
import play.data.validation.Valid;
import play.mvc.Controller;
import play.mvc.With;

@With(Seguranca.class)
public class Usuarios extends Controller {
	
	public static void deletar(long idUser, long idConta) {
		Usuario user = Usuario.findById(idUser);
		Conta cont = Conta.findById(idConta);

		List<Pagamento> pagamentos = Pagamento.findAll();
		for (int i = 0; i < pagamentos.size(); i++) {
			if (pagamentos.get(i).conta.id == idConta) {
				pagamentos.get(i).delete();
			}
		}

		List<Saldo> saldos = Saldo.findAll();
		for (int i = 0; i < saldos.size(); i++) {
			if (saldos.get(i).conta.id == idConta) {
				saldos.get(i).delete();
			}
		}

		List<Saques> saques = Saques.findAll();
		for (int i = 0; i < saques.size(); i++) {
			if (saques.get(i).conta.id == idConta) {
				saques.get(i).delete();
			}
		}

		List<Deposito> depositos = Deposito.findAll();
		for (int i = 0; i < depositos.size(); i++) {
			if (depositos.get(i).conta.id == idConta) {
				depositos.get(i).delete();
			}
		}

		user.delete();
		cont.delete();
		Application.index();
	}

	public static void editar(@Valid Usuario user) {
		if (validation.hasErrors()) {
			params.flash();
			validation.keep();
			auxEdit(user.id);
			// renderTemplate("Usuarios/editar.html",user);
		}
		Usuario usuario = Usuario.find("id <> ? and nomeUsuario = ? ", user.id, user.nomeUsuario).first();
		if (usuario == null) {
			user.save();
			Application.index();
		} else {
			flash.error("Esse nome de usuario ja esta vinculada a uma conta");
			auxEdit(user.id);
			// renderTemplate("Usuarios/editar.html",user);
		}

	}

	public static void auxEdit(Long id) {
		Usuario user = Usuario.findById(id);
		renderTemplate("Usuarios/editar.html", user);
	}
	
	public  static  void  fotoUsuario(Long  id) {
	    Usuario usuario = Usuario.findById(id);
	    notFoundIfNull(usuario);
	    response.setContentTypeIfNotSet(usuario.foto.type());
	    renderBinary(usuario.foto.get());
	}
}
