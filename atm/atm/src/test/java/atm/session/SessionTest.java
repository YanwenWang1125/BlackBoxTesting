package atm.session;

import atm.dispatcher.MessageDispatcher;
import atm.exceptions.InvalidAmountException;
import atm.exceptions.InvalidPinFormatException;
import atm.session.transactions.ATMTransaction;
import atm.ui.panels.MainPanel;
import bank.transactions.utils.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;


public class SessionTest {

    private Session session;
    private MainPanel mainPanel;
    private MessageDispatcher dispatcher;

    @BeforeEach
    public void setUp() {
        mainPanel = mock(MainPanel.class);
        dispatcher = mock(MessageDispatcher.class);
        session = new Session(mainPanel, dispatcher);
    }

    @Test
    public void testSetAmount() {
        ATMTransaction mockTransaction = Mockito.mock(ATMTransaction.class);
        Mockito.when(mockTransaction.getTransactionType()).thenReturn(TransactionType.Withdrawal);
        session.setTransaction(mockTransaction);


        // Test Case 1: Negative Amount
        assertThrows(InvalidAmountException.class, () -> session.setAmount(-1));

        // Test Case 2: Zero Amount
        assertThrows(InvalidAmountException.class, () -> session.setAmount(0));

        // Test Case 3: Just less than daily limit
        assertDoesNotThrow(() -> session.setAmount(999));

        // Test Case 4: Exact daily limit
        assertDoesNotThrow(() -> session.setAmount(1000));

        // Test Case 5: Just more than daily limit
        assertThrows(InvalidAmountException.class, () -> session.setAmount(1001));

        // Test Case 6: Amount larger than daily limit and available balance
        assertThrows(InvalidAmountException.class, () -> session.setAmount(6000));
    }

    // Assuming a 4-digit numeric PIN is required, and InvalidPinException is thrown when an invalid PIN is provided
    @Test
    public void testAddPin() {
        // Test Case 7: Shorter than required length
        assertThrows(InvalidPinFormatException.class, () -> session.addPin(new char[] {'1', '2', '3'}));

        // Test Case 8: Exact required length
        assertDoesNotThrow(() -> session.addPin(new char[] {'1', '2', '3', '4'}));

        // Test Case 9: Longer than required length
        assertThrows(InvalidPinFormatException.class, () -> session.addPin(new char[] {'1', '2', '3', '4', '5'}));

        // Test Case 10: Non-numeric characters
        assertThrows(InvalidPinFormatException.class, () -> session.addPin(new char[] {'1', '2', 'a', '4'}));
    }
}
