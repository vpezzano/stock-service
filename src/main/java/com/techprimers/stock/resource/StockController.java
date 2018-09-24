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

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

@RestController
@RequestMapping("/rest/stock")
public class StockController {
	private static final String URL = "http://localhost:8300/rest/db/quotes/";

	@Autowired
	private RestTemplate restTemplate;

	@GetMapping("/{username}")
	public List<Stock> getStock(@PathVariable final String username) throws IOException {
		ResponseEntity<List<String>> responseEntity = restTemplate.exchange(URL + username, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<String>>() {
				});
		List<String> quotes = responseEntity.getBody();
		return quotes.stream().map(this::getStockQuote).collect(Collectors.toList());
	}

	private Stock getStockQuote(String quote) {
		try {
			return YahooFinance.get(quote);
		} catch (IOException e) {
			return new Stock(quote);
		}
	}
}
