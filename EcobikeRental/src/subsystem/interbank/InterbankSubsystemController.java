package subsystem.interbank;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Map;

import common.exception.InternalServerErrorException;
import common.exception.InvalidCardException;
import common.exception.InvalidTransactionAmountException;
import common.exception.InvalidVersionException;
import common.exception.NotEnoughBalanceException;
import common.exception.NotEnoughTransactionInfoException;
import common.exception.SuspiciousTransactionException;
import common.exception.UnrecognizedException;
import entity.transaction.CreditCard;
import entity.transaction.Transaction;
import utils.Configs;
import utils.MyMap;
import utils.Utils;

public class InterbankSubsystemController {

	private static final String PUBLIC_KEY = "Bl8Ytl9yVqs=";
	private static final String SECRET_KEY = "BXutHoU0hvg=";
	private static final String VERSION = "1.0.0";

	private static InterbankBoundary interbankBoundary = new InterbankBoundary();
	
	private String generateData(Map<String, Object> data) {
		return ((MyMap) data).toJSON();
	}

	public Transaction payTransaction(CreditCard card, int amount, String contents) {
		Map<String, Object> transaction = new MyMap();

		try {
			transaction.putAll(MyMap.toMyMap(card));
		} catch (IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			throw new InvalidCardException();
		}
		transaction.put("command", Configs.PAY);
		transaction.put("transactionContent", contents);
		transaction.put("amount", amount);
		Timestamp today = Utils.getToday();
		String s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(today);
		transaction.put("createdAt", s);

		Map<String, Object> requestMap = new MyMap();
		requestMap.put("version", VERSION);
		requestMap.put("transaction", transaction);

		String responseText = interbankBoundary.query(Configs.PROCESS_TRANSACTION_URL, generateData(requestMap));
		MyMap response = null;
		try {
			response = MyMap.toMyMap(responseText, 0);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			throw new UnrecognizedException();
		}

		return makeTransaction(response);
	}
	
	public Transaction refundTransaction(CreditCard card, int amount, String contents) {
		Map<String, Object> transaction = new MyMap();

		try {
			transaction.putAll(MyMap.toMyMap(card));
		} catch (IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			throw new InvalidCardException();
		}
		transaction.put("command", Configs.REFUND);
		transaction.put("transactionContent", contents);
		transaction.put("amount", amount);
		Timestamp today = Utils.getToday();
		String s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(today);
		transaction.put("createdAt", s);

		Map<String, Object> requestMap = new MyMap();
		requestMap.put("version", VERSION);
		requestMap.put("transaction", transaction);

		String responseText = interbankBoundary.query(Configs.PROCESS_TRANSACTION_URL, generateData(requestMap));
		MyMap response = null;
		try {
			response = MyMap.toMyMap(responseText, 0);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			throw new UnrecognizedException();
		}

		return makeTransaction(response);
	}

	private Transaction makeTransaction(MyMap response) {
		if (response == null)
			return null;
		MyMap transaction = (MyMap) response.get("transaction");
		Transaction trans = null;
		if(transaction != null) {
			CreditCard card = new CreditCard((String) transaction.get("cardCode"), (String) transaction.get("owner"),
					Integer.parseInt((String) transaction.get("cvvCode")), (String) transaction.get("dateExpired"));
			trans = new Transaction(Integer.parseInt((String) transaction.get("amount")), (String) transaction.get("transactionContent"));
			trans.setId((String) transaction.get("transactionId"));
			trans.setType((String) transaction.get("command"));
		}
		
		switch ((String)response.get("errorCode")) {
		case "00":
			break;
		case "01":
			throw new InvalidCardException();
		case "02":
			throw new NotEnoughBalanceException();
		case "03":
			throw new InternalServerErrorException();
		case "04":
			throw new SuspiciousTransactionException();
		case "05":
			throw new NotEnoughTransactionInfoException();
		case "06":
			throw new InvalidVersionException();
		case "07":
			throw new InvalidTransactionAmountException();
		default:
			throw new UnrecognizedException();
		}

		return trans;
	}

}
