package common.exception;

import java.io.IOException;

import views.screen.popup.PopupScreen;

public class PaymentException extends RuntimeException {
	public PaymentException(String message) {
		super(message);
		try {
			PopupScreen.error(message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
