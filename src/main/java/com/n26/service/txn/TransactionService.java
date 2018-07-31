package com.n26.service.txn;

import com.n26.domain.Transaction;
import com.n26.domain.TransactionStatistics;

public interface TransactionService {

  void addTransaction(Transaction transaction, long now);

  TransactionStatistics getStatistics();

  void deleteAllTransactions(long now);
}
