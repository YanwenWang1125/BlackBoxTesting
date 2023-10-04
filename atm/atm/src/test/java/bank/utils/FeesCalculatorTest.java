package bank.utils;

import static org.junit.jupiter.api.Assertions.*;

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
	public void withdrawalValidTest() {
		assertEquals(0.2, calculator.calculateWithdrawalFee(200, 1000, false, 0));		//pass
		assertEquals(0.01, calculator.calculateWithdrawalFee(200, 1000, false, 0));	//fail
	}

	@Test
	public void withdrawalRobustWorstCaseBoundaryValueTest(){
		// case 1: input = isStudent & weekday(Friday); output = no fee
		double withdrawAmount = 200;
		double amountBalance = 5000;
		boolean isStudent = true;
		int day = Calendar.FRIDAY;
		double feeRate = 0;
		double expectFee = withdrawAmount * feeRate;
		assertEquals(expectFee, calculator.calculateWithdrawalFee(withdrawAmount, amountBalance, isStudent, day));

		// case 1.b: input = isStudent & weekend(Monday); output = is not 0.1% of the withdrawAmount
		double notExpectFeeRate = 0.001;
		day = Calendar.MONDAY;
		double notExpectFee = withdrawAmount * notExpectFeeRate;
		assertNotEquals(notExpectFee, calculator.calculateWithdrawalFee(withdrawAmount, amountBalance, isStudent, day));

		// case 2: input = isStudent & weekend(Saturday); output = 0.1% of the withdrawAmount
		feeRate = 0.001;
		day = Calendar.SATURDAY;
		expectFee = withdrawAmount * feeRate;
		assertEquals(expectFee, calculator.calculateWithdrawalFee(withdrawAmount, amountBalance, isStudent, day));

		// case 2.b: input = isStudent & weekend(Sunday); output = is not no fee
		notExpectFeeRate = 0;
		day = Calendar.SUNDAY;
		notExpectFee = withdrawAmount * notExpectFeeRate;
		assertNotEquals(notExpectFee, calculator.calculateWithdrawalFee(withdrawAmount, amountBalance, isStudent, day));

		// case 3.a: input = isNotStudent & balance = 999, output = 0.3% of the withdrawAmount
		isStudent = false;
		amountBalance = 999;
		feeRate = 0.003;
		expectFee = withdrawAmount * feeRate;
		assertEquals(expectFee, calculator.calculateWithdrawalFee(withdrawAmount, amountBalance, isStudent, day));

		// case 3.b: input = isNotStudent & balance = 1000, output = is not 0.3% of the withdrawAmount
		amountBalance = 1000;
		notExpectFeeRate = 0.003;
		notExpectFee = withdrawAmount * notExpectFeeRate;
		assertNotEquals(notExpectFee, calculator.calculateWithdrawalFee(withdrawAmount, amountBalance, isStudent, day));

		// case 3.c: input = isNotStudent & balance = 1000, output = is not 0.3% of the withdrawAmount
		amountBalance = 1001;
		notExpectFeeRate = 0.003;
		notExpectFee = withdrawAmount * notExpectFeeRate;
		assertNotEquals(notExpectFee, calculator.calculateWithdrawalFee(withdrawAmount, amountBalance, isStudent, day));

		// case 4.a: input = isNotStudent & balance = 999, output = is not 0.1% of the withdrawAmount
		amountBalance = 999;
		notExpectFeeRate = 0.001;
		notExpectFee = withdrawAmount * notExpectFeeRate;
		assertNotEquals(notExpectFee, calculator.calculateWithdrawalFee(withdrawAmount, amountBalance, isStudent, day));

		// case 4.b: input = isNotStudent & balance = 1000, output = is 0.1% of the withdrawAmount
		amountBalance = 1000;
		feeRate = 0.001;
		expectFee = withdrawAmount * feeRate;
		assertEquals(expectFee, calculator.calculateWithdrawalFee(withdrawAmount, amountBalance, isStudent, day));

		// case 4.c: input = isNotStudent & balance = 1001, output = is 0.1% of the withdrawAmount
		amountBalance = 1001;
		feeRate = 0.001;
		expectFee = withdrawAmount * feeRate;
		assertEquals(expectFee, calculator.calculateWithdrawalFee(withdrawAmount, amountBalance, isStudent, day));

		// case 4.d: input = isNotStudent & balance = 4999, output = is 0.1% of the withdrawAmount
		amountBalance = 4999;
		feeRate = 0.001;
		expectFee = withdrawAmount * feeRate;
		assertEquals(expectFee, calculator.calculateWithdrawalFee(withdrawAmount, amountBalance, isStudent, day));

		// case 4.e: input = isNotStudent & balance = 5000, output = is not 0.1% of the withdrawAmount
		amountBalance = 5000;
		notExpectFeeRate = 0.001;
		notExpectFee = withdrawAmount * notExpectFeeRate;
		assertNotEquals(notExpectFee, calculator.calculateWithdrawalFee(withdrawAmount, amountBalance, isStudent, day));

		// case 4.f: input = isNotStudent & balance = 5001, output = is not 0.1% of the withdrawAmount
		amountBalance = 5001;
		notExpectFeeRate = 0.001;
		notExpectFee = withdrawAmount * notExpectFeeRate;
		assertNotEquals(notExpectFee, calculator.calculateWithdrawalFee(withdrawAmount, amountBalance, isStudent, day));

		// case 5.a: input = isNotStudent & balance = 4999, output = is not no fee
		amountBalance = 4999;
		notExpectFeeRate = 0;
		notExpectFee = withdrawAmount * notExpectFeeRate;
		assertNotEquals(notExpectFee, calculator.calculateWithdrawalFee(withdrawAmount, amountBalance, isStudent, day));

		// this case is not mention either in case and in instructions, but we still assume it has no fee requirement based on common logic
		// case 5.b: input = isNotStudent & balance = 5000, output = is no fee
		amountBalance = 5000;
		feeRate = 0;
		expectFee = withdrawAmount * feeRate;
		assertEquals(expectFee, calculator.calculateWithdrawalFee(withdrawAmount, amountBalance, isStudent, day));

		// case 5.c: input = isNotStudent & balance = 5001, output = is no fee
		amountBalance = 5001;
		feeRate = 0;
		expectFee = withdrawAmount * feeRate;
		assertEquals(expectFee, calculator.calculateWithdrawalFee(withdrawAmount, amountBalance, isStudent, day));

		// case 6: logic test
		withdrawAmount = -1;
		amountBalance = 5001;
		feeRate = 0;
		double finalWithdrawAmount = withdrawAmount;
		double finalAmountBalance = amountBalance;
		boolean finalIsStudent = isStudent;
		int finalDay = day;
//		assertThrows(ArithmeticException.class, () -> calculator.calculateWithdrawalFee(finalWithdrawAmount, finalAmountBalance, finalIsStudent, finalDay));
	}

	@Test
	public void depositWeakRobustEquivalenceClassTest(){
		/*
		{student | not student}
		A1 = Amount Deposit [0,50)
		A2 = [50,inf]
		A3 = [0,250)
		A4 = [250, inf]
		B1 = Account Balance [0, 500)
		B2 = [500, inf)
		B3 = [0, 5000)
		B4 = [5000, inf)
		B5 = [0, 2500)
		B6 = [2500, inf)
		B7 = [0, 10000)
		B8 = [10000, inf)
		*/
		boolean isStudent = true;
		double amountDeposit = 0;
		double amountBalance = 0;
		double interestPercentage = 0;
		double expectInterest = interestPercentage * amountDeposit;

		// Error: case is not fit with the instruction
		// case 1: is Student & A1 & B1, output = no interest
		amountDeposit = 25;
		amountBalance = 250;
		interestPercentage = 0;
		expectInterest = interestPercentage * amountDeposit;
		//assertEquals(expectInterest, calculator.calculateDepositInterest(amountDeposit, amountBalance, isStudent));

		// case 2: is Student & A2 & B2, output = 0.5% of the interest
		amountDeposit = 85;
		amountBalance = 850;
		interestPercentage = 0.005;
		expectInterest = interestPercentage * amountDeposit;
		assertEquals(expectInterest, calculator.calculateDepositInterest(amountDeposit, amountBalance, isStudent));

		// case 3: is Not Student & A3 & B3
		isStudent = false;
		amountDeposit = 200;
		amountBalance = 2000;



		// case 4: is Not Student & A4 & B4

		// case 5: is Not Student & A4 & B5

		// case 6: is Not Student & A3 & B6

		// case 7: is Student & A2 & B7

		// case 8: is Student & A1 & B8

	}


	@Test
	public void transferDecisionTableTest(){
		// variable may change
		boolean isStudent = true;
		double amountTransfer = 0;
		double balanceComesFrom = 0;
		double balanceGoesIn = 0;
		double feePercentage = 0;
		double expectFee = amountTransfer * feePercentage;

		// case 1: input = isStudent & amountTransfer = 100 & balanceComesFrom = 1000 & balanceGoesIn = 500
		// output = 0.1% of the amountTransfer
		amountTransfer = 100;
		balanceComesFrom = 1000;
		balanceGoesIn = 500;
		feePercentage = 0.001;
		expectFee = amountTransfer * feePercentage;
		assertEquals(expectFee, calculator.calculateTransferFee(amountTransfer, balanceComesFrom, balanceGoesIn, isStudent));

		// case 2: input = isStudent & amountTransfer = 100 & balanceComesFrom = 1000 & balanceGoesIn = 1500
		// output = 0.05% of the amountTransfer
		amountTransfer = 100;
		balanceComesFrom = 1000;
		balanceGoesIn = 1500;
		feePercentage = 0.0005;
		expectFee = amountTransfer * feePercentage;
		assertEquals(expectFee, calculator.calculateTransferFee(amountTransfer, balanceComesFrom, balanceGoesIn, isStudent));

		// case 3: input = isStudent & amountTransfer = 100 & balanceComesFrom = 3000 & balanceGoesIn = 500
		// output = 0.05% of the amountTransfer
		amountTransfer = 100;
		balanceComesFrom = 3000;
		balanceGoesIn = 500;
		feePercentage = 0.0005;
		expectFee = amountTransfer * feePercentage;
		assertEquals(expectFee, calculator.calculateTransferFee(amountTransfer, balanceComesFrom, balanceGoesIn, isStudent));

		// case 4: input = isStudent & amountTransfer = 100 & balanceComesFrom = 3000 & balanceGoesIn = 1500
		// output = 0.025% of the amountTransfer
		amountTransfer = 100;
		balanceComesFrom = 3000;
		balanceGoesIn = 1500;
		feePercentage = 0.00025;
		expectFee = amountTransfer * feePercentage;
		assertEquals(expectFee, calculator.calculateTransferFee(amountTransfer, balanceComesFrom, balanceGoesIn, isStudent));

		// ERROR: case is not fit with the instruction
		// case 5: input = isStudent & amountTransfer = 300 & balanceComesFrom = 1000 & balanceGoesIn = 500
		// output = 0.05% of the amountTransfer
		amountTransfer = 300;
		balanceComesFrom = 1000;
		balanceGoesIn = 500;
		feePercentage = 0.0005;
		expectFee = amountTransfer * feePercentage;
//		assertEquals(expectFee, calculator.calculateTransferFee(amountTransfer, balanceComesFrom, balanceGoesIn, isStudent));


		// case 6: input = isStudent & amountTransfer = 300 & balanceComesFrom = 1000 & balanceGoesIn = 1500
		// output = 0.025% of the amountTransfer
		amountTransfer = 300;
		balanceComesFrom = 1000;
		balanceGoesIn = 1500;
		feePercentage = 0.00025;
		expectFee = amountTransfer * feePercentage;
//		assertEquals(expectFee, calculator.calculateTransferFee(amountTransfer, balanceComesFrom, balanceGoesIn, isStudent));

		// ERROR: case is not fit with the instruction
		// case 7: input = isStudent & amountTransfer = 300 & balanceComesFrom = 3000 & balanceGoesIn = 500
		// output = 0.25% of the amountTransfer
		amountTransfer = 300;
		balanceComesFrom = 3000;
		balanceGoesIn = 500;
		feePercentage = 0.0025;
		expectFee = amountTransfer * feePercentage;
//		assertEquals(expectFee, calculator.calculateTransferFee(amountTransfer, balanceComesFrom, balanceGoesIn, isStudent));

		// ERROR: case is not fit with the instruction
		// case 8: input = isStudent & amountTransfer = 300 & balanceComesFrom = 3000 & balanceGoesIn = 1500
		// output = 0.125% of the amountTransfer
		amountTransfer = 300;
		balanceComesFrom = 3000;
		balanceGoesIn = 1500;
		feePercentage = 0.00125;
		expectFee = amountTransfer * feePercentage;
		//assertEquals(expectFee, calculator.calculateTransferFee(amountTransfer, balanceComesFrom, balanceGoesIn, isStudent));

		// case 9: input = isNotStudent & amountTransfer = 50 & balanceComesFrom = 3000 & balanceGoesIn = 1000
		// output = 0.2% of the amountTransfer
		isStudent = false;
		amountTransfer = 50;
		balanceComesFrom = 3000;
		balanceGoesIn = 1000;
		feePercentage = 0.002;
		expectFee = amountTransfer * feePercentage;
		assertEquals(expectFee, calculator.calculateTransferFee(amountTransfer, balanceComesFrom, balanceGoesIn, isStudent));

		// Error: case is not fit with the instruction
		// case 10: input = isNotStudent & amountTransfer = 50 & balanceComesFrom = 3000 & balanceGoesIn = 3000
		// output = 0.1% of the amountTransfer
		amountTransfer = 50;
		balanceComesFrom = 3000;
		balanceGoesIn = 3000;
		feePercentage = 0.0001;
		expectFee = amountTransfer * feePercentage;
		//assertEquals(expectFee, calculator.calculateTransferFee(amountTransfer, balanceComesFrom, balanceGoesIn, isStudent));

		// Error: case is not fit with the instruction
		// case 11: input = isNotStudent & amountTransfer = 50 & balanceComesFrom = 5000 & balanceGoesIn = 1000
		// output = 1% of the amountTransfer
		amountTransfer = 50;
		balanceComesFrom = 5000;
		balanceGoesIn = 1000;
		feePercentage = 0.01;
		expectFee = amountTransfer * feePercentage;
		//assertEquals(expectFee, calculator.calculateTransferFee(amountTransfer, balanceComesFrom, balanceGoesIn, isStudent));


		// Error: case is not fit with the instruction
		// case 12: input = isNotStudent & amountTransfer = 50 & balanceComesFrom = 5000 & balanceGoesIn = 3000
		// output = 0.5% of the amountTransfer
		amountTransfer = 50;
		balanceComesFrom = 5000;
		balanceGoesIn = 3000;
		feePercentage = 0.005;
		expectFee = amountTransfer * feePercentage;
		//assertEquals(expectFee, calculator.calculateTransferFee(amountTransfer, balanceComesFrom, balanceGoesIn, isStudent));

		// Error: case is not fit with the instruction
		// case 13: input = isNotStudent & amountTransfer = 200 & balanceComesFrom = 1000 & balanceGoesIn = 500
		// output = 0.2% of the amountTransfer
		amountTransfer = 200;
		balanceComesFrom = 1000;
		balanceGoesIn = 500;
		feePercentage = 0.002;
		expectFee = amountTransfer * feePercentage;
		//assertEquals(expectFee, calculator.calculateTransferFee(amountTransfer, balanceComesFrom, balanceGoesIn, isStudent));

		// Error: case is not fit with the instruction
		// case 14: input = isNotStudent & amountTransfer = 200 & balanceComesFrom = 1000 & balanceGoesIn = 2000
		// output = 0.1% of the amountTransfer
		amountTransfer = 200;
		balanceComesFrom = 1000;
		balanceGoesIn = 2000;
		feePercentage = 0.001;
		expectFee = amountTransfer * feePercentage;
		//assertEquals(expectFee, calculator.calculateTransferFee(amountTransfer, balanceComesFrom, balanceGoesIn, isStudent));

		// Error: case is not fit with the instruction
		// case 15: input = isNotStudent & amountTransfer = 200 & balanceComesFrom = 3000 & balanceGoesIn = 500
		// output = 0.5% of the amountTransfer
		amountTransfer = 200;
		balanceComesFrom = 3000;
		balanceGoesIn = 500;
		feePercentage = 0.005;
		expectFee = amountTransfer * feePercentage;
		//assertEquals(expectFee, calculator.calculateTransferFee(amountTransfer, balanceComesFrom, balanceGoesIn, isStudent));

		// Error: case is not fit with the instruction
		// case 16: input = isNotStudent & amountTransfer = 200 & balanceComesFrom = 3000 & balanceGoesIn = 2000
		// output = 0.25% of the amountTransfer
		amountTransfer = 200;
		balanceComesFrom = 3000;
		balanceGoesIn = 2000;
		feePercentage = 0.0025;
		expectFee = amountTransfer * feePercentage;
		//assertEquals(expectFee, calculator.calculateTransferFee(amountTransfer, balanceComesFrom, balanceGoesIn, isStudent));

	}
}
