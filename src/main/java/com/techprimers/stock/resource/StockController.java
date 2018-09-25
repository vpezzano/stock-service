package com.techprimers.stock.resource;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.techprimers.stock.model.Quote;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

@RestController
@RequestMapping("/rest/stock")
public class StockController {
	/*
	 * Here db-service is the name we used to register the db-service
	 * microservice to Eureka
	 */
	private static final String URL = "http://db-service/rest/db/quotes/";

	@Autowired
	private RestTemplate restTemplate;

	@GetMapping("/{username}")
	public List<Quote> getStock(@PathVariable final String username) throws IOException {
		ResponseEntity<List<String>> responseEntity = restTemplate.exchange(URL + username, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<String>>() {
				});
		List<String> quotes = responseEntity.getBody();
		return quotes.stream().map(quote -> {
			Stock stock = getStockPrice(quote);
			return new Quote(quote, stock.getQuote().getPrice());
		}).collect(Collectors.toList());
	}

	private Stock getStockPrice(String quote) {
		try {
			return YahooFinance.get(quote);
		} catch (IOException e) {
			e.printStackTrace();
			return new Stock(quote);
		}
	}
}
