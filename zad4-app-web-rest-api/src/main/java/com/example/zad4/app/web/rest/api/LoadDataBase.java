package com.example.zad4.App.Web.REST.API;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDataBase {

    private static final Logger log = LoggerFactory.getLogger(LoadDataBase.class);

    @Bean
    CommandLineRunner initDatabase(MovieRepository repository){
        return args -> {
            log.info("Loading data from database..."+ repository.save(new Movie("Star Wars","George Lucas")));
            log.info("Loading data from database..."+ repository.save(new Movie("Piraci z Skaraibow","Espen Sandberg")));
        };

    }

}
