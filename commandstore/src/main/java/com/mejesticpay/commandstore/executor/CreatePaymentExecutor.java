package com.mejesticpay.commandstore.executor;

import com.mejesticpay.commands.Command;
import com.mejesticpay.commands.CreatePaymentCommand;
import com.mejesticpay.commandstore.model.CommandModel;
import com.mejesticpay.commandstore.model.PaymentId;
import com.mejesticpay.commandstore.model.PaymentOriginationModel;
import com.mejesticpay.commandstore.model.PaymentTransactionModel;
import com.mejesticpay.util.JSONHelper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@ToString

public class CreatePaymentExecutor extends BaseExecutor
{

    public CreatePaymentExecutor(CommandStoreContext context)
    {
        super(context);
    }

    public CommandModel execute( Command command)
    {
        try
        {
            CreatePaymentCommand createPaymentCommand = (CreatePaymentCommand)command;
            PaymentId paymentId = new PaymentId(UUID.randomUUID().toString(),1);
            PaymentOriginationModel origination = new PaymentOriginationModel();
            origination.setPaymentReference(paymentId.getPaymentRef());
            origination.setCreatedTime(Instant.now());
            origination.setSource(createPaymentCommand.getSource());
            origination.setJson_genesis(JSONHelper.convertToStringFromObject(createPaymentCommand.getGenesis()));
            context.getPaymentOriginationRepos().save(origination);

            PaymentTransactionModel transaction = new PaymentTransactionModel();
            transaction.setPaymentId(paymentId);
            transaction.setState("Active");
            transaction.setBranch(createPaymentCommand.getBranch());
            transaction.setCreatedTime(Instant.now());

            transaction.setTrack("OutboundPayment");
            transaction.setStation("New");
            transaction.setStatus("Ready");
            context.getPaymentTransactionRepos().save(transaction);

            return transaction;

        }catch (Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(),e);
        }
    }


}
