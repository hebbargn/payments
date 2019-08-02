import com.mejesticpay.paymentbase.Genesis;
import com.mejesticpay.paymentbase.Party;
import com.mejesticpay.util.JSONHelper;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Random;

public class PaymentTest
{

    @Test
    public void testPayment() throws Exception
    {
        Random random = new Random();
        Genesis origination = new Genesis();

        Party debtor = new Party();
        debtor.setName("John Smith");
        debtor.setAccountNumber("12321321");
        debtor.setAccountType("Checking");
//        debtor.addAccount("US", "12321321");
        origination.setDebtor(debtor);

        Party debtorAgent = new Party();
        debtor.setName("Bank Of America");
//        debtor.addAccount("USABA", "132123");
//        origination.setDebtorAgent(debtorAgent);

        Party creditor = new Party();
        creditor.setName("Sean Pallock");
//        creditor.addAccount("US", "21332132");
//        origination.setCreditor(creditor);

        Party creditorAgent = new Party();
        creditorAgent.setName("Bank Of New York");
//        creditorAgent.addAccount("USABA", "23232");
//        origination.setCreditorAgent(creditorAgent);

        origination.setSettlementAmount(new BigDecimal(random.nextInt(1000)));
        origination.setSettlementCurrency("USD");

        origination.setInstructionId("INSTID-" + random.nextInt(1000));
        origination.setEndToendId("ENDENDID-"+random.nextInt(1000));

        System.out.println(JSONHelper.convertToPrettyStringFromObject(origination));

    }
}
