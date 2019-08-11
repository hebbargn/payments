package com.mejesticpay.rtpgateway.mysql;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("DuplicateCheck")
public interface DuplicateCheckRepository extends CrudRepository<DuplicateCheck,Long>
{

}
