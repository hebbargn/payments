package com.mejesticpay.paymentbase;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Party
{
    public final static String IBAN_ACCOUNT = "IBAN";
    public final static String BBAN_ACCOUNT = "BBAN";
    public final static String CLABE_ACCOUNT = "CLAB";

    /*Name by which a party is known and which is usually used to identify that party*/
    private String name;
    /*Name of a street or thoroughfare */
    private String street;
    /*Number that identifies the position of a building on a street.*/
    private String buildingNumber;
    /*Identifier consisting of a group of letters and/or numbers that is added to a postal address to assist the sorting of mail.*/
    private String postalCode;
    /*Name of a built-up area, with defined boundaries, and a local government*/
    private String townName;
    /*Nation with its own government.*/
    private String country;
    private Identification identification;

    /*Unambiguous identification of the account of the debtor to which a debit entry will be made as a result of the transaction. */
    private Map<String,String> accounts;

    /*Unique and unambiguous identification of a financial institution, asassigned under an internationally recognized or proprietary identification scheme.
    example are sort codes, BIC etc.
     */
    private Map<String,String> financialInstitutionIds ;

    /**
     * Unique and unambiguous identification of a party.
     */
    public class Identification
    {
        private String dateOfBirth;
        private String cityOfBirth;
        private String countryOfBirth;

        public Identification(String dateOfBirth, String cityOfBirth, String countryOfBirth)
        {
            this.dateOfBirth = dateOfBirth;
            this.cityOfBirth = cityOfBirth;
            this.countryOfBirth = countryOfBirth;
        }
    }

    public void addAccount(String type, String number)
    {
        if(accounts == null)
        {
            accounts = new HashMap<String,String>();
        }
        accounts.put(type,number);
    }

    public void addFinancialInstitutionId(String type, String identifier)
    {
        if(financialInstitutionIds == null)
        {
            financialInstitutionIds = new HashMap<String,String>();
        }
        financialInstitutionIds.put(type,identifier);
    }

    public void setIdentification(String dateOfBirth, String cityOfBirth, String countryOfBirth)
    {
        this.identification = new Identification(dateOfBirth,  cityOfBirth,  countryOfBirth);

    }

}
