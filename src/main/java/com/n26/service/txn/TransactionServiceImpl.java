package com.n26.service.txn;

import com.n26.domain.Transaction;
import com.n26.domain.TransactionStatistics;
import com.n26.service.snapshot.SnapshotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class TransactionServiceImpl implements TransactionService {

  private static final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

  private final SnapshotService snapshotService;

  @Autowired
  public TransactionServiceImpl(SnapshotService snapshotService) {
    this.snapshotService = snapshotService;
  }

  @Override
  public void addTransaction(Transaction transaction, long now) {
    logger.debug(
        "addTransaction operation has been started; transaction: {}, now: {}",
        transaction,
        now
    );

    snapshotService.updateSnapshot(now, transaction);
  }

  @Override
  public TransactionStatistics getStatistics() {
    logger.debug("getStatistics operation has been started; ");
    return snapshotService.getStatistics();
  }

  @Override
  public void deleteAllTransactions(long now) {
    logger.debug("deleteAllTransactions operation has been started; now: {}", now);
    snapshotService.deleteAll();
  }

}