package controller;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import common.exception.InvalidCardException;
import common.exception.PaymentException;
import common.exception.UnrecognizedException;
import entity.transaction.CreditCard;
import entity.transaction.Transaction;
import subsystem.InterbankInterface;
import subsystem.InterbankSubsystem;
import utils.Configs;


/**
 * This {@code TransactionController} class control the flow of the payment process
 * in our EcobikeRental Software.
 * 
 * @author NhungTTH
 *
 */
public class TransactionController extends BaseController {

	/**
	 * Represent the card used for transaction
	 */
	private CreditCard card;

	/**
	 * Represent the Interbank subsystem
	 */
	private InterbankInterface interbank;

	/**
	 * process return transaction, and then return the result with a message.
	 * 
	 * @param deposit         - the deposit to refund
	 * @param fees			- the amount to pay
	 * @param contents       - the transaction contents
	 * @param cardNumber     - the card number
	 * @param cardHolderName - the card holder name
	 * @param expirationDate - the expiration date in the format "mm/yy"
	 * @param securityCode   - the cvv/cvc code of the credit card
	 * @return {@link java.util.Map Map} represent the transaction result with a
	 *         message.
	 */
	public Map<String, String> returnTransaction(int deposit, int fees, String contents, CreditCard card) {
		Map<String, String> result = new Hashtable<String, String>();
		result.put("RESULT", "TRANSACTION FAILED!");
		try {
			this.card = card;
			this.card.setDateExpired(getExpirationDate(card.getDateExpired()));

			this.interbank = new InterbankSubsystem();
			int amount = 0;
			// refund
			amount = deposit;
			Transaction refund = interbank.refundTransaction(card, amount, contents);
			refund.setPurpose(Configs.RETURN);
			Transaction.saveTransaction(refund);
			
			// pay fees
			amount = fees;
			Transaction pay = interbank.payTransaction(card, amount, contents);
			pay.setPurpose(Configs.RETURN);
			Transaction.saveTransaction(pay);
			
			result.put("RESULT", "TRANSACTION SUCCESSFUL!");
			result.put("MESSAGE", "You have succesffully paid the transaction!");
		} catch (PaymentException | UnrecognizedException ex) {
			result.put("MESSAGE", ex.getMessage());
		}
		return result;
	}

	/**
	 * process transaction, and then return the result with a message.
	 * 
	 * @param fees         - the amount to pay
	 * @param contents       - the transaction contents
	 * @param cardNumber     - the card number
	 * @param cardHolderName - the card holder name
	 * @param expirationDate - the expiration date in the format "mm/yy"
	 * @param securityCode   - the cvv/cvc code of the credit card
	 * @return {@link java.util.Map Map} represent the transaction result with a
	 *         message.
	 */
	public Map<String, String> rentTransaction(int fees, String contents,CreditCard card) {
		Map<String, String> result = new Hashtable<String, String>();
		result.put("RESULT", "TRANSACTION FAILED!");
		try {
			this.card = card;
			this.card.setDateExpired(getExpirationDate(card.getDateExpired()));

			this.interbank = new InterbankSubsystem();
			int amount = 0;
			// pay fees for rent a bike
			amount = fees;
			Transaction transaction = interbank.payTransaction(card, amount, contents);
			transaction.setPurpose(Configs.RENT);
			Transaction.saveTransaction(transaction);
	
			result.put("RESULT", "TRANSACTION SUCCESSFUL!");
			result.put("MESSAGE", "You have succesffully paid the transaction!");
		} catch (PaymentException | UnrecognizedException ex) {
			result.put("MESSAGE", ex.getMessage());
		}
		return result;
	}

	
	public boolean validateTransactionInfo(HashMap<String, String> message) {
		for (Map.Entry<String, String> pair: message.entrySet()) {
            if(pair.getValue() == "")
            	return false;
        }
		return true;
	}
	
	/**
	 * Validate the input date which should be in the format "mm/yy", and then
	 * return a {@link java.lang.String String} representing the date in the
	 * required format "mmyy" .
	 * 
	 * @param date - the {@link java.lang.String String} represents the input date
	 * @return {@link java.lang.String String} - date representation of the required
	 *         format
	 * @throws InvalidCardException - if the string does not represent a valid date
	 *                              in the expected format
	 */
	private String getExpirationDate(String date) throws InvalidCardException {
		String[] strs = date.split("/");
		if (strs.length != 2) {
			throw new InvalidCardException();
		}

		String expirationDate = null;
		int month = -1;
		int year = -1;

		try {
			month = Integer.parseInt(strs[0]);
			year = Integer.parseInt(strs[1]);
			if (month < 1 || month > 12 || year < Calendar.getInstance().get(Calendar.YEAR) % 100 || year > 100) {
				throw new InvalidCardException();
			}
			expirationDate = strs[0] + strs[1];

		} catch (Exception ex) {
			throw new InvalidCardException();
		}

		return expirationDate;
	}

}