package com.n26.integration;

import static java.util.concurrent.TimeUnit.MINUTES;
import static org.junit.Assert.assertEquals;

import com.n26.controller.CreateTransactionRequest;
import com.n26.controller.GetStatisticsResult;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SmokeIntegrationTest {

  @Autowired
  private TestRestTemplate restTemplate;

  @SuppressWarnings("ConstantConditions")
  @Test
  public void createClient() throws InterruptedException {

    for (int i = 0; i < 100; i++) {
      Instant now = Instant.now();
      ResponseEntity<Void> responseEntity =
          restTemplate.postForEntity(
              "/transactions",
              new CreateTransactionRequest(
                  BigDecimal.ZERO.toString(),
                  DateTimeFormatter.ISO_INSTANT.format(now)
              ),
              Void.class
          );

      assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    Thread.sleep(MINUTES.toMillis(1));

    ResponseEntity<GetStatisticsResult> responseEntity =
        restTemplate.getForEntity(
            "/statistics",
            GetStatisticsResult.class
        );
    GetStatisticsResult body = responseEntity.getBody();
  }

}