package functionreference;

import java.util.List;
import java.util.Locale;
import java.util.function.Function;

class MovieDatabase {
    public static String getDirector(String title) {
        return switch (title) {
            case "inception" -> "Christophar Nolan";
            case "parasite" -> "Boong Joon-ho";
            case "dune" -> "Denis Velleneuve";
            default -> "Unknow Director";
        };
    }
}

public class FunctionReference {
    public static void main(String[] args) {
        Function<String, String> getDirector = MovieDatabase::getDirector;
        System.out.println("Diector: " + getDirector.apply("inception"));

        List<String> movies = List.of("Inception", "Parasite", "Dune");

        List<String> creditLine = movies.stream()
                .map(String::toUpperCase)
                .map(title -> title + " being directed by " + getDirector.apply(title.toLowerCase()))
                .toList();

        creditLine.forEach(System.out::println);
    }
}
