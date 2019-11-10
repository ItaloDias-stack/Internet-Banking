package controllers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import models.Conta;
import models.Deposito;
import models.Pagamento;
import models.Saldo;
import models.Transferencia;
import models.Usuario;
import play.data.validation.Valid;
import play.mvc.Controller;
import play.mvc.With;
import play.mvc.results.RenderTemplate;

@With(Seguranca.class)
public class Transferencias extends Controller{
	
	public static void transferencia(long id) {
		Conta conta = Conta.findById(id);
		renderTemplate("Transferencias/transferencia.html",conta);
	}
	
	public static void transfere(@Valid Transferencia transferencia) {
		if (validation.hasErrors()) {
			params.flash();
			validation.keep();
			transferencia(transferencia.conta.id);
		}
		Saldo saldo = Saldo.find("conta_id = ?", transferencia.conta.id).first();
		if(saldo.valor>=transferencia.valor) {
			
			Conta contaRe = Conta.find("agencia = ? and numeroDaConta = ? and operacao = ?",Integer.parseInt(transferencia.ag+""),transferencia.numC,Integer.parseInt(transferencia.op+"")).first();
			if(contaRe != null) {
				
				Calendar calendar = new GregorianCalendar();
				Date date = new Date();
				calendar.setTime(date);
				
				saldo.valor = saldo.valor - transferencia.valor;
				saldo.save();
				transferencia.dataTransferencia = calendar.getTime();
				transferencia.save();
				
				Saldo saldo2  = Saldo.find("conta_id", contaRe.id).first();
				saldo2.valor = saldo2.valor + transferencia.valor;
				saldo2.save();
				
				
				Email email = new Email();
				Usuario user = Usuario.find("conta_id = ?",transferencia.conta.id).first();
				email.mandaEmail("Comprovante de saque", "Foi transferido: R$"+transferencia.valor+""
						+ " da conta: "+transferencia.conta.numeroDaConta+" Agencia: "+transferencia.conta.agencia+" Operacao: "+ transferencia.conta.operacao+
						" para a conta: "+contaRe.numeroDaConta+" da agencia: "+Integer.parseInt(contaRe.agencia+"")+" de operacao: "+ contaRe.operacao, user.email);
				listarT(transferencia.conta.id);
			}else {
				flash.error("Esta conta nao existe");
				transferencia(transferencia.conta.id);
			}
			
		}else {
			flash.error("Saldo insufuciente");
			transferencia(transferencia.conta.id);
		}
		
	}
	
	public static void listarT(long id) {
		List<Transferencia> transferencias = Transferencia.findAll();
		Conta conta = Conta.findById(id);
		renderTemplate("Transferencias/listarT.html",transferencias,conta);
	}
	
	public void buscarT(long id,Date data) {
		Calendar gc = Calendar.getInstance();
		
		if (gc.getTime().before(data) ) {
			flash.error("Data escolhida inválida");
			listarT(id);
		}
		gc.setTime(data);

		Calendar gcT = Calendar.getInstance();
		
		List<Transferencia> transferenciass = Transferencia.findAll();
		List<Transferencia> transferencias = new ArrayList();
		
		for (Transferencia transferencia : transferenciass) {
			gcT.setTime(transferencia.dataTransferencia);
			if (gc.get(gc.MONTH) == gcT.get(gcT.MONTH)) {
				transferencias.add(transferencia);
			}
		}
		Conta conta = Conta.findById(id);
		renderTemplate("Transferencias/listarT.html",transferencias,conta);
		
	}

}
