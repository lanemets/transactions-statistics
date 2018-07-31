package com.n26.service.snapshot;

import com.n26.domain.Transaction;
import com.n26.domain.TransactionStatistics;

public interface SnapshotService {

  void updateSnapshot(long now, Transaction source);

  TransactionStatistics getStatistics();

  void deleteAll();
}