package controllers;

import play.*;
import play.data.validation.Valid;
import play.mvc.*;

import java.util.*;

import models.*;

public class Application extends Controller {

	public static void index() {
		render();
	}

	public static void login(String username, String senha) {
		Usuario usuario = Usuario.find("nomeUsuario = ? and senha = ?", username, senha).first();
		if (usuario != null) {
			session.put("usuario", username);
			Contas.inicio(usuario.conta.id);
		} else {
			flash.error("Usuario ou senha invalidos");
			index();
		}

	}

	public static void cadastro() {
		render();
	}

	public static void salvar(@Valid Usuario user) {
		
		if(validation.hasErrors()) {
			params.flash();
			validation.keep();
			cadastro();
		}
		
		Usuario testeUser = null;
		
		if (user.id == null) {
			testeUser = Usuario.find("nomeUsuario = ? and email = ?", user.nomeUsuario,user.email).first();
		}
		if (testeUser == null) {
			user.save();
			Application.index();
		} else {
			flash.error("Nome de usuario ou E-Mail existente");
			cadastro();
		}

	}

	public static void cadastroC() {
		render();
	}

	static Random rand = new Random();

	public static void salvarC(@Valid Conta conta) {

		if(validation.hasErrors()) {
			params.flash();
			validation.keep();
			cadastroC();
		}
		Conta usuario = Conta.find("id <> ? ", conta.id).first();
		conta.agencia = Integer.parseInt(rand.nextInt(9) + rand.nextInt(9) + rand.nextInt(9)+"");
		conta.numeroDaConta = rand.nextInt(9) + rand.nextInt(9) + "" + rand.nextInt(9) + rand.nextInt(9) + ""
				+ rand.nextInt(9) + "-" + rand.nextInt(9);
		conta.save();
		Saldo saldo = new Saldo();
		saldo.valor = 0;
		saldo.conta = conta;
		saldo.save();
		renderTemplate("Application/cadastro.html", conta);

	}
	public static void deslogar() {
		session.clear();
		index();
	}

}