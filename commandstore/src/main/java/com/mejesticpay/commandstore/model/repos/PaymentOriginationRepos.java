package com.mejesticpay.commandstore.model.repos;

import com.mejesticpay.commandstore.model.PaymentId;
import com.mejesticpay.commandstore.model.PaymentOriginationModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentOriginationRepos extends CrudRepository<PaymentOriginationModel,PaymentId>
{
}
