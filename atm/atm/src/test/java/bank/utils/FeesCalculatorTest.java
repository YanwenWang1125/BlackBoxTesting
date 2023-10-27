package bank.utils;

import static org.junit.jupiter.api.Assertions.*;

import atm.exceptions.InvalidAmountException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;

// Use of Parameterized helps in this case, since multiple runs of same test are required
class FeesCalculatorTest {
	FeesCalculator calculator = new FeesCalculator();

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	public void withdrawalSliceBasedTest(){

		/*
		* Test case 1:
			amount = 200
			accountBalance = 5000
			student = isStudent
			dayOfWeek = Friday
			Expected Output:
			fee = 0
			Statement covers:{1, 2, 3, 4, 5, 7, 15}
			statement coverage = 7/15 = 46.7%
		* */
		double withdrawAmount = 200;
		double amountBalance = 5000;
		boolean isStudent = true;
		int day = Calendar.FRIDAY;
		double feeRate = 0;
		double expectFee = withdrawAmount * feeRate;
		assertEquals(expectFee, calculator.calculateWithdrawalFee(withdrawAmount, amountBalance, isStudent, day));

		/*
		*
		Test case 2:
			Input:
			amount = 200
			accountBalance = 5000
			student = isStudent
			dayOfWeek = Sunday
			Expected Output:
			fee = amount * 0.001
			Statement covers:{1, 2, 3, 4, 6, 7, 15}
			statement coverage = 7/15 = 46.7%
		* */
		withdrawAmount = 200;
		amountBalance = 5000;
		isStudent = true;
		day = Calendar.SUNDAY;
		feeRate = 0.001;
		expectFee = withdrawAmount * feeRate;
		assertEquals(expectFee, calculator.calculateWithdrawalFee(withdrawAmount, amountBalance, isStudent, day));

		/*
		* Test case 3:
			Input:
			amount = 200
			accountBalance = 500
			student = notStudent
			dayOfWeek = Sunday
			Expected Output:
			fee = amount * 0.003
			Statement covers:{1, 2, 3, 8, 9, 14, 15}
			statement coverage = 7/15 = 46.7%
		* */
		withdrawAmount = 200;
		amountBalance = 500;
		isStudent = false;
		day = Calendar.SUNDAY;
		feeRate = 0.003;
		expectFee = withdrawAmount * feeRate;
		assertEquals(expectFee, calculator.calculateWithdrawalFee(withdrawAmount, amountBalance, isStudent, day));

		/*
		*Test case 4:
			Input:
			amount = 200
			accountBalance = 2000
			student = notStudent
			dayOfWeek = Sunday
			Expected Output:
			fee = amount * 0.001
			Statement covers:{1, 2, 3, 8, 10, 11, 13, 14, 15}
			statement coverage = 9/15 = 60.0%
		* */
		withdrawAmount = 200;
		amountBalance = 2000;
		isStudent = false;
		day = Calendar.SUNDAY;
		feeRate = 0.001;
		expectFee = withdrawAmount * feeRate;
		assertEquals(expectFee, calculator.calculateWithdrawalFee(withdrawAmount, amountBalance, isStudent, day));

		/*
		* Test case 5:
			Input:
			amount = 200
			accountBalance = 9000
			student = notStudent
			dayOfWeek = Sunday
			Expected Output:
			fee = 0
			Statement covers:{1, 2, 3, 8, 10, 12, 13, 14, 15}
			statement coverage = 9/15 = 60.0%
		* */
		withdrawAmount = 200;
		amountBalance = 9000;
		isStudent = false;
		day = Calendar.SUNDAY;
		feeRate = 0;
		expectFee = withdrawAmount * feeRate;
		assertEquals(expectFee, calculator.calculateWithdrawalFee(withdrawAmount, amountBalance, isStudent, day));

	}


