package controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class CalculateDepositTest {
	private RentBikeController rentBikeController;

	@BeforeEach
	void setUp() throws Exception {
		this.rentBikeController = new RentBikeController();
	}

	@ParameterizedTest
	@CsvSource({
		"other, 0",
		"Standard Bike, 400000",
		"Standard E-bike, 700000",
		"Twin Bike, 550000",
	})
	void test(String type, int expected) {
		int deposit = this.rentBikeController.calculateDeposit(type);
		assertEquals(expected, deposit);
	}

}
