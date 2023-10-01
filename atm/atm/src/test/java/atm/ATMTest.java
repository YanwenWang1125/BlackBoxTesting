package atm;

import atm.dispatcher.MessageDispatcher;
import atm.exceptions.InvalidCredentialsException;
import atm.exceptions.InvalidPinFormatException;
import atm.ui.panels.MainPanel;
import atm.utils.CredentialsCheck;
import atm.utils.FormatChecker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class ATMTest {
	ATM atm;
	MessageDispatcher dispatcher;
	

	@BeforeEach
	public void setUp() throws Exception {
		dispatcher = Mockito.mock(MessageDispatcher.class);
		FormatChecker formatCheck = new FormatChecker();
		CredentialsCheck credentialsCheck = new CredentialsCheck(dispatcher);
		atm = new ATM(formatCheck, credentialsCheck, dispatcher);
		
		MainPanel mainPanel = Mockito.mock(MainPanel.class);
		atm.setMainPanel(mainPanel);
		atm.createSession();
	}

	@AfterEach
	public void tearDown() throws Exception {
	}

	@Test
	public void checkCorrectPINTest() {
		char[] expectedPin = new char[] {'1','2','3','4','5'};
		Mockito.when(dispatcher.checkCredentials(null, expectedPin)).thenReturn(true);
		Assertions.assertDoesNotThrow(() -> atm.checkPin(expectedPin));
	}


	@Test
	public void checkIncorrectPINTest() {
		Mockito.when(dispatcher.checkCredentials(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(false);
		Assertions.assertThrows(InvalidCredentialsException.class, () -> atm.checkPin(new char[] {'5','5','5','4', '5'}));
	}


	@Test
	public void checkIncorrectPINFormatTest(){
//		Mockito.when(dispatcher.checkCredentials(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(true);
		assertThrows(InvalidPinFormatException.class, () -> atm.checkPin(new char[] {'1', '2', '3', '4'}));
		assertThrows(InvalidPinFormatException.class, () -> atm.checkPin(new char[] {'1', '2', '3', '4','5' ,'6'}));

		//  In the ASCII table, which is commonly used to represent characters in computers,
		//  the characters just smaller than '0' (ASCII value 48) are '/', ':' (ASCII value 47),
		//  and the characters just larger than '9' (ASCII value 57) are ':', ';' (ASCII value 58).
		assertThrows(InvalidPinFormatException.class, () -> atm.checkPin(new char[] {'1', '2', '3', '4', '/'}));
		assertThrows(InvalidPinFormatException.class, () -> atm.checkPin(new char[] {'1', '2', '3', '4', ':'}));
	}

	// Robust Worst Case Boundary Value Analysis for the amount decision
	@Test
	public void checkValidAmountTest(){


	}
}
