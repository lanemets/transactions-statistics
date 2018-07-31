package com.n26.domain;

import static java.util.concurrent.TimeUnit.SECONDS;

import com.n26.controller.CreateTransactionRequest;
import com.n26.controller.exception.Exceptions.DateExceededException;
import com.n26.controller.exception.Exceptions.IncorrectAmountException;
import com.n26.controller.exception.Exceptions.IncorrectDateException;
import com.n26.controller.exception.Exceptions.OutdatedException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import org.springframework.stereotype.Component;

@Component
public class TransactionConverter {

  private static final long OUTDATED = SECONDS.toMillis(60);

  public Transaction convert(CreateTransactionRequest request, long now) {
    try {
      BigDecimal amount = new BigDecimal(request.getAmount());

      long timestamp = Instant.from(DateTimeFormatter.ISO_INSTANT.parse(request.getTimestamp()))
          .toEpochMilli();

      if (timestamp - now > 0) {
        throw new DateExceededException();
      }

      if (now - timestamp >= OUTDATED) {
        throw new OutdatedException();
      }

      return new Transaction(amount, timestamp);
    } catch (NumberFormatException e) {
      throw new IncorrectAmountException();
    } catch (DateTimeParseException e) {
      throw new IncorrectDateException();
    }
  }
}