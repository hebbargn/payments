package com.mejesticpay.mysqlstore.jpa;

import com.mejesticpay.mysqlstore.model.CommonBase;
import com.mejesticpay.mysqlstore.model.PaymentWrapper;
import com.mejesticpay.mysqlstore.model.PaymentWrapper_;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;

import java.io.Serializable;

public class BaseJpaRepositoryImpl <T, ID extends Serializable>
        extends SimpleJpaRepository<T, ID>
{
    private final EntityManager entityManager;

    BaseJpaRepositoryImpl(JpaEntityInformation entityInformation,
                     EntityManager entityManager)
    {
        super(entityInformation, entityManager);

        // Keep the EntityManager around to used from the newly introduced methods.
        this.entityManager = entityManager;
    }

    @Transactional
    public <S extends T> S save(S entity)
    {
        PaymentWrapper paymentWrapper = (PaymentWrapper)entity;
        if(paymentWrapper.getDbAction() == CommonBase.DBACTION.UPDATE)
        {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaUpdate<PaymentWrapper> criteriaUpdate = criteriaBuilder.createCriteriaUpdate(PaymentWrapper.class);

            Root<PaymentWrapper> paymentWrapperRoot = criteriaUpdate.from(PaymentWrapper.class);

            for(String field: paymentWrapper.getUpdatedFields().keySet())
            {
                String fieldValue = (String)paymentWrapper.getUpdatedFields().get(field);
                criteriaUpdate.set(field,fieldValue);
            }
            criteriaUpdate.set(paymentWrapperRoot.<Integer>get(CommonBase.VERSION_COL),
                    criteriaBuilder.sum(paymentWrapperRoot.get(CommonBase.VERSION_COL), 1));
            criteriaUpdate.where(criteriaBuilder.and(
                    criteriaBuilder.equal(paymentWrapperRoot.get(PaymentWrapper_.PAYMENT_REF), paymentWrapper.getPaymentRef()),
                    criteriaBuilder.equal(paymentWrapperRoot.<Integer>get(CommonBase.VERSION_COL), paymentWrapper.getVersion()))
            );
            int rowUpdated = -1;
            try
            {
                Query query = entityManager.createQuery(criteriaUpdate);
                System.out.println(query.unwrap(org.hibernate.Query.class).getQueryString());
                rowUpdated = query.executeUpdate();
                System.out.println("Entities updated: " + rowUpdated);
            }finally
            {
                if(rowUpdated == 1)
                {
                    paymentWrapper.setVersion(paymentWrapper.getVersion() + 1);
                }
                else
                {
                   throw new RuntimeException("Failed to update the record for ID = " +paymentWrapper.getPaymentRef()+ " Version = " +  paymentWrapper.getVersion());

                }
            }

            return entity;
        }
        else
        {
            return super.save(entity);
        }
    }
}
