package atm.session;


import atm.ATM;
import atm.exceptions.InvalidAmountException;
import atm.session.transactions.ATMWithdrawal;
import atm.dispatcher.MessageDispatcher;
import atm.exceptions.InvalidCredentialsException;
import atm.exceptions.InvalidPinFormatException;
import atm.ui.panels.MainPanel;
import atm.utils.CredentialsCheck;
import atm.utils.FormatChecker;
import bank.db.DBHandler;
import bank.exceptions.CardNotFoundException;
import bank.exceptions.UnsuccessfulBalanceUpdate;
import bank.exceptions.UserNotFoundException;
import bank.transactions.BankTransaction;
import bank.transactions.BankWithdrawal;
import bank.transactions.utils.AccountType;
import bank.transactions.utils.TransactionData;
import bank.transactions.utils.TransactionResult;
import bank.transactions.utils.TransactionType;
import bank.utils.FeesCalculator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class SessionTest {

    private Session session;
    private MainPanel mainPanel;
    private MessageDispatcher dispatcher;
    private ATM atm;
    private TransactionData transactionData;
    private BankWithdrawal bankWithdrawal;
    private DBHandler dbHandler;
    private FeesCalculator feesCalculator;
    private BankTransaction bankTransaction;



    @BeforeEach
    public void setUp() throws CardNotFoundException {
        mainPanel = mock(MainPanel.class);
        dispatcher = mock(MessageDispatcher.class);
        session = new Session(mainPanel, dispatcher);
        FormatChecker formatCheck = new FormatChecker();
        CredentialsCheck credentialsCheck = new CredentialsCheck(dispatcher);
        atm = new ATM(formatCheck, credentialsCheck, dispatcher);

        MainPanel mainPanel = Mockito.mock(MainPanel.class);
        atm.setMainPanel(mainPanel);
        atm.createSession();

    }

    private void mockTransactionData() {
        transactionData = mock(TransactionData.class);
        when(transactionData.getCardNumber()).thenReturn("4000000000000000");
        when(transactionData.getAccounts()).thenReturn(new AccountType[]{AccountType.Chequing, AccountType.Savings, AccountType.TFSA, AccountType.None});
    }

    private void mockBankWithdrawal() throws CardNotFoundException {
        feesCalculator = mock(FeesCalculator.class);
        dbHandler = mock(DBHandler.class);
        when(dbHandler.getCardOwner("4000000000000000")).thenReturn("GROUP11");
        bankTransaction = new BankWithdrawal(feesCalculator, dbHandler);
    }

    @Test
    public void checkCorrectPINTest() {
        char[] expectedPin = new char[]{'1', '2', '3', '4', '5'};
        when(dispatcher.checkCredentials(null, expectedPin)).thenReturn(true);
        Assertions.assertDoesNotThrow(() -> atm.checkPin(expectedPin));
    }


    @Test
    public void checkIncorrectPINCredentialsTest() {
        when(dispatcher.checkCredentials(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(false);
        Assertions.assertThrows(InvalidCredentialsException.class, () -> atm.checkPin(new char[]{'5', '5', '5', '4', '5'}));
    }


    @Test
    public void checkIncorrectPINFormatTest() {
        assertThrows(InvalidPinFormatException.class, () -> atm.checkPin(new char[]{'1', '2', '3', '4'}));
        assertThrows(InvalidPinFormatException.class, () -> atm.checkPin(new char[]{'1', '2', '3', '4', '5', '6'}));

        //  In the ASCII table, which is commonly used to represent characters in computers,
        //  the characters just smaller than '0' (ASCII value 48) are '/', ':' (ASCII value 47),
        //  and the characters just larger than '9' (ASCII value 57) are ':', ';' (ASCII value 58).
        assertThrows(InvalidPinFormatException.class, () -> atm.checkPin(new char[]{'1', '2', '3', '4', '/'}));
        assertThrows(InvalidPinFormatException.class, () -> atm.checkPin(new char[]{'1', '2', '3', '4', ':'}));
    }

    @Test
    // should we test this actual performs or not?
    public void checkValidAmountTest() throws UserNotFoundException, UnsuccessfulBalanceUpdate, CardNotFoundException {
        this.mockTransactionData();
        this.mockBankWithdrawal();
        // Withdraw amount mock by transactionData, and balance mock by dbHandler
        when(transactionData.getAmount()).thenReturn(500.0);
        when(dbHandler.getBalance(anyString(), any())).thenReturn(5000.0);
        when(dbHandler.isStudent(anyString())).thenReturn(true);
        when(feesCalculator.calculateWithdrawalFee(anyDouble(), anyDouble(), anyBoolean(), anyInt())).thenReturn(0.0);
        doNothing().when(dbHandler).setBalance(anyString(),any(),anyDouble());

        // should we use polymorphism or we can just use the bankWithdraw class?
        assertDoesNotThrow(() -> bankTransaction.perform(transactionData));
        double[] accountsBalances = new double[1];
        // 5000 - 500 = 4500, here is to store my
        accountsBalances[0] = 4500.0;
        TransactionResult expectResult = new TransactionResult(true, "", 0, accountsBalances);
        TransactionResult acutalResult = bankTransaction.perform(transactionData);
        assertArrayEquals(acutalResult.getAccountBalances(),expectResult.getAccountBalances());
        assertEquals(acutalResult.getFees(),expectResult.getFees());
        assertEquals(acutalResult.getReason(),expectResult.getReason());
        assertEquals(acutalResult.isSuccessful(),expectResult.isSuccessful());
    }

    @Test
    // i did not find any daily amount set up in the source code
    // so for now I will just test the valid amount as product of 20 or 50
    public void checkInvalidAmountTest(){

        ATMWithdrawal atmWithdrawal = mock(ATMWithdrawal.class);
        session.setTransaction(atmWithdrawal);
        when(atmWithdrawal.getTransactionType()).thenReturn(TransactionType.Withdrawal);
        // case 1: the amount is 19
        assertThrows(InvalidAmountException.class, ()-> session.setAmount(19));

        // case 2: the amount is 21
        assertThrows(InvalidAmountException.class, ()-> session.setAmount(21));

        // case 3: the amount is 49
        assertThrows(InvalidAmountException.class, ()-> session.setAmount(49));

        // case 4: the amount is 51
        assertThrows(InvalidAmountException.class, ()-> session.setAmount(51));

        // case 5: the amount is 999
        assertThrows(InvalidAmountException.class, ()-> session.setAmount(999));

        // case 6: the amount is 1001
        assertThrows(InvalidAmountException.class, ()-> session.setAmount(1001));

        // case 7: the amount is -1
        assertThrows(InvalidAmountException.class, ()-> session.setAmount(-1));

        // case 8: the amount is 0 ?
        // assertDoesNotThrows
        //assertThrows(InvalidAmountException.class, ()-> session.setAmount(0));

        // case 9: the amount is 0
        assertDoesNotThrow(()-> session.setAmount(0));

    }

    @Test
    public void checkvalidAmountTest(){
        ATMWithdrawal atmWithdrawal = mock(ATMWithdrawal.class);
        session.setTransaction(atmWithdrawal);
        when(atmWithdrawal.getTransactionType()).thenReturn(TransactionType.Withdrawal);
        // case 1: the amount is valid as 20
        assertDoesNotThrow(() -> session.setAmount(20));

        // case 1: the amount is valid as 50
        assertDoesNotThrow(() -> session.setAmount(50));

        // case 1: the amount is valid as 100
        assertDoesNotThrow(() -> session.setAmount(100));

    }






}

