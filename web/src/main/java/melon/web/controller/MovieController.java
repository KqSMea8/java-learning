package melon.web.controller;

import lombok.extern.slf4j.Slf4j;
import melon.web.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class MovieController {
    @Autowired
    MovieService movieService;
    @GetMapping
    public String doFetch(){
        log.info("start fetch...");
        movieService.startFetch();
        return "do fetch";
    }
}
