package controllers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
public class Contas extends Controller {

	static Random rand = new Random();

	public static void salvar(@Valid Conta conta) {
		if(validation.hasErrors()) {
			params.flash();
			validation.keep();
			auxEdit(conta.id);
		}
		
		Saldo quantiatual = Saldo.find("conta_id = ?", conta.id).first();
		Conta cnt = Conta.findById(conta.id);
		conta.agencia = cnt.agencia;
		conta.numeroDaConta = cnt.numeroDaConta;
		conta.save();
		List<Deposito> depositos = Deposito.findAll();
		List<Saques> saques = Saques.findAll();
		List<Saques> pagamentos = Pagamento.findAll();
		Usuario user = Usuario.find("conta_id = ?", conta.id).first();
		Deposito saldo = Deposito.find("conta_id = ?", conta.id).first();
		//renderTemplate("Contas/inicio.html", conta, depositos, saques, quantiatual, pagamentos, user, saldo);
		inicio(conta.id);
	}


	public static void inicio(long id) {
		Conta conta = Conta.findById(id);
		Deposito saldo = Deposito.find("conta_id = ?", id).first();
		Usuario user = Usuario.find("conta_id = ?", id).first();
		if (saldo == null) {
			renderTemplate("Contas/inicio.html", conta, user);
		} else {
			Saldo quantiatual = Saldo.find("conta_id = ?", id).first();
			List<Deposito> depositos = Deposito.findAll();
			List<Saques> saques = Saques.findAll();
			List<Saques> pagamentos = Pagamento.findAll();
			renderTemplate("Contas/inicio.html", conta, saldo, quantiatual, user, depositos, saques, pagamentos);
		}
	}

	public static void auxEdit(long id) {
		Conta conta = Conta.findById(id);
		renderTemplate("Contas/editar.html", conta);
	}

	public static void busca(long id, Date data) {
		Calendar gc = Calendar.getInstance();
		
		if (gc.getTime().before(data) ) {
			flash.error("Data escolhida inválida");
			inicio(id);
		}
		gc.setTime(data);

		Calendar gcT = Calendar.getInstance();

		List<Deposito> depositosT = Deposito.findAll();
		List<Saques> saquesT = Saques.findAll();
		List<Pagamento> pagamentosT = Pagamento.findAll();

		List<Deposito> depositos = new ArrayList();
		List<Saques> saques = new ArrayList();
		List<Pagamento> pagamentos = new ArrayList();

		for (Deposito deposito : depositosT) {
			gcT.setTime(deposito.dataDeposito);
			if (gc.get(gc.MONTH) == gcT.get(gcT.MONTH)) {
				depositos.add(deposito);
			}
		}

		for (Saques saque : saquesT) {
			gcT.setTime(saque.dataSaque);
			if (gc.get(gc.MONTH) == gcT.get(gcT.MONTH)) {
				saques.add(saque);
			}
		}

		for (Pagamento pagamento : pagamentosT) {

			gcT.setTime(pagamento.dataPagamento);
			if (gc.get(gc.MONTH) == gcT.get(gcT.MONTH)) {
				pagamentos.add(pagamento);
			}
		}
		Conta conta = Conta.findById(id);
		Saldo quantiatual = Saldo.find("conta_id = ?", id).first();
		Usuario user = Usuario.find("conta_id = ?", id).first();

		renderTemplate("Contas/inicio.html", conta, quantiatual, user, depositos, saques, pagamentos);
	}
}
