package com.n26.service.snapshot;

import static java.util.concurrent.TimeUnit.MINUTES;

import com.n26.domain.Snapshot;
import com.n26.domain.Transaction;
import com.n26.domain.TransactionStatistics;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import org.springframework.stereotype.Service;

@Service
class SnapshotServiceImpl implements SnapshotService {

  private static final int NEW_SCALE = 2;
  private static final RoundingMode HALF_UP = RoundingMode.HALF_UP;

  private static final int INITIAL_CAPACITY = 60;
  private static final long ONE_MINUTE = MINUTES.toMillis(1);

  private final ConcurrentHashMap<Integer, Snapshot> snapshotsPerLastMinute =
      new ConcurrentHashMap<>(INITIAL_CAPACITY);

  @Override
  public void updateSnapshot(long now, Transaction transaction) {
    int currentSecond = LocalDateTime
        .ofInstant(Instant.ofEpochMilli(transaction.getTimestamp()), ZoneId.systemDefault())
        .getSecond();

    snapshotsPerLastMinute.compute(
        currentSecond, (second, snapshot) -> {
          if (snapshot == null || isStale(now, transaction)) {
            BigDecimal amount = transaction.getAmount();
            return new Snapshot(amount, amount, amount, 1L, transaction.getTimestamp());
          }

          return new Snapshot(
              snapshot.getSum().add(transaction.getAmount()),
              snapshot.getMax().compareTo(transaction.getAmount()) < 0
                  ? transaction.getAmount()
                  : snapshot.getMax(),
              snapshot.getMin().compareTo(transaction.getAmount()) > 0
                  ? transaction.getAmount()
                  : snapshot.getMin(),
              snapshot.getCount() + 1L,
              transaction.getTimestamp()
          );
        }
    );
  }

  @Override
  public TransactionStatistics getStatistics() {
    Predicate<Snapshot> snapshotPredicate = snapshot ->
        (System.currentTimeMillis() - snapshot.getTimestamp()) < ONE_MINUTE;
    Collection<Snapshot> values = snapshotsPerLastMinute.values();

    if (values.isEmpty()) {
      return TransactionStatistics.EMPTY;
    }

    Long count = count(snapshotPredicate, values);

    BigDecimal min = scaled(min(snapshotPredicate, values));
    BigDecimal max = scaled(max(snapshotPredicate, values));
    BigDecimal sum = scaled(sum(snapshotPredicate, values));
    BigDecimal avg = scaled(sum.divide(BigDecimal.valueOf(count), RoundingMode.HALF_UP));

    return new TransactionStatistics(sum, avg, max, min, count);
  }

  @Override
  public void deleteAll() {
    snapshotsPerLastMinute.clear();
  }

  private boolean isStale(long now, Transaction transaction) {
    return now - transaction.getTimestamp() >= ONE_MINUTE;
  }

  private static Long count(Predicate<Snapshot> snapshotPredicate, Collection<Snapshot> values) {
    return values.stream()
        .filter(snapshotPredicate)
        .map(Snapshot::getCount)
        .reduce((left, right) -> left + right)
        .orElseThrow(IllegalStateException::new);
  }

  private static BigDecimal min(Predicate<Snapshot> snapshotPredicate, Collection<Snapshot> values) {
    return values.stream()
        .filter(snapshotPredicate)
        .map(Snapshot::getMin)
        .min(BigDecimal::compareTo)
        .orElseThrow(IllegalStateException::new);
  }

  private static BigDecimal max(Predicate<Snapshot> snapshotPredicate, Collection<Snapshot> values) {
    return values.stream()
        .filter(snapshotPredicate)
        .map(Snapshot::getMax)
        .max(BigDecimal::compareTo)
        .orElseThrow(IllegalStateException::new);
  }

  private static BigDecimal sum(Predicate<Snapshot> snapshotPredicate, Collection<Snapshot> values) {
    return values.stream()
        .filter(snapshotPredicate)
        .map(Snapshot::getSum)
        .reduce(BigDecimal::add)
        .orElseThrow(IllegalStateException::new);
  }

  private static BigDecimal scaled(BigDecimal value) {
    return value.setScale(NEW_SCALE, HALF_UP);
  }
}