package com.mejesticpay.commandstore.model.repos;

import com.mejesticpay.commandstore.model.AuditEntryModel;
import com.mejesticpay.commandstore.model.PaymentId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditEntryRepos extends CrudRepository<AuditEntryModel,PaymentId>
{
}
