package com.n26.service.snapshot;

import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

import com.n26.domain.Transaction;
import com.n26.domain.TransactionStatistics;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.junit.Before;
import org.junit.Test;

public class SnapshotServiceImplTest {

  private SnapshotService snapshotService;

  @Before
  public void setUp() {
    snapshotService = new SnapshotServiceImpl();
  }

  @Test
  public void updateSnapshot() {
    Instant now = Instant.now();
    snapshotService
        .updateSnapshot(
            now.toEpochMilli(),
            new Transaction(
                BigDecimal.ONE,
                now.minus(10, ChronoUnit.SECONDS).toEpochMilli()
            )
        );
    TransactionStatistics statistics = snapshotService.getStatistics();
    assertReflectionEquals(
        new TransactionStatistics(
            BigDecimal.ONE,
            BigDecimal.ONE,
            BigDecimal.ONE,
            BigDecimal.ONE,
            1L
        ),
        statistics
    );
  }

  @Test
  public void updateSnapshotMultipleTransactions() {
    Instant now = Instant.now();
    for (int txnIndex = 0; txnIndex < 10; txnIndex++) {
      snapshotService
          .updateSnapshot(
              now.toEpochMilli(),
              new Transaction(
                  BigDecimal.ONE,
                  now.minus(txnIndex, ChronoUnit.SECONDS).toEpochMilli()
              )
          );
    }

    TransactionStatistics statistics = snapshotService.getStatistics();
    assertReflectionEquals(
        new TransactionStatistics(
            BigDecimal.valueOf(10L),
            BigDecimal.ONE,
            BigDecimal.ONE,
            BigDecimal.ONE,
            10L
        ),
        statistics
    );
  }

  @Test(expected = IllegalStateException.class)
  public void updateSnapshotZeroTimestamp() {
    Instant now = Instant.now();
    snapshotService
        .updateSnapshot(
            now.toEpochMilli(),
            new Transaction(
                BigDecimal.ONE,
                0
            )
        );
    snapshotService.getStatistics();
  }

  @Test
  public void deleteAll() {
    Instant now = Instant.now();
    snapshotService
        .updateSnapshot(
            now.toEpochMilli(),
            new Transaction(
                BigDecimal.ONE,
                now.minus(10, ChronoUnit.SECONDS).toEpochMilli()
            )
        );
    snapshotService.deleteAll();

    TransactionStatistics statistics = snapshotService.getStatistics();
    assertReflectionEquals(TransactionStatistics.EMPTY, statistics);
  }
}