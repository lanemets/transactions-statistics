package com.n26.domain;

import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

import com.n26.controller.CreateTransactionRequest;
import com.n26.controller.exception.Exceptions.DateExceededException;
import com.n26.controller.exception.Exceptions.IncorrectAmountException;
import com.n26.controller.exception.Exceptions.IncorrectDateException;
import com.n26.controller.exception.Exceptions.OutdatedException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import org.junit.Before;
import org.junit.Test;

public class TransactionConverterTest {

  private TransactionConverter transactionConverter;

  @Before
  public void setUp() {
    this.transactionConverter = new TransactionConverter();
  }

  @Test
  public void convert() {
    Instant now = Instant.now();
    CreateTransactionRequest createTransactionRequest = new CreateTransactionRequest(
        "12.00",
        DateTimeFormatter.ISO_INSTANT.format(now)
    );

    Transaction transaction = transactionConverter.convert(
        createTransactionRequest,
        now.plus(10, ChronoUnit.SECONDS).toEpochMilli()
    );

    assertReflectionEquals(
        new Transaction(
            BigDecimal.valueOf(12.00),
            now.toEpochMilli()
        ),
        transaction
    );
  }

  @Test(expected = DateExceededException.class)
  public void convertExceptionallyDateExceeded() {
    Instant now = Instant.now();
    CreateTransactionRequest createTransactionRequest = new CreateTransactionRequest(
        "12.00",
        DateTimeFormatter.ISO_INSTANT.format(now)
    );

    transactionConverter.convert(
        createTransactionRequest,
        now.minus(10, ChronoUnit.SECONDS).toEpochMilli()
    );
  }

  @Test(expected = OutdatedException.class)
  public void convertExceptionallyOutdatedException() {
    Instant now = Instant.now();
    CreateTransactionRequest createTransactionRequest = new CreateTransactionRequest(
        "12.00",
        DateTimeFormatter.ISO_INSTANT.format(now)
    );

    transactionConverter.convert(
        createTransactionRequest,
        now.plus(85, ChronoUnit.SECONDS).toEpochMilli()
    );
  }

  @Test(expected = IncorrectAmountException.class)
  public void convertExceptionallyIncorrectAmountException() {
    Instant now = Instant.now();
    CreateTransactionRequest createTransactionRequest = new CreateTransactionRequest(
        "12.--",
        DateTimeFormatter.ISO_INSTANT.format(now)
    );

    transactionConverter.convert(
        createTransactionRequest,
        now.plus(10, ChronoUnit.SECONDS).toEpochMilli()
    );
  }

  @Test(expected = IncorrectDateException.class)
  public void convertExceptionallyIncorrectDateException() {
    Instant now = Instant.now();
    CreateTransactionRequest createTransactionRequest = new CreateTransactionRequest(
        "12.00",
        "$-90-$"
    );

    transactionConverter.convert(
        createTransactionRequest,
        now.plus(10, ChronoUnit.SECONDS).toEpochMilli()
    );
  }
}