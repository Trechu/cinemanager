package pl.edu.agh.to.cinemanager.configuration;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.edu.agh.to.cinemanager.model.Genre;
import pl.edu.agh.to.cinemanager.model.Movie;
import pl.edu.agh.to.cinemanager.model.User;
import pl.edu.agh.to.cinemanager.model.UserRole;
import pl.edu.agh.to.cinemanager.repository.GenreRepository;
import pl.edu.agh.to.cinemanager.repository.MovieRepository;
import pl.edu.agh.to.cinemanager.repository.UserRepository;

import java.util.List;

@Configuration
public class DataConfig {
    @Bean
    CommandLineRunner commandLineRunner(UserRepository userRepository, PasswordEncoder passwordEncoder, MovieRepository movieRepository, GenreRepository genreRepository) {
        return args -> {
            if (userRepository.count() == 0) {
                User user1 = new User("Jan", "Kowalski", "jan@mail.com",
                        passwordEncoder.encode("password"), UserRole.ADMINISTRATOR);
                User user2 = new User("Kamil", "Slimak", "kamil@snail.com",
                        passwordEncoder.encode("password"), UserRole.CUSTOMER);
                userRepository.save(user1);
                userRepository.save(user2);

                Genre action = new Genre("Akcja");
                Genre drama = new Genre("Dramat");
                Genre comedy = new Genre("Komedia");
                Genre thriller = new Genre("Thriller");
                Genre fantasy = new Genre("Fantasy");
                Genre horror = new Genre("Horror");
                Genre sciFi = new Genre("Science Fiction");
                Genre animation = new Genre("Animacja");
                Genre romance = new Genre("Romans");
                Genre mystery = new Genre("Kryminalne");

                genreRepository.saveAll(List.of(action, drama, comedy, thriller, fantasy,
                        horror, sciFi, animation, romance, mystery));

                Movie movie1 = new Movie(
                        "Władca Pierścieni: Drużyna Pierścienia",
                        "Grupa bohaterów wyrusza w podróż, aby zniszczyć pierścień władzy.",
                        "Peter Jackson",
                        "posters/wladca_pierscieni_druzyna_pierscienia.png",
                        178,
                        fantasy
                );

                Movie movie2 = new Movie(
                        "Skazani na Shawshank",
                        "Młody bankier niesłusznie skazany na dożywocie odnajduje nadzieję za kratami.",
                        "Frank Darabont",
                        "posters/skazani_na_shawshank.png",
                        142,
                        drama
                );

                Movie movie3 = new Movie(
                        "Matrix",
                        "Haker odkrywa prawdę o rzeczywistości i bierze udział w walce przeciwko maszynom.",
                        "Wachowski Sisters",
                        "posters/matrix.png",
                        136,
                        sciFi
                );

                Movie movie4 = new Movie(
                        "Incepcja",
                        "Zespół specjalistów kradnie informacje, włamując się do ludzkich snów.",
                        "Christopher Nolan",
                        "posters/incepcja.png",
                        148,
                        sciFi
                );

                Movie movie5 = new Movie(
                        "Forrest Gump",
                        "Historia życia prostego człowieka, który przypadkowo staje się świadkiem wielkich wydarzeń.",
                        "Robert Zemeckis",
                        "posters/forrest_gump.png",
                        142,
                        drama
                );

                Movie movie6 = new Movie(
                        "Shrek",
                        "Ogr wyrusza na misję, aby uratować księżniczkę, i odkrywa przyjaźń po drodze.",
                        "Andrew Adamson, Vicky Jenson",
                        "posters/shrek.png",
                        90,
                        animation
                );

                Movie movie7 = new Movie(
                        "Pulp Fiction",
                        "Splątane historie kryminalne w Los Angeles.",
                        "Quentin Tarantino",
                        "posters/pulp_fiction.png",
                        154,
                        thriller
                );

                Movie movie8 = new Movie(
                        "Titanic",
                        "Historia miłosna na tle katastrofy RMS Titanic.",
                        "James Cameron",
                        "posters/titanic.png",
                        195,
                        romance
                );

                Movie movie9 = new Movie(
                        "Gladiator",
                        "Były generał rzymski zostaje gladiatorem, aby zemścić się na cesarzu.",
                        "Ridley Scott",
                        "posters/gladiator.png",
                        155,
                        action
                );

                Movie movie10 = new Movie(
                        "Joker",
                        "Historia psychologiczna przemiany Arthura Flecka w legendarnego przestępcę.",
                        "Todd Phillips",
                        "posters/joker.png",
                        122,
                        drama
                );

                Movie movie11 = new Movie(
                        "Interstellar",
                        "Zespół naukowców podróżuje przez czarną dziurę, aby znaleźć nowy dom dla ludzkości.",
                        "Christopher Nolan",
                        "posters/interstellar.png",
                        169,
                        sciFi
                );

                Movie movie12 = new Movie(
                        "Obcy: Ósmy pasażer Nostromo",
                        "Załoga statku kosmicznego walczy z przerażającym obcym stworzeniem.",
                        "Ridley Scott",
                        "posters/obcy_osmy_pasazer_nostromo.png",
                        117,
                        horror
                );

                Movie movie13 = new Movie(
                        "Toy Story",
                        "Zabawki ożywają, gdy ludzie nie patrzą, i przeżywają niesamowite przygody.",
                        "John Lasseter",
                        "posters/toy_story.png",
                        81,
                        animation
                );

                Movie movie14 = new Movie(
                        "Zielona Mila",
                        "Strażnik więzienny zaprzyjaźnia się z więźniem posiadającym nadnaturalne moce.",
                        "Frank Darabont",
                        "posters/zielona_mila.png",
                        189,
                        drama
                );

                Movie movie15 = new Movie(
                        "Mission: Impossible",
                        "Agent Ethan Hunt musi oczyścić swoje imię i powstrzymać spisek.",
                        "Brian De Palma",
                        "posters/mission_impossible.png",
                        110,
                        action
                );

                Movie movie16 = new Movie(
                        "Człowiek ze Stali",
                        "Młody Clark Kent odkrywa swoje moce i staje się Supermanem.",
                        "Zack Snyder",
                        "posters/czlowiek_ze_stali.png",
                        143,
                        sciFi
                );

                Movie movie17 = new Movie(
                        "Piraci z Karaibów: Klątwa Czarnej Perły",
                        "Piracka przygoda z Jackiem Sparrowem i tajemniczą klątwą.",
                        "Gore Verbinski",
                        "posters/piraci_z_karaibow_klatwa_czarnej_perly.png",
                        143,
                        fantasy
                );

                Movie movie18 = new Movie(
                        "Sherlock Holmes",
                        "Słynny detektyw i jego przyjaciel Watson rozwiązują zagadki kryminalne.",
                        "Guy Ritchie",
                        "posters/sherlock_holmes.png",
                        128,
                        mystery
                );

                Movie movie19 = new Movie(
                        "Kraina Lodu",
                        "Księżniczki Anna i Elsa wyruszają w przygodę w świecie pełnym lodu i magii.",
                        "Chris Buck, Jennifer Lee",
                        "posters/kraina_lodu.png",
                        102,
                        animation
                );

                Movie movie20 = new Movie(
                        "Avatar",
                        "Były żołnierz odkrywa magię planety Pandora.",
                        "James Cameron",
                        "posters/avatar.png",
                        162,
                        sciFi
                );

                movieRepository.saveAll(List.of(
                        movie1, movie2, movie3, movie4, movie5,
                        movie6, movie7, movie8, movie9, movie10,
                        movie11, movie12, movie13, movie14, movie15,
                        movie16, movie17, movie18, movie19, movie20
                ));
            }
        };
    }
}
