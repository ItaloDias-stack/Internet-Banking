package controllers;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

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
public class Depositos extends Controller {

	static Email email = new Email();
	public static void formDeposito(Long idConta) {
		Conta conta = Conta.findById(idConta);
		renderTemplate("Depositos/formDeposito.html", conta);
	}

	public static void formSaque(Long idConta) {
		Conta conta = Conta.findById(idConta);
		renderTemplate("Depositos/formSaque.html", conta);
	}

	public static void formPagamentos(Long idConta) {
		Conta conta = Conta.findById(idConta);
		renderTemplate("Depositos/formPagamentos.html", conta);
	}

	public static void depositar(@Valid Deposito deposito) {

		if (validation.hasErrors()) {
			params.flash();
			validation.keep();
			formDeposito(deposito.conta.id);
		}

		Calendar calendar = new GregorianCalendar();
		Date date = new Date();
		calendar.setTime(date);
		
		Deposito saldo = new Deposito();
		saldo.conta = deposito.conta;
		saldo.dataDeposito = calendar.getTime();
		saldo.saldo = saldo.saldo + deposito.saldo;
		saldo.save();
		
		Saldo quantiatual = new Saldo();
		Saldo quantia = null;
		quantia = Saldo.find("conta_id = ?", deposito.conta.id).first();
		if(quantia==null) {
						
			quantiatual.conta = deposito.conta;
			quantiatual.valor = deposito.saldo;
			quantiatual.save();
		}else {
			
			quantiatual=quantia;
			quantiatual.valor += deposito.saldo;
			quantiatual.conta = deposito.conta;
			quantiatual.save();
			
		}
		// <-
		//Conta conta = Conta.findById(idC);
		Usuario user = Usuario.find("conta_id = ?", deposito.conta.id).first();
		List<Deposito> depositos = Deposito.findAll();
		List<Saques> saques = Saques.findAll();
		Conta conta = Conta.findById(deposito.conta.id);
		List<Pagamento> pagamentos = Pagamento.findAll();
		
		email.mandaEmail("Comprovante de deposito", "Foi depositado: R$"+deposito.saldo+" na conta: "+deposito.conta.numeroDaConta+" Agencia: "+Integer.parseInt(deposito.conta.agencia+"")+" Operacao: "+ deposito.conta.operacao, user.email);
		renderTemplate("Contas/inicio.html",conta,quantiatual,user,depositos,saques,pagamentos);
	}

	public static void pagar(@Valid Pagamento pagar) {

		if (validation.hasErrors()) {
			params.flash();
			validation.keep();
			formPagamentos(pagar.conta.id);
		}

		Calendar calendar = new GregorianCalendar();
		Date date = new Date();
		calendar.setTime(date);

		Conta conta = Conta.findById(pagar.conta.id);
		Saldo total = Saldo.find("conta_id = ?", conta.id).first();

		if (total.valor >= pagar.valorDoPagamento) {
			Pagamento pg = new Pagamento();
			pg.conta = conta;
			pg.dataPagamento = calendar.getTime();
			pg.valorDoPagamento = pagar.valorDoPagamento;
			pg.save();
			// Conta conta = Conta.findById(id);

			Saldo quantiatual = Saldo.find("conta_id = ?", conta.id).first();
			quantiatual.valor -= pagar.valorDoPagamento;
			quantiatual.save();
			Usuario user = Usuario.find("conta_id = ?", conta.id).first();
			List<Deposito> depositos = Deposito.findAll();
			List<Saques> saques = Saques.findAll();
			List<Pagamento> pagamentos = Pagamento.findAll();
			email.mandaEmail("Comprovante de pagamento", "Foi pago: R$"+pagar.valorDoPagamento+ "pela conta: "+pagar.conta.numeroDaConta+" da agencia: "+Integer.parseInt(pagar.conta.agencia+""), user.email);
			renderTemplate("Contas/inicio.html", conta, quantiatual, user, depositos, saques, pagamentos);
		} else {
			flash.error("Saldo insuficinete para realizar o pagamento");
			formPagamentos(conta.id);
			
		}
	}

	public static void sacar(@Valid Saques saquess) {
		if (validation.hasErrors()) {
			params.flash();
			validation.keep();
			formSaque(saquess.conta.id);
		}
		Calendar calendar = new GregorianCalendar();
		Date date = new Date();
		calendar.setTime(date);

		// Conta conta = Conta.find("id = ?", saquess.conta.id).first();
		Saldo total = Saldo.find("conta_id = ?", saquess.conta.id).first();
		Conta conta = Conta.findById(saquess.conta.id);
		if (total.valor >= saquess.valorDoSaque) {

			Saques saque = new Saques();
			saque.conta = saquess.conta;
			saque.dataSaque = calendar.getTime();
			saque.valorDoSaque = saquess.valorDoSaque;
			saque.save();
			// Conta conta = Conta.findById(id);

			Saldo quantiatual = Saldo.find("conta_id = ?", saquess.conta.id).first();
			quantiatual.valor -= saquess.valorDoSaque;
			quantiatual.save();
			Usuario user = Usuario.find("conta_id = ?", saquess.conta.id).first();
			List<Deposito> depositos = Deposito.findAll();
			List<Saques> saques = Saques.findAll();

			List<Pagamento> pagamentos = Pagamento.findAll();
			email.mandaEmail("Comprovante de saque", "Foi sacado: R$"+saquess.valorDoSaque+" da conta: "+saquess.conta.numeroDaConta+" Agencia: "+Integer.parseInt(saquess.conta.agencia+"")+" Operacao: "+ saquess.conta.operacao, user.email);
			renderTemplate("Contas/inicio.html", conta, quantiatual, user, depositos, saques, pagamentos);
		} else {
			flash.error("Saldo insuficinete");
			formSaque(conta.id);
			
		}

	}

}
