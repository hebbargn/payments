package com.mejesticpay.commandstore.model.repos;

import com.mejesticpay.commandstore.model.FraudCheckModel;
import com.mejesticpay.commandstore.model.PaymentId;
import org.springframework.data.repository.CrudRepository;

public interface FraudCheckRepos  extends CrudRepository<FraudCheckModel,PaymentId> {
}
