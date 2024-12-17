package pl.edu.agh.to.cinemanager.configuration;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.edu.agh.to.cinemanager.model.*;
import pl.edu.agh.to.cinemanager.repository.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Configuration
public class DataConfig {
    @Bean
    CommandLineRunner commandLineRunner(UserRepository userRepository, PasswordEncoder passwordEncoder,
                                        MovieRepository movieRepository, GenreRepository genreRepository,
                                        ScreeningTypeRepository screeningTypeRepository,
                                        ScreeningRepository screeningRepository,
                                        CinemaRoomRepository cinemaRoomRepository) {
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

                ScreeningType type2D = new ScreeningType("2D", new BigDecimal("20.00"), new BigDecimal("15.00"));
                ScreeningType type3D = new ScreeningType("3D", new BigDecimal("25.00"), new BigDecimal("20.00"));

                screeningTypeRepository.saveAll(List.of(type2D, type3D));

                CinemaRoom roomShire = new CinemaRoom("Shire", 10, 20);
                CinemaRoom roomMordor = new CinemaRoom("Mordor", 15, 25);
                CinemaRoom roomMatrix = new CinemaRoom("Matrix", 12, 22);
                CinemaRoom roomTitanic = new CinemaRoom("Titanic", 8, 18);
                CinemaRoom roomPandora = new CinemaRoom("Pandora", 14, 30);

                cinemaRoomRepository.saveAll(List.of(roomShire, roomMordor, roomMatrix, roomTitanic, roomPandora));

                List<Screening> screenings = List.of(
                        // Week 1-2 (01.12.2024 - 14.12.2024) - Movies 1, 2, 3, 4, 5
                        new Screening(LocalDateTime.of(2024, 12, 1, 12, 0), movie1, roomShire, type2D),
                        new Screening(LocalDateTime.of(2024, 12, 1, 15, 0), movie2, roomMordor, type3D),
                        new Screening(LocalDateTime.of(2024, 12, 2, 18, 0), movie3, roomMatrix, type2D),
                        new Screening(LocalDateTime.of(2024, 12, 3, 12, 0), movie4, roomTitanic, type2D),
                        new Screening(LocalDateTime.of(2024, 12, 4, 15, 0), movie5, roomPandora, type3D),
                        new Screening(LocalDateTime.of(2024, 12, 5, 15, 0), movie2, roomMordor, type2D),
                        new Screening(LocalDateTime.of(2024, 12, 6, 12, 0), movie1, roomShire, type2D),
                        new Screening(LocalDateTime.of(2024, 12, 6, 18, 0), movie3, roomMatrix, type3D),
                        new Screening(LocalDateTime.of(2024, 12, 6, 21, 0), movie3, roomMatrix, type3D),
                        new Screening(LocalDateTime.of(2024, 12, 7, 15, 0), movie2, roomMordor, type2D),
                        new Screening(LocalDateTime.of(2024, 12, 7, 15, 0), movie4, roomTitanic, type2D),
                        new Screening(LocalDateTime.of(2024, 12, 8, 15, 0), movie5, roomPandora, type3D),
                        new Screening(LocalDateTime.of(2024, 12, 9, 12, 0), movie4, roomTitanic, type2D),
                        new Screening(LocalDateTime.of(2024, 12, 10, 15, 0), movie5, roomPandora, type3D),
                        new Screening(LocalDateTime.of(2024, 12, 10, 12, 0), movie1, roomShire, type2D),
                        new Screening(LocalDateTime.of(2024, 12, 11, 15, 0), movie2, roomMordor, type3D),
                        new Screening(LocalDateTime.of(2024, 12, 11, 15, 0), movie3, roomMatrix, type2D),
                        new Screening(LocalDateTime.of(2024, 12, 12, 12, 0), movie1, roomShire, type2D),
                        new Screening(LocalDateTime.of(2024, 12, 13, 12, 0), movie4, roomTitanic, type2D),
                        new Screening(LocalDateTime.of(2024, 12, 13, 18, 0), movie3, roomMatrix, type3D),
                        new Screening(LocalDateTime.of(2024, 12, 14, 15, 0), movie5, roomPandora, type3D),


                        // Week 3-4 (15.12.2024 - 28.12.2024) - Movies 4, 5, 6, 7, 8
                        new Screening(LocalDateTime.of(2024, 12, 15, 12, 0), movie4, roomMordor, type3D),
                        new Screening(LocalDateTime.of(2024, 12, 15, 15, 0), movie6, roomMatrix, type2D),
                        new Screening(LocalDateTime.of(2024, 12, 16, 18, 0), movie7, roomShire, type3D),
                        new Screening(LocalDateTime.of(2024, 12, 17, 12, 0), movie8, roomTitanic, type2D),
                        new Screening(LocalDateTime.of(2024, 12, 18, 15, 0), movie5, roomPandora, type2D),
                        new Screening(LocalDateTime.of(2024, 12, 20, 12, 0), movie6, roomMordor, type3D),
                        new Screening(LocalDateTime.of(2024, 12, 22, 15, 0), movie7, roomMatrix, type2D),
                        new Screening(LocalDateTime.of(2024, 12, 24, 12, 0), movie4, roomMordor, type3D),
                        new Screening(LocalDateTime.of(2024, 12, 25, 15, 0), movie6, roomMatrix, type2D),
                        new Screening(LocalDateTime.of(2024, 12, 25, 18, 0), movie7, roomShire, type3D),
                        new Screening(LocalDateTime.of(2024, 12, 26, 12, 0), movie8, roomTitanic, type2D),
                        new Screening(LocalDateTime.of(2024, 12, 27, 15, 0), movie5, roomPandora, type2D),
                        new Screening(LocalDateTime.of(2024, 12, 27, 12, 0), movie6, roomMordor, type3D),
                        new Screening(LocalDateTime.of(2024, 12, 28, 15, 0), movie7, roomMatrix, type2D),

                        // Week 5-6 (29.12.2024 - 11.01.2025) - Movies 9, 10, 11, 12, 1
                        new Screening(LocalDateTime.of(2024, 12, 29, 12, 0), movie9, roomShire, type2D),
                        new Screening(LocalDateTime.of(2024, 12, 29, 15, 0), movie10, roomMordor, type3D),
                        new Screening(LocalDateTime.of(2024, 12, 30, 18, 0), movie11, roomMatrix, type2D),
                        new Screening(LocalDateTime.of(2025, 1, 1, 12, 0), movie12, roomTitanic, type3D),
                        new Screening(LocalDateTime.of(2025, 1, 2, 15, 0), movie1, roomPandora, type2D),
                        new Screening(LocalDateTime.of(2025, 1, 3, 18, 0), movie10, roomMordor, type2D),
                        new Screening(LocalDateTime.of(2025, 1, 3, 12, 0), movie9, roomShire, type3D),
                        new Screening(LocalDateTime.of(2025, 1, 4, 12, 0), movie12, roomTitanic, type3D),
                        new Screening(LocalDateTime.of(2025, 1, 4, 15, 0), movie1, roomPandora, type2D),
                        new Screening(LocalDateTime.of(2025, 1, 5, 18, 0), movie10, roomMordor, type2D),
                        new Screening(LocalDateTime.of(2025, 1, 5, 18, 0), movie12, roomPandora, type2D),
                        new Screening(LocalDateTime.of(2025, 1, 5, 18, 0), movie9, roomTitanic, type2D),
                        new Screening(LocalDateTime.of(2025, 1, 6, 12, 0), movie9, roomShire, type3D),
                        new Screening(LocalDateTime.of(2025, 1, 7, 12, 0), movie12, roomTitanic, type3D),
                        new Screening(LocalDateTime.of(2025, 1, 7, 15, 0), movie1, roomPandora, type2D),
                        new Screening(LocalDateTime.of(2025, 1, 8, 18, 0), movie10, roomMordor, type2D),
                        new Screening(LocalDateTime.of(2025, 1, 9, 12, 0), movie9, roomShire, type3D),
                        new Screening(LocalDateTime.of(2025, 1, 10, 15, 0), movie1, roomPandora, type2D),
                        new Screening(LocalDateTime.of(2025, 1, 11, 18, 0), movie10, roomMordor, type2D),
                        new Screening(LocalDateTime.of(2025, 1, 11, 12, 0), movie9, roomShire, type3D),

                        // Week 7-8 (12.01.2025 - 25.01.2025) - Movies 13, 14, 15, 16, 17
                        new Screening(LocalDateTime.of(2025, 1, 12, 12, 0), movie13, roomShire, type2D),
                        new Screening(LocalDateTime.of(2025, 1, 12, 12, 0), movie15, roomMatrix, type2D),
                        new Screening(LocalDateTime.of(2025, 1, 13, 15, 0), movie14, roomMordor, type3D),
                        new Screening(LocalDateTime.of(2025, 1, 13, 18, 0), movie17, roomTitanic, type3D),
                        new Screening(LocalDateTime.of(2025, 1, 13, 15, 0), movie16, roomPandora, type3D),
                        new Screening(LocalDateTime.of(2025, 1, 14, 18, 0), movie15, roomMatrix, type2D),
                        new Screening(LocalDateTime.of(2025, 1, 15, 12, 0), movie16, roomTitanic, type3D),
                        new Screening(LocalDateTime.of(2025, 1, 15, 14, 0), movie13, roomPandora, type3D),
                        new Screening(LocalDateTime.of(2025, 1, 16, 15, 0), movie14, roomTitanic, type2D),
                        new Screening(LocalDateTime.of(2025, 1, 16, 15, 0), movie17, roomPandora, type2D),
                        new Screening(LocalDateTime.of(2025, 1, 17, 12, 0), movie13, roomShire, type2D),
                        new Screening(LocalDateTime.of(2025, 1, 17, 12, 0), movie15, roomMatrix, type2D),
                        new Screening(LocalDateTime.of(2025, 1, 18, 15, 0), movie14, roomMordor, type3D),
                        new Screening(LocalDateTime.of(2025, 1, 18, 18, 0), movie17, roomTitanic, type3D),
                        new Screening(LocalDateTime.of(2025, 1, 18, 15, 0), movie16, roomPandora, type3D),
                        new Screening(LocalDateTime.of(2025, 1, 20, 18, 0), movie15, roomMatrix, type2D),
                        new Screening(LocalDateTime.of(2025, 1, 21, 12, 0), movie16, roomTitanic, type3D),
                        new Screening(LocalDateTime.of(2025, 1, 21, 14, 0), movie13, roomPandora, type3D),
                        new Screening(LocalDateTime.of(2025, 1, 22, 15, 0), movie14, roomTitanic, type2D),
                        new Screening(LocalDateTime.of(2025, 1, 23, 15, 0), movie17, roomPandora, type2D),
                        new Screening(LocalDateTime.of(2025, 1, 24, 15, 0), movie14, roomTitanic, type2D),
                        new Screening(LocalDateTime.of(2025, 1, 24, 15, 0), movie17, roomPandora, type2D),
                        new Screening(LocalDateTime.of(2025, 1, 25, 12, 0), movie13, roomShire, type2D),
                        new Screening(LocalDateTime.of(2025, 1, 25, 12, 0), movie15, roomMatrix, type2D),


                        // Week 9 (26.01.2025 - 01.02.2025) - Movies 18, 19, 20
                        new Screening(LocalDateTime.of(2025, 1, 26, 12, 0), movie18, roomShire, type3D),
                        new Screening(LocalDateTime.of(2025, 1, 26, 12, 0), movie19, roomMatrix, type2D),
                        new Screening(LocalDateTime.of(2025, 1, 26, 15, 0), movie20, roomMordor, type3D),
                        new Screening(LocalDateTime.of(2025, 1, 26, 18, 0), movie18, roomPandora, type2D),
                        new Screening(LocalDateTime.of(2025, 1, 26, 20, 0), movie19, roomTitanic, type3D),
                        new Screening(LocalDateTime.of(2025, 1, 27, 12, 0), movie20, roomShire, type2D),
                        new Screening(LocalDateTime.of(2025, 1, 27, 15, 0), movie19, roomMordor, type2D),
                        new Screening(LocalDateTime.of(2025, 1, 27, 15, 0), movie18, roomTitanic, type3D),
                        new Screening(LocalDateTime.of(2025, 1, 27, 18, 0), movie20, roomMatrix, type2D),
                        new Screening(LocalDateTime.of(2025, 1, 27, 20, 0), movie18, roomPandora, type3D),
                        new Screening(LocalDateTime.of(2025, 1, 28, 12, 0), movie19, roomShire, type2D),
                        new Screening(LocalDateTime.of(2025, 1, 28, 15, 0), movie18, roomMordor, type3D),
                        new Screening(LocalDateTime.of(2025, 1, 28, 15, 0), movie20, roomTitanic, type2D),
                        new Screening(LocalDateTime.of(2025, 1, 28, 18, 0), movie19, roomMatrix, type3D),
                        new Screening(LocalDateTime.of(2025, 1, 28, 20, 0), movie20, roomPandora, type2D),
                        new Screening(LocalDateTime.of(2025, 1, 29, 12, 0), movie18, roomShire, type2D),
                        new Screening(LocalDateTime.of(2025, 1, 29, 15, 0), movie19, roomMordor, type3D),
                        new Screening(LocalDateTime.of(2025, 1, 29, 18, 0), movie20, roomMatrix, type2D),
                        new Screening(LocalDateTime.of(2025, 1, 29, 18, 0), movie18, roomTitanic, type3D),
                        new Screening(LocalDateTime.of(2025, 1, 29, 20, 0), movie19, roomPandora, type2D),
                        new Screening(LocalDateTime.of(2025, 1, 30, 12, 0), movie18, roomTitanic, type2D),
                        new Screening(LocalDateTime.of(2025, 1, 30, 12, 0), movie19, roomShire, type3D),
                        new Screening(LocalDateTime.of(2025, 1, 30, 15, 0), movie20, roomMordor, type2D),
                        new Screening(LocalDateTime.of(2025, 1, 30, 18, 0), movie18, roomMatrix, type3D),
                        new Screening(LocalDateTime.of(2025, 1, 30, 20, 0), movie19, roomPandora, type2D),
                        new Screening(LocalDateTime.of(2025, 1, 31, 12, 0), movie19, roomShire, type3D),
                        new Screening(LocalDateTime.of(2025, 1, 31, 15, 0), movie19, roomPandora, type3D),
                        new Screening(LocalDateTime.of(2025, 1, 31, 18, 0), movie20, roomTitanic, type2D),
                        new Screening(LocalDateTime.of(2025, 1, 31, 20, 0), movie18, roomMatrix, type2D),
                        new Screening(LocalDateTime.of(2025, 2, 1, 12, 0), movie20, roomShire, type3D),
                        new Screening(LocalDateTime.of(2025, 2, 1, 15, 0), movie19, roomMordor, type2D),
                        new Screening(LocalDateTime.of(2025, 2, 1, 18, 0), movie18, roomPandora, type2D),
                        new Screening(LocalDateTime.of(2025, 2, 1, 20, 0), movie20, roomTitanic, type3D)
                );

                screeningRepository.saveAll(screenings);
            }
        };
    }
}
