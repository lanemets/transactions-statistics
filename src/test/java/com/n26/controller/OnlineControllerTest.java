package com.n26.controller;

import static com.google.common.base.Charsets.UTF_8;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import com.google.common.io.Resources;
import com.n26.Application;
import com.n26.controller.OnlineControllerTest.Configuration;
import com.n26.controller.exception.Exceptions.IncorrectAmountException;
import com.n26.controller.exception.Exceptions.IncorrectDateException;
import com.n26.domain.TransactionConverter;
import com.n26.domain.TransactionStatistics;
import com.n26.service.snapshot.SnapshotService;
import com.n26.service.txn.TransactionService;
import java.math.BigDecimal;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.support.GenericWebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@Import(Configuration.class)
public class OnlineControllerTest {

  @Autowired
  private GenericWebApplicationContext webApplicationContext;

  @Autowired
  private TransactionConverter transactionConverter;

  private MockMvc mockMvc;

  @Before
  public void setUpClass() {
    mockMvc = webAppContextSetup(webApplicationContext).build();
    assertNotNull(mockMvc);
  }

  @Test
  public void createTransaction() throws Exception {
    assertTransactionsResponse("request-create-transaction.json", status().isCreated());
  }

  @Test
  public void createTransactionNotParsableData() throws Exception {
    when(
        transactionConverter
            .convert(
                any(CreateTransactionRequest.class),
                anyLong()
            )
    ).thenThrow(IncorrectAmountException.class);

    assertTransactionsResponse(
        "request-create-transaction-not-parsable-amount.json",
        status().isUnprocessableEntity()
    );

    when(
        transactionConverter
            .convert(
                any(CreateTransactionRequest.class),
                anyLong()
            )
    ).thenThrow(IncorrectDateException.class);

    assertTransactionsResponse(
        "request-create-transaction-not-parsable-date.json",
        status().isUnprocessableEntity()
    );
  }

  @Test
  public void getStatistics() throws Exception {
    mockMvc.perform(
        MockMvcRequestBuilders.get("/statistics")
            .accept(MediaType.APPLICATION_JSON_VALUE)
    ).andExpect(status().isOk())
        .andExpect(content().json(
            Resources
                .toString(
                    Resources.getResource("response-get-statistics.json"),
                    UTF_8
                )
            )
        );
  }

  @Test
  public void deleteAll() throws Exception {
    mockMvc.perform(
        MockMvcRequestBuilders.delete("/transactions")
    ).andExpect(status().isOk());
  }

  @Test
  public void incorrectRequest() throws Exception {
    assertTransactionsResponse("request-unparsable.json", status().isBadRequest());
  }

  private void assertTransactionsResponse(String source, ResultMatcher resultMatcher)
      throws Exception {
    mockMvc.perform(
        MockMvcRequestBuilders.post("/transactions")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .content(Resources
                .toString(
                    Resources.getResource(source),
                    UTF_8
                )
            )
    ).andExpect(resultMatcher);
  }

  @TestConfiguration
  static class Configuration {

    @Bean
    public TransactionService transactionService() {
      TransactionService mock = mock(TransactionService.class);

      when(mock.getStatistics())
          .thenReturn(
              new TransactionStatistics(
                  new BigDecimal("1000.0"),
                  new BigDecimal("100.0"),
                  new BigDecimal("100.0"),
                  new BigDecimal("100.0"),
                  100L
              )
          );

      return mock;
    }

    @Bean
    public TransactionConverter transactionConverter() {
      TransactionConverter mock = mock(TransactionConverter.class);
      return mock;
    }

    @Bean
    public SnapshotService snapshotService() {
      return mock(SnapshotService.class);
    }

  }
}