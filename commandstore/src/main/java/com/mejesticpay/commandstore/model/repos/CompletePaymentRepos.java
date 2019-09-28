package com.mejesticpay.commandstore.model.repos;

import com.mejesticpay.commandstore.model.CompletePaymentModel;
import com.mejesticpay.commandstore.model.PaymentId;
import org.springframework.data.repository.CrudRepository;

public interface CompletePaymentRepos  extends CrudRepository<CompletePaymentModel,PaymentId> {
}
