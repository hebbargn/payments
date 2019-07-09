package com.mejesticpay.store.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel="payments", path="payment")
public interface PaymentRepository extends MongoRepository<PaymentWrapper,String>
{
}
