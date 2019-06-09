package com.decimalbits.dbservice.resource;

import com.decimalbits.dbservice.model.Quote;
import com.decimalbits.dbservice.model.Quotes;
import com.decimalbits.dbservice.repository.QuotesRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/db")
public class DBServiceResource {

    private QuotesRepository quotesRepository;

    public DBServiceResource(QuotesRepository quotesRepository) {
        this.quotesRepository = quotesRepository;
    }

    @GetMapping("/{username}")
    public List<String> getQuotes(@PathVariable("username") final String username){
        return getQuoteByUsername(username);
    }

    @PostMapping("/delete/{username}")
    public List<String> delete(@PathVariable("username") final String username){
            List<Quote> quotes = quotesRepository.findByUserName(username);
            quotesRepository.deleteAll(quotes);
            return getQuoteByUsername(username);
    }

    @PostMapping("/add")
    public List<String> add(@RequestBody final Quotes quotes){
        quotes.getQuotes()
                .stream()
                .map(quote -> new Quote(quotes.getUsername(),quote))
                .forEach(quotesRepository::save);
        return getQuoteByUsername(quotes.getUsername());
    }

    private List<String> getQuoteByUsername(String username){
        return quotesRepository.findByUserName(username)
                .stream()
                .map(Quote::getQuote)
                .collect(Collectors.toList());
    }


}
