import org.formation.MovieApplication;
import org.formation.model.Movie;
import org.formation.service.MovieService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MovieServiceTest {


    @Test
    public void testDirectedByWithFile() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.getEnvironment().setActiveProfiles("file");
        context.register(MovieApplication.class);
        context.refresh();

        performTest(context);

    }

    @Test
    public void testDirectedByWithJdbc() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.getEnvironment().setActiveProfiles("jdbc");
        context.register(MovieApplication.class);
        context.refresh();
        performTest(context);

    }

    private void performTest(ApplicationContext context) {
        MovieService movieService = (MovieService) context.getBean("movieService");

        List<Movie> hitchcock = movieService.moviesDirectedBy("Hitchcock");
        List<Movie> HITCHCOCK = movieService.moviesDirectedBy("HITCHCOCK");
        assertEquals(hitchcock.size(), 2);
        assertEquals(HITCHCOCK.size(), 2);
        List<Movie> empty = movieService.moviesDirectedBy("");
        assertNotNull(empty);
        assertEquals(0, empty.size());
    }
}
