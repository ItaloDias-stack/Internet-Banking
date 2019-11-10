package controllers;

import play.mvc.Before;
import play.mvc.Controller;

public class Seguranca extends Controller{

	@Before
	public static void checa() {
		if (session.isEmpty()) {
			Application.index();
		}
	}
}