	@Test
	public void depositDUPathsTest(){
		/*
		* Test Case 1:
			amount = 200
			accountBalance = 5000
			student = isStudent
			Expected Output:
			fee = amount * 0.005
			statement covers:{1-2-3-4-5-6-7-8-9-10-11-12-15-23-41-42-43-44}
			statement coverage = 18/44 = 0.409
		* */
		boolean isStudent = true;
		double amountDeposit = 200;
		double amountBalance = 5000;
		double interestPercentage = 0.005;
		double expectInterest = interestPercentage * amountDeposit;
		assertEquals(expectInterest, calculator.calculateDepositInterest(amountDeposit, amountBalance, isStudent));


		/*
		* Test Case 2:
			amount = 200
			accountBalance = 500
			student = isStudent
			Expected Output:
			fee = amount * 0.0025
			statement covers:{1-2-3-4-5-6–7-8-9-10-13-14-15-23-41-42-43-44}
			statement coverage = 18/44 = 0.409
		* */
		isStudent = true;
		amountDeposit = 200;
		amountBalance = 500;
		interestPercentage = 0.0025;
		expectInterest = interestPercentage * amountDeposit;
		assertEquals(expectInterest, calculator.calculateDepositInterest(amountDeposit, amountBalance, isStudent));

		/*
		* Test Case 3:
			amount = 50
			accountBalance = 6000
			student = isStudent
			Expected Output:
			fee = amount * 0.005
			statement covers:{1-2-3-4-5-6-7-8-16-17-18-19-22-23-41-42-43-44}
			statement coverage = 18/44 = 0.409
		* */
		isStudent = true;
		amountDeposit = 50;
		amountBalance = 6000;
		interestPercentage = 0.005;
		expectInterest = interestPercentage * amountDeposit;
		assertEquals(expectInterest, calculator.calculateDepositInterest(amountDeposit, amountBalance, isStudent));

		/*
		* Test Case 4:
			amount = 50
			accountBalance = 5000
			student = isStudent
			Expected Output:
			fee = amount * 0.002
			statement covers:{1-2-3-4-5-6-7-8-16-17-20-21-22-23-41-42-43-44}
			statement coverage = 18/44 = 0.409
			!!Error: Code calculates result is not equal to requirement's specifies (0%)
		* */
		isStudent = true;
		amountDeposit = 50;
		amountBalance = 5000;
		interestPercentage = 0.002;
		expectInterest = interestPercentage * amountDeposit;
		assertEquals(expectInterest, calculator.calculateDepositInterest(amountDeposit, amountBalance, isStudent));

		/*
		* Test Case 5:
			amount = 300
			accountBalance = 5000
			student = isNotStudent
			Expected Output:
			fee = amount * 0.008
			statement covers:{1-2-3-4-5-6-24-25-26-27-28-29-32-40-41-42-43-44}
			statement coverage = 18/44 = 0.409
		* */
		isStudent = false;
		amountDeposit = 300;
		amountBalance = 5000;
		interestPercentage = 0.008;
		expectInterest = interestPercentage * amountDeposit;
		assertEquals(expectInterest, calculator.calculateDepositInterest(amountDeposit, amountBalance, isStudent));

		/*
		* Test Case 6:
			amount = 300
			accountBalance = 2500
			student = isNotStudent
			Expected Output:
			fee = amount * 0.004
			statement covers:{1-2-3-4-5-6-24-25-26-27-28-29-32-40-41-42-43-44}
			statement coverage = 18/44 = 0.409
		* */
		isStudent = false;
		amountDeposit = 300;
		amountBalance = 2500;
		interestPercentage = 0.004;
		expectInterest = interestPercentage * amountDeposit;
		assertEquals(expectInterest, calculator.calculateDepositInterest(amountDeposit, amountBalance, isStudent));

		/*
		* Test Case 7:
			amount = 200
			accountBalance = 12000
			student = isNotStudent
			Expected Output:
			fee = amount * 0.005
			statement covers:{1-2-3-4-5-6-24-25-26-27-28-29-32-40-41-42-43-44}
			statement coverage = 18/44 = 0.409
			!!Error: Code calculates result is not equal to requirement's specifies (0%).
		* */
		isStudent = false;
		amountDeposit = 200;
		amountBalance = 12000;
		interestPercentage = 0.005;
		expectInterest = interestPercentage * amountDeposit;
		assertEquals(expectInterest, calculator.calculateDepositInterest(amountDeposit, amountBalance, isStudent));

		/*
		* Test Case 8:
			amount = 200
			accountBalance = 5000
			student = isNotStudent
			Expected Output:
			fee = amount * 0.001
			statement covers:{1-2-3-4-5-6-24-25-26-27-28-29-32-40-41-42-43-44}
			statement coverage = 18/44 = 0.409
		* */
		isStudent = false;
		amountDeposit = 200;
		amountBalance = 5000;
		interestPercentage = 0.001;
		expectInterest = interestPercentage * amountDeposit;
		assertEquals(expectInterest, calculator.calculateDepositInterest(amountDeposit, amountBalance, isStudent));


	}


