package streams;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

record NetflixTitle(
        String name,
        String genre,
        double rating,
        boolean alreadyWatched,
        int runTimeInMinutes
) {}

public class stream {
    public static void main(String[] args) {
        List<NetflixTitle> catalog = List.of(
                new NetflixTitle("Inception", "Scifi", 4.9, false, 120),
                new NetflixTitle("Spiderman", "Action", 4.4, true, 180),
                new NetflixTitle("Superman", "Superhero", 4.8, true, 90),
                new NetflixTitle("Avenger", "Action", 4.0, false, 150),
                new NetflixTitle("Titanic", "Romance", 4.5, false, 140)
        );

        Set<String> movieNightGenre = Set.of("Action", "Scifi");

        // Recomment movies based on preference

        List<String> recommendedMovies = catalog.stream()
                .filter((title) -> movieNightGenre.contains(title.genre()))
                .filter((title) -> !title.alreadyWatched())
                .filter((title) -> title.rating() >= 4.5)
                .filter((title) -> title.runTimeInMinutes() <= 150)
                .sorted(Comparator.comparingDouble(NetflixTitle::rating).reversed())
                .map((title) -> title.name() +  " | " + title.rating())
                .limit(2)
                .toList();

        System.out.println(recommendedMovies);
    }
}
