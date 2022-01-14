package controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import utils.Configs;

class CalculateFeesTest {
	private ReturnBikeController returnBikeController;

	@BeforeEach
	void setUp() throws Exception {
		this.returnBikeController = new ReturnBikeController();
	}

	@ParameterizedTest
	@CsvSource({
		"5, 0",
		"15, 10000",
		"32, 10000",
		"45, 13000",
		"61, 16000"
	})
	void test(long time, int expected) {
		int total = this.returnBikeController.calculateFees(time, Configs.STANDARD);
		assertEquals(expected, total);
	}

}