	@Test
	public void transferBasisPathTest(){
		// variable may change
		boolean isStudent = true;
		double amountTransfer = 0;
		double balanceComesFrom = 0;
		double balanceGoesIn = 0;
		double feePercentage = 0;
		double expectFee = amountTransfer * feePercentage;

		// case 1: input = isStudent & amountTransfer = 100 & balanceComesFrom = 1000 & balanceGoesIn = 500
		// output = 0.001 of the amountTransfer
		amountTransfer = 100;
		balanceComesFrom = 1000;
		balanceGoesIn = 500;
		feePercentage = 0.001;
		expectFee = amountTransfer * feePercentage;
		assertEquals(expectFee, calculator.calculateTransferFee(amountTransfer, balanceComesFrom, balanceGoesIn, isStudent));

		// case 2: input = isStudent & amountTransfer = 100 & balanceComesFrom = 1000 & balanceGoesIn = 1500
		// output = 0.0005 of the amountTransfer
		amountTransfer = 100;
		balanceComesFrom = 1000;
		balanceGoesIn = 1500;
		feePercentage = 0.0005;
		expectFee = amountTransfer * feePercentage;
		assertEquals(expectFee, calculator.calculateTransferFee(amountTransfer, balanceComesFrom, balanceGoesIn, isStudent));

		// case 3: input = isStudent & amountTransfer = 100 & balanceComesFrom = 3000 & balanceGoesIn = 500
		// output = 0.0005 of the amountTransfer
		amountTransfer = 100;
		balanceComesFrom = 3000;
		balanceGoesIn = 500;
		feePercentage = 0.0005;
		expectFee = amountTransfer * feePercentage;
		assertEquals(expectFee, calculator.calculateTransferFee(amountTransfer, balanceComesFrom, balanceGoesIn, isStudent));

		// case 4: input = isStudent & amountTransfer = 100 & balanceComesFrom = 3000 & balanceGoesIn = 1500
		// output = 0.00025 of the amountTransfer
		amountTransfer = 100;
		balanceComesFrom = 3000;
		balanceGoesIn = 1500;
		feePercentage = 0.00025;
		expectFee = amountTransfer * feePercentage;
		assertEquals(expectFee, calculator.calculateTransferFee(amountTransfer, balanceComesFrom, balanceGoesIn, isStudent));

		// Error: Code calculates result is not equal as requirements specifies(0.05%).
		// case 5: input = isStudent & amountTransfer = 300 & balanceComesFrom = 1000 & balanceGoesIn = 500
		// output = 0.005 of the amountTransfer
		amountTransfer = 300;
		balanceComesFrom = 1000;
		balanceGoesIn = 500;
		feePercentage = 0.005;
		expectFee = amountTransfer * feePercentage;
		assertEquals(expectFee, calculator.calculateTransferFee(amountTransfer, balanceComesFrom, balanceGoesIn, isStudent));


		// Error: Code calculates result is not equal to requirements specifies(0.025%).
		// case 6: input = isStudent & amountTransfer = 300 & balanceComesFrom = 1000 & balanceGoesIn = 1500
		// output = 0.0025 of the amountTransfer
		amountTransfer = 300;
		balanceComesFrom = 1000;
		balanceGoesIn = 1500;
		feePercentage = 0.0025;
		expectFee = amountTransfer * feePercentage;
		assertEquals(expectFee, calculator.calculateTransferFee(amountTransfer, balanceComesFrom, balanceGoesIn, isStudent));

		// Error: Code calculates result is not equal as requirement's specifies(0.25%).
		// case 7: input = isStudent & amountTransfer = 300 & balanceComesFrom = 3000 & balanceGoesIn = 500
		// output = 0.025 of the amountTransfer
		amountTransfer = 300;
		balanceComesFrom = 3000;
		balanceGoesIn = 500;
		feePercentage = 0.025;
		expectFee = amountTransfer * feePercentage;
		assertEquals(expectFee, calculator.calculateTransferFee(amountTransfer, balanceComesFrom, balanceGoesIn, isStudent));

		// Error: Code calculates result is not equal as requirement's specifies(0.125% ).
		// case 8: input = isStudent & amountTransfer = 300 & balanceComesFrom = 3000 & balanceGoesIn = 1500
		// output = 0.0125 of the amountTransfer
		amountTransfer = 300;
		balanceComesFrom = 3000;
		balanceGoesIn = 1500;
		feePercentage = 0.0125;
		expectFee = amountTransfer * feePercentage;
		assertEquals(expectFee, calculator.calculateTransferFee(amountTransfer, balanceComesFrom, balanceGoesIn, isStudent));

		// case 9: input = isNotStudent & amountTransfer = 50 & balanceComesFrom = 3000 & balanceGoesIn = 1000
		// output = 0.002 of the amountTransfer
		isStudent = false;
		amountTransfer = 50;
		balanceComesFrom = 3000;
		balanceGoesIn = 1000;
		feePercentage = 0.002;
		expectFee = amountTransfer * feePercentage;
		assertEquals(expectFee, calculator.calculateTransferFee(amountTransfer, balanceComesFrom, balanceGoesIn, isStudent));

		// Error: Code calculates result is not equal as requirements specifies(0.1%).
		// case 10: input = isNotStudent & amountTransfer = 50 & balanceComesFrom = 3000 & balanceGoesIn = 3000
		// output = 0.01 of the amountTransfer
		amountTransfer = 50;
		balanceComesFrom = 3000;
		balanceGoesIn = 3000;
		feePercentage = 0.01;
		expectFee = amountTransfer * feePercentage;
		assertEquals(expectFee, calculator.calculateTransferFee(amountTransfer, balanceComesFrom, balanceGoesIn, isStudent));

		// Error: Code calculates result is not equal as requirements specifies(1%)
		// case 11: input = isNotStudent & amountTransfer = 50 & balanceComesFrom = 5000 & balanceGoesIn = 1000
		// output = 0.1 of the amountTransfer
		amountTransfer = 50;
		balanceComesFrom = 5000;
		balanceGoesIn = 1000;
		feePercentage = 0.1;
		expectFee = amountTransfer * feePercentage;
		assertEquals(expectFee, calculator.calculateTransferFee(amountTransfer, balanceComesFrom, balanceGoesIn, isStudent));


		// Error: Code calculates result is not equal as requirement's specifies(0.5%).
		// case 12: input = isNotStudent & amountTransfer = 50 & balanceComesFrom = 5000 & balanceGoesIn = 3000
		// output = 0.03 of the amountTransfer
		amountTransfer = 50;
		balanceComesFrom = 5000;
		balanceGoesIn = 3000;
		feePercentage = 0.03;
		expectFee = amountTransfer * feePercentage;
		assertEquals(expectFee, calculator.calculateTransferFee(amountTransfer, balanceComesFrom, balanceGoesIn, isStudent));

		// Error: Code calculates result is not equal as requirement's specifies(0.2%).
		// case 13: input = isNotStudent & amountTransfer = 200 & balanceComesFrom = 1000 & balanceGoesIn = 500
		// output = 0.02 of the amountTransfer
		amountTransfer = 200;
		balanceComesFrom = 1000;
		balanceGoesIn = 500;
		feePercentage = 0.02;
		expectFee = amountTransfer * feePercentage;
		assertEquals(expectFee, calculator.calculateTransferFee(amountTransfer, balanceComesFrom, balanceGoesIn, isStudent));

		// Error: Code calculates result is not equal as requirement's specifies (0.1%).
		// case 14: input = isNotStudent & amountTransfer = 200 & balanceComesFrom = 1000 & balanceGoesIn = 2000
		// output = 0.01 of the amountTransfer
		amountTransfer = 200;
		balanceComesFrom = 1000;
		balanceGoesIn = 2000;
		feePercentage = 0.01;
		expectFee = amountTransfer * feePercentage;
		assertEquals(expectFee, calculator.calculateTransferFee(amountTransfer, balanceComesFrom, balanceGoesIn, isStudent));

		// Error: Code calculates result is not equal as requirement's specifies(0.5%).
		// case 15: input = isNotStudent & amountTransfer = 200 & balanceComesFrom = 3000 & balanceGoesIn = 500
		// output = 0.05 of the amountTransfer
		amountTransfer = 200;
		balanceComesFrom = 3000;
		balanceGoesIn = 500;
		feePercentage = 0.05;
		expectFee = amountTransfer * feePercentage;
		assertEquals(expectFee, calculator.calculateTransferFee(amountTransfer, balanceComesFrom, balanceGoesIn, isStudent));

		// Error: Code calculates result is not equal to requirement's specifies(0.25%).
		// case 16: input = isNotStudent & amountTransfer = 200 & balanceComesFrom = 3000 & balanceGoesIn = 2000
		// output = 0.055 of the amountTransfer
		amountTransfer = 200;
		balanceComesFrom = 3000;
		balanceGoesIn = 2000;
		feePercentage = 0.055;
		expectFee = amountTransfer * feePercentage;
		assertEquals(expectFee, calculator.calculateTransferFee(amountTransfer, balanceComesFrom, balanceGoesIn, isStudent));
	}
}
