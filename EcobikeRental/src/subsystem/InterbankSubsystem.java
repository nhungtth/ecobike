package subsystem;

import common.exception.InternalServerErrorException;
import common.exception.InvalidCardException;
import common.exception.NotEnoughBalanceException;
import entity.transaction.CreditCard;
import entity.transaction.Transaction;
import subsystem.interbank.InterbankSubsystemController;

/***
 * The {@code InterbankSubsystem} class is used to communicate with the
 * Interbank to make transaction.
 * 
 * @author NhungTTH
 *
 */
public class InterbankSubsystem implements InterbankInterface {

	/**
	 * Represent the controller of the subsystem
	 */
	private InterbankSubsystemController ctrl;

	/**
	 * Initializes a newly created {@code InterbankSubsystem} object so that it
	 * represents an Interbank subsystem.
	 */
	public InterbankSubsystem() {
		String str = new String();
		this.ctrl = new InterbankSubsystemController();
	}

	/**
	 * @see InterbankInterface#payTransaction(entity.transaction.CreditCard, int,
	 *      java.lang.String, java.lang.String)
	 */
	public Transaction payTransaction(CreditCard card, int amount, String contents) {
		Transaction transaction = ctrl.payTransaction(card, amount, contents);
		return transaction;
	}
	
	/**
	 * @see InterbankInterface#refundTransaction(entity.transaction.CreditCard, int,
	 *      java.lang.String, java.lang.String)
	 */
	public Transaction refundTransaction(CreditCard card, int amount, String contents) {
		Transaction transaction = ctrl.refundTransaction(card, amount, contents);
		return transaction;
	}
}
