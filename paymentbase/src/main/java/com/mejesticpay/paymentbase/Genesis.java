package com.mejesticpay.paymentbase;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Getter @Setter @NoArgsConstructor @ToString
public class Genesis
{

    //IDs related to Payment Processing
    /* Instruction Identification - Unique identification, as assigned by an instructing party for an   instructed party, to unambiguously identify the instruction.
     The Instruction Identifier is assigned by the Debtor FI to uniquely identify a single Credit Transfer
    */
    private String instructionId;

    /* End To End Identification-  Unique identification, as assigned by the initiating party(i.e. the person or organisation submitting the payment instruction), to unambiguously identify the transaction. This identification is passed on, unchanged,
    throughout the entire end-to-end chain. The end-to-end identification can be used by the end users for reconciliation or to link tasks relating to the transaction.
    */
    private String endToendId;

    /* Transaction Identification - Unique identification, as assigned by the first instructing agent, to    unambiguously identify the transaction that is passed on, unchanged,
        throughout the entire interbank chain.
    */
    private String transactionId;
    /*
        Clearing System Reference - Unique reference, as assigned by a clearing system, to unambiguously identify the instruction.
     */
    private String ClearingSystemReference;
    /**
     * Payment can be comes with set of instruction like same day payment(SDVA), Real time etc and during payment processing, system need to honor those code.
     */

    private final static String PROCESSING_INSTRUCTION_SERVICE_LEVEL = "PI_SERVICE_LEVEL";


    private Map<String,String> processingInstructions;
    //TODO
    /*
     * Sender Information
      *     <Prtry>CONSUMER, BUSINESS,FABUSINESS,FACONSUMER,INTERMEDIARY,ZELLE</Prtry>
     */

    /*Amount of money moved between the instructing agent and the instructed agent.*/
    private BigDecimal settlementAmount;
    private String settlementCurrency;


    //Ultimate party represent where really money come from and go. But there is no real account and only for regulatory reason will be used.
    private Party ultimateDebtor;
    /* Agent that instructs the next party in the chain to carry out the (set of) instruction(s).
     * Sender Party*/
    private List<Party>instructingParties;
    private Party debtor;
    private Party debtorAgent;

    private Party ultimateCreditor;
    private Party creditor;
    private Party creditorAgent;
    /* List of Parties involved to reach the Creditor Agent.
    /*Agent that is instructed by the previous party in the chain to carry out the (set of) instruction(s)
   Receiver Party
     */
    private List<Party>intermediaries;


    /*
           Specifies which party/parties will bear the charges associated with the processing of the payment transaction.
           EX: "SLEV" - which indicates that a service level agreement determines how charges are to be applied.
    */
    private String chargeBearer;

    private String Remittance;


    /*Provides information related to the handling of the remittance information by any of the agents in the transaction processing chain.*/
    private class Remittance
    {
        /* Unique identification, as assigned by the initiating party, to unambiguously identify the remittance information sent
            separately from the payment instruction, such as a remittance advice. */
        private String Id;
        /*Method used to deliver the remittance advice information. Example - EMAL,URID(url) */
        private String method;
        /* Electronic address to which an agent is to send the remittance information.*/
        private String electornicAddress;

        /*Information supplied to enable the matching of an entry with the items that the transfer is intended to settle, such as
        commercial invoices in an accounts' receivable system. - Either structured or unstructured format;*/
        private String unstructuredInfo;
        private String structuredInfo;
        /*Unique and unambiguous identification of the referred document.*/
        private String structuredInfoId;
        /*Date associated with the referred document */
        private String structuredInfoDate;

    }

    private List<AuditEntry> auditEntries;


}
