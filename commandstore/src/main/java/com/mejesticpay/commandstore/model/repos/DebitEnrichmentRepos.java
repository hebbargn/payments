package com.mejesticpay.commandstore.model.repos;

import com.mejesticpay.commandstore.model.DebitEnrichmentModel;
import com.mejesticpay.commandstore.model.PaymentId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DebitEnrichmentRepos  extends CrudRepository<DebitEnrichmentModel,PaymentId>
{
}
