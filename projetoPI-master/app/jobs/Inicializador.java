package jobs;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import models.Conta;
import models.Deposito;
import models.Pagamento;
import models.Saldo;
import models.Saques;
import models.Transferencia;
import models.Usuario;
import play.jobs.Job;
import play.jobs.OnApplicationStart;

@OnApplicationStart
public class Inicializador extends Job {

	@Override
	public void doJob() throws Exception {
		Conta conta = new Conta();
		
		Calendar calendar = new GregorianCalendar();
		Date date = new Date();
		calendar.setTime(date);
		
		if (Conta.count() == 0) {
			conta.agencia = 1;
			conta.nomeProprietario = "12345";
			conta.numeroDaConta = "12357";
			conta.operacao = 12;
			conta.senha = "sdf";
			conta.save();
		}

		if (Usuario.count() == 0) {
			Usuario u = new Usuario();
			u.email = "fvkmv";
			u.nomeUsuario = "12345";
			u.senha = "12345";
			u.conta = conta;
			u.save();
		}
		
		if(Deposito.count()==0) {
			Deposito d = new Deposito();
			d.conta = conta;
			d.dataDeposito=calendar.getTime();
			d.saldo=250;
			d.save();
		}
		
		if(Saldo.count()==0) {
			Saldo saldo = new Saldo();
			saldo.conta=conta;
			saldo.valor=2345;
			saldo.save();
		}
		
		if(Pagamento.count()==0) {
			Pagamento p = new Pagamento();
			p.conta=conta;
			p.dataPagamento=calendar.getTime();
			p.valorDoPagamento=123456;
			p.save();
		}
		if(Saques.count()==0) {
			Saques s = new Saques();
			s.conta=conta;
			s.valorDoSaque=12345;
			s.dataSaque=calendar.getTime();
			s.save();
		}
		
		if(Transferencia.count()==0){
			Transferencia t = new Transferencia();
			t.ag=12;
			t.conta=conta;
			t.dataTransferencia=calendar.getTime();
			t.numC="1234567";
			t.op=3;
			t.valor=200;
			t.save();
		}

	}
}
