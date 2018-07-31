package com.n26.controller;

import com.n26.domain.Transaction;
import com.n26.domain.TransactionConverter;
import com.n26.domain.TransactionStatistics;
import com.n26.service.txn.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OnlineController {

  private static final Logger logger = LoggerFactory.getLogger(OnlineController.class);

  private final TransactionService transactionService;
  private final TransactionConverter transactionConverter;

  @Autowired
  public OnlineController(
      TransactionService transactionService,
      TransactionConverter transactionConverter
  ) {
    this.transactionService = transactionService;
    this.transactionConverter = transactionConverter;
  }

  @RequestMapping(
      value = "/transactions",
      method = RequestMethod.POST,
      consumes = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<?> createTransaction(
      @RequestBody CreateTransactionRequest createTransactionRequest
  ) {
    logger.debug(
        "createTransaction request received; createTransactionRequest: {}",
        createTransactionRequest
    );

    long now = System.currentTimeMillis();

    Transaction transaction = transactionConverter.convert(createTransactionRequest, now);

    transactionService.addTransaction(transaction, now);

    logger.debug("createTransaction operation has been finished;");
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @RequestMapping(
      value = "/statistics",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<GetStatisticsResult> getStatistics() {
    TransactionStatistics transactionStatistics = transactionService.getStatistics();

    return new ResponseEntity<>(
        new GetStatisticsResult(
            transactionStatistics.getSum().toString(),
            transactionStatistics.getAvg().toString(),
            transactionStatistics.getMax().toString(),
            transactionStatistics.getMin().toString(),
            transactionStatistics.getCount()
        ),
        HttpStatus.OK
    );
  }

  @RequestMapping(
      value = "/transactions",
      method = RequestMethod.DELETE
  )
  public ResponseEntity<?> deleteAll() {
    long now = System.currentTimeMillis();

    transactionService.deleteAllTransactions(now);
    return new ResponseEntity<>(HttpStatus.OK);
  }

}