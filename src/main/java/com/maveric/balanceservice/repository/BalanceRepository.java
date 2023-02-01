package com.maveric.balanceservice.repository;

import com.maveric.balanceservice.model.Balance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.aggregation.AggregationExpression;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface BalanceRepository extends MongoRepository<Balance,String> {

    @Query("{accountId:?0}")
    Page<Balance> findAllByAccountId(Pageable pageable, String accountId);

    @Query("{'_id':?0,'accountId':?1}")
    AggregationExpression findByAccountIdAndBalanceId(String balanceId,String accountId);

    Balance findByAccountId(String accountId);
    @Query(value = "{'_id':?0,'accountId':?1}",delete = true)
    AggregationExpression findByAccountIdAndBalanceIdWithDelete(String balanceId,String accountId);
}
