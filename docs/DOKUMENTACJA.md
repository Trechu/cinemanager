# CineManager

## Model bazodanowy
### Schemat
![](./img/Database2.png)

### Tabele
- `Users` - dane o użytkownikach
  - `first_name` - imię użytkownika
  - `last_name` - nazwisko użytkownika
  - `email` - mail wykorzystywany do logowania
  - `password` - zaszyfrowane hasło
  - `role` - rola użytkownika (`CUSTOMER`, `EMPLOYEE`, `MANAGER`, `ADMINISTRATOR`)

- `Movies` - dane o filmach
  - `title` - tytuł filmu
  - `description` - opis filmu
  - `director` - reżyser filmu
  - `poster_url` - adres url do plakatu filmu
  - `length` - długość filmu w minutach
  - `genre_id` - klucz obcy do tabeli `Genres`, oznaczający gatunek filmu

- `Genres` - dane o gatunkach filmowych
  - `name` - nazwa gatunku

- `Reviews` - dane o recenzjach filmów dodane przez użytkowników
    - `user_id` - klucz obcy do tabeli `Users`, użytkownik wystawiający recenzję
    - `movie_id` - klucz obcy do tabeli `Movies`, oceniany film
    - `rating` - wystawiona ocena
    - `content` - treść recenzji filmu

- `CinemaRooms` - dane o salach kinowych
  - `name` - nazwa sali
  - `rows` - liczba rzędów siedzeń
  - `seats_per_row` - liczba siedzeń w każdym rzędzie

- `Screening` - dane o seansach filmowych
  - `movie_id` - klucz obcy do tabeli `Movies`, jaki film będzie na seansie
  - `room_id` - klucz obcy do tabeli `CinemaRooms`, gdzie odbędzie się seans
  - `start_date` - data seansu filmowego

- `Tickets` - dane o biletach
  - `user_id` - klucz obcy do tabeli `Users`, do kogo należy bilet
  - `screening_id` - klucz obcy do tabeli `Screenings`, na jaki seans
  - `seat_row` - numer rzędu w sali kinowej
  - `seat_position` - numer siedzenia w rzędzie
  - `valid` - ważność biletu, po użyciu `false`

### Relacje

- `Reviews` 
  - Recenzja jest wystawiana przez jednego użytkownika na jeden film

- `Movies`
  - film ma jest przypisany do jednego gatunku

- `Screenings`
  - Seans jest przypisany do jednej sali kinowej i do jednego filmu

- `Tickets`
  - Bilet jest przypisany do jednego użytkownika na jeden seans

### Mapowanie

W projekcie zastosowano Hibernate jako framework do mapowania obiektowo-relacyjnego (`ORM`). Każda tabela w bazie danych została odwzorowana na odpowiednią klasę encji w Javie, co pozwala na wygodne operowanie danymi w kodzie przy użyciu obiektów.

Poniżej znajduje się przykład mapowania tabeli `Users`

```java
@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue
    private long id;

    @NotBlank
    @Column(length = 64)
    private String firstName;
    @NotBlank
    @Column(length = 64)
    private String lastName;
    @Email
    @Column(length = 128, unique = true)
    private String email;
    @NotBlank
    @Column(length = 256)
    private String password;
    @Column(length = 32)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @OneToMany(mappedBy = "user")
    private Set<Review> reviewSet = new HashSet<>();
    @OneToMany(mappedBy = "user")
    private Set<Ticket> ticketSet = new HashSet<>();

    public User() {}

    public User(String firstName, String lastName, String email, String password, UserRole role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // Getters & Setters ...
}
```

## Model obiektowy
Poniżej przedstawiony jest diagram UML modelu obiektowego dla użytkownika.
![](./img/UML.png)

Pozostałe elementy systemu są zbudowane analogicznie - klasa modelowa, repozytorium, serwis oraz kontroler.

![](./img/UML2.png)

## Autentykacja i autoryzacja
### Autentykacja
Dostęp do API jest zabezpieczony za pomocą Spring Security. Autentykacja użytkowników odbywa się za pośrednictwem tokenów JWT. 

Użytkownik wysyła zapytanie POST o token na `/api/token`, podając w nagłówku Authorization zakodowane za pomocą Base64 `email:haslo`.

```
Base64("jan@mail.com:password") -> amFuQG1haWwuY29tOnBhc3N3b3Jk

POST /api/token
Authorization: Basic amFuQG1haWwuY29tOnBhc3N3b3Jk
```

Jeśli podane dane są poprawne, użytkownik otrzymuje token ważny przez godzinę.

```
HTTP/1.1 200 

header.payload.signature
```

Token zawiera algorytm szyfrowania użyty do wygenerowania podpisu, niewrażliwe dane użytkownika, czas wygaśnięcia tokenu oraz podpis wygenerowany za pomocą klucza prywatnego na serwerze.

Przykładowa zawartość tokenu:
```json
// Header
{
  "alg": "RS256"
}

// Payload
{
  "iss": "self",
  "sub": "jan@mail.com",
  "exp": 1733220945,
  "iat": 1733217345,
  "scope": "ROLE_ADMINISTRATOR"
}

// Signature
```

Jeżeli dane są niepoprawne, użytkownik otrzyma odpowiedź 401 Unauthorized.

Aby uzyskać dostęp do zabezpieczonych endpointów, należy dodać do zapytania token w nagłówku Authorization.

Przykładowe zapytanie:
```
GET /api/users
Authorization: Bearer <token>
```

### Autoryzacja
Użytkownicy mogą mieć jedną z 4 ról:

- ADMINISTRATOR - administrator systemu,
- MANAGER - menedżer,
- EMPLOYEE - zwykły pracownik,
- CUSTOMER - klient.

Role są hierarchiczne, więc np. ADMINISTRATOR posiada uprawnienia MANAGER, EMPLOYEE i CUSTOMER.

Dzięki podziałowi na role, różni użytkownicy posiadają różne uprawnienia do danych dostępnych za pomocą endpointów.

### Klasy potrzebne do zabezpieczenia API
#### SecurityConfig
Główna klasa Spring Security, pozwalająca na zabezpieczenie systemu. W metodzie 
`securityFilterChain(HttpSecurity http)` ustawiane są główne ustawienia:
- ustawienia CORS i CSRF,
- stworzenie serwera zasobów OAuth2, do autentykacji przez JWT
- tryb STATELESS.

W metodach `roleHierarchy` oraz `methodSecurityExpressionHandler` ustawiana jest hierarchia ról.

W metodach `jwtDecoder`, `jwtEncoder` oraz klasie `CustomAuthenticatorConverter` tworzymy system tworzenia, kodowania tokenów JWT oraz przetwarzania ról użytkowników.

W metodzie `passwordEncoder` tworzymy używany w aplikacji algorytm szyfrowania haseł.

#### RsaKeyProperties
Rekord umożliwa odczyt kluczy ustawionych w `application.properties`, potrzebnych do tworzenia podpisu tokenu JWT.

Na potrzeby developmentu zostały utworzone przykładowe klucze `private.example.pem` i `public.example.pem`, których **NIE MOŻNA BĘDZIE STOSOTWAĆ W ŚRODOWISKU PRODUKCYJNYM**. W folderze `/resourses/certs` został umieszczony skrypt `generate.sh` umożliwiający wygenerowanie nowych kluczy.

#### JpaUserDetailsService
Aby zapewnić poprawne działanie systemu logowania, zaimplementowany został interfejs `UserDetailsService` - metoda `loadUserByUsername(String email)` korzysta z bazy danych użytkowników, aby odnaleźć i zwrócić użytkownika o podanym mailu lub zwraca błąd, gdy taki użytkownik nie istnieje. Korzystamy w tej metodzie z klasy `SecurityUser`.

#### SecurityUser
Klasa opakowująca `User`a, będącego encją JPA. Implementuje `UserDetails` potrzebny do poprawnego działania systemu logowania i autoryzacji.

#### AuthService
Serwis pozwalający w prosty sposób za pomocą metody `hasRole(userRole, authorities)` sprawdzić, czy użytkownik o danych `authorities`, czyli danej roli, spełnia wymagania `userRole` - ta metoda jest wymagana ze względu na hierarchiczność ról.

## REST API

### Autentykacja
#### POST /api/token

##### Body: 
puste

##### Nagłówki:
Authorization: 'Basic ' + Base64(email + ':' + password)

##### Zwraca:
200 OK - Logowanie powiodło się. W polu danych zwracany jest wygenerowany token.

401 UNAUTHORIZED - Logowanie nie powiodło się z powodu błędnego hasła lub emailu.


#### POST /api/register

##### Body: 
firstName, lastName, email, password (plaintext), role (ADMINISTRATOR, MANAGER, EMPLOYEE, CUSTOMER)

##### Nagłówki:
`Opcjonalnie` Authorization: 'Bearer ' + Token

##### Specyfikacja:
Niezalogowana osoba może stworzyć tylko 
użytkownika o roli Customer. Administrator oraz Manager mogą tworzyć użytkowników o roli swojej lub niższej.

##### Zwraca:
201 CREATED - Dodatkowo header Location z URL /api/users/{id} oraz w body id, firstname, lastname, email, role.

403 FORBIDDEN - Nie ma prawa do stworzenia osoby tej rangi.

409 CONFLICT - Jeśli istnieje już użytkownik z takim adresem email. 

### Użytkownicy
#### GET /api/users

##### Nagłówki:
Authorization: 'Bearer ' + Token

##### Specyfikacja:
Użytkownik musi być zalogowany. Ponadto jego rola musi być równa co najmniej Managerowi.

##### Zwraca:
200 OK - W polu data zwracana jest lista użytkowników zarejestrowanych w systemie.

403 FORBIDDEN - Nie ma prawa do otrzymania listy użytkowników.

#### GET /api/users/{id}

##### Nagłówki:
Authorization: 'Bearer ' + Token

##### Specyfikacja:
Użytkownik musi być zalogowany. Użytkownik może podejrzeć sam siebie lub jego ranga musi być równa co najmniej managerowi.

##### Zwraca:
200 OK - W polu data zwracane są dane o użytkowniku.

403 FORBIDDEN - Nie ma prawa do otrzymania danych o użytkowniku.

#### DELETE /api/users/{id}

##### Nagłówki:
Authorization: 'Bearer ' + Token

##### Specyfikacja:
Użytkownik musi być zalogowany. Ponadto jego rola musi być równa co najmniej Managerowi. Pozwala na usunięcie siebie lub użytkownika co najwyżej tej samej rangi.

##### Zwraca:
204 NO CONTENT - Usunięcie powiodło się.

403 FORBIDDEN - Nie można usunąć użytkownika takiej rangi.

### Gatunki
#### GET /api/genres

##### Specyfikacja:
Pozwala na wylistowanie wszystkich gatunków filmowych, do których należą filmy w bazie kina.

##### Zwraca:
200 OK - Lista gatunków postaci `id, name`.

#### GET /api/genres/{id}

##### Specyfikacja:
Pozwala na wylistowanie gatunku filmowego.

##### Zwraca:
200 OK - Gatunek postaci `id, name`.

#### POST /api/genres

##### Nagłówki:
Authorization: 'Bearer ' + Token

##### Specyfikacja:
Pozwala dodać gatunek filmowy. Należy podać jedynie pole `name`. Wymaga uprawnień co najmniej managera.

##### Zwraca:
201 CREATED - Dodanie gatunku powiodło się. Dodatkowo w odpowiedzi jest header `Location` z URL `/api/genres/{id}` oraz w body znajduje się `id, name`.

400 BAD REQUEST - Nie podano wszystkich wymaganych pól w body lub są one niepoprawne.

401 UNAUTHORIZED - Nagłówek `Authorization` nie został podany w zapytaniu.

403 FORBIDDEN - Brak uprawnień do wykonania akcji.

#### PUT /api/genres/{id}
##### Nagłówki:
Authorization: 'Bearer ' + Token

##### Specyfikacja:
Pozwala uaktualnić gatunek filmowy. W body należy podać jedynie pole `name`. Wymaga uprawnień co najmniej managera.

##### Zwraca:
204 NO CONTENT - Uaktualnienie powiodło się.

400 BAD REQUEST - Nie podano wszystkich wymaganych pól w body lub są one niepoprawne.

401 UNAUTHORIZED - Nagłówek `Authorization` nie został podany w zapytaniu.

403 FORBIDDEN - Brak uprawnień do wykonania akcji.


### Filmy
#### GET /api/movies

##### Specyfikacja:
Pozwala na wylistowanie wszystkich filmów w bazie kina. Wspiera paginację oraz sortowanie (np. `?page=0&size=10&sort=length,desc`).

##### Zwraca:
200 OK - Lista filmów postaci `id, title, descrption, director, posterUrl, length, genre (id, name)`, znajdujące się pod kluczem `content`. 
Dodatkowo dostępne są dane strony w `page` takie jak `number, size, totalElements, totalPages`.

#### GET /api/movies/{id}

##### Specyfikacja:
Pozwala na wylistowanie danych filmu.

##### Zwraca:
200 OK - Film postaci `id, title, descrption, director, posterUrl, length, genre (id, name)`.

#### POST /api/movies

##### Nagłówki:
Authorization: 'Bearer ' + Token

##### Specyfikacja:
Pozwala dodać film. Należy podać pola `title, descrption, director, posterUrl, length, genreId`. Wymaga uprawnień co najmniej managera.

##### Zwraca:
201 CREATED - Dodanie filmu powiodło się. Dodatkowo w odpowiedzi jest header `Location` z URL `/api/movies/{id}` oraz w body znajduje się `id, title, descrption, director, posterUrl, length, genre (id, name)`.

400 BAD REQUEST - Nie podano wszystkich wymaganych pól w body lub są one niepoprawne.

401 UNAUTHORIZED - Nagłówek `Authorization` nie został podany w zapytaniu.

403 FORBIDDEN - Brak uprawnień do wykonania akcji.

#### PUT /api/movies/{id}
##### Nagłówki:
Authorization: 'Bearer ' + Token

##### Specyfikacja:
Pozwala uaktualnić film. W body należy podać pola `title, descrption, director, posterUrl, length, genreId`. Wymaga uprawnień co najmniej managera.

##### Zwraca:
204 NO CONTENT - Uaktualnienie powiodło się.

400 BAD REQUEST - Nie podano wszystkich wymaganych pól w body lub są one niepoprawne.

401 UNAUTHORIZED - Nagłówek `Authorization` nie został podany w zapytaniu.

403 FORBIDDEN - Brak uprawnień do wykonania akcji.

### Sale kinowe
#### GET /api/cinema-rooms
##### Specyfikacja:
Pozwala na wylistowanie wszystkich sal kinowych.

##### Zwraca:
200 OK - Lista gatunków postaci `id, name, rows, seatsPerRow`.

#### GET /api/cinema-rooms/{id}
##### Specyfikacja:
Pozwala na wylistowanie danych konkretnej sali kinowej

##### Zwraca:
200 OK - Dane w postaci `id, name, rows, seatsPerRow`.

400 BAD REQUEST - Podana sala nie istnieje.

#### POST /api/cinema-rooms
##### Nagłówki:
Authorization: 'Bearer ' + Token

##### Specyfikacja:
Pozwala dodać nową salę kinową. Wymaga rangi co najmniej managera. W body należy podać `name, rows, seatsPerRow`.

##### Zwraca:
201 CREATED - Dodanie sali powiodło się. Dodatkowo w odpowiedzi jest header `Location` z URL `/api/cinema-rooms/{id}` oraz w body znajduje się `id, name, rows, seatsPerRow`.

400 BAD REQUEST - Nie podano wszystkich wymaganych pól w body lub są one niepoprawne.

401 UNAUTHORIZED - Nagłówek `Authorization` nie został podany w zapytaniu.

403 FORBIDDEN - Brak uprawnień do wykonania akcji.

#### PUT /api/cinema-rooms/{id}
##### Nagłówki:
Authorization: 'Bearer ' + Token

##### Specyfikacja:
Pozwala uaktualnić salę kinową. W body należy podać pola `name, rows, seatsPerRow`. Wymaga uprawnień co najmniej managera.

##### Zwraca:
204 NO CONTENT - Uaktualnienie powiodło się.

400 BAD REQUEST - Nie podano wszystkich wymaganych pól w body lub są one niepoprawne.

401 UNAUTHORIZED - Nagłówek `Authorization` nie został podany w zapytaniu.

403 FORBIDDEN - Brak uprawnień do wykonania akcji.

### Typy seansów
#### GET /api/screening-types
##### Specyfikacja:
Pozwala na wylistowanie wszystkich rodzajów seansów (np. 2D, 3D).

##### Zwraca:
200 OK - Lista gatunków postaci `id, name, basePrice, discountPrice`.

#### GET /api/screening-types/{id}
##### Specyfikacja:
Pozwala na wylistowanie danych konkretnego typu seansu.

##### Zwraca:
200 OK - Dane w postaci `id, name, basePrice, discountPrice`.

400 BAD REQUEST - Podana sala nie istnieje.

#### POST /api/screening-types
##### Nagłówki:
Authorization: 'Bearer ' + Token

##### Specyfikacja:
Pozwala dodać nowy typ seansu. Wymaga rangi co najmniej managera. W body należy podać `name, basePrice, discountPrice`.

##### Zwraca:
201 CREATED - Dodanie typu seansu powiodło się. Dodatkowo w odpowiedzi jest header `Location` z URL `/api/screening-types/{id}` oraz w body znajduje się `id, name, basePrice, discountPrice`.

400 BAD REQUEST - Nie podano wszystkich wymaganych pól w body lub są one niepoprawne.

401 UNAUTHORIZED - Nagłówek `Authorization` nie został podany w zapytaniu.

403 FORBIDDEN - Brak uprawnień do wykonania akcji.

#### PUT /api/cinema-rooms/{id}
##### Nagłówki:
Authorization: 'Bearer ' + Token

##### Specyfikacja:
Pozwala uaktualnić typ seansu. W body należy podać pola `name, basePrice, discountPrice`. Wymaga uprawnień co najmniej managera.

##### Zwraca:
204 NO CONTENT - Uaktualnienie powiodło się.

400 BAD REQUEST - Nie podano wszystkich wymaganych pól w body lub są one niepoprawne.

401 UNAUTHORIZED - Nagłówek `Authorization` nie został podany w zapytaniu.

403 FORBIDDEN - Brak uprawnień do wykonania akcji.

### Seanse
#### GET /api/screenings

##### Specyfikacja:
Pozwala na wylistowanie wszystkich seansów. Wspiera paginację oraz sortowanie (np. `?page=0&size=10&sort=startDate,desc`). 
Dodatkowo, można filtrować listę, podając odpowiedni(e) parametr(y) w zapytaniu:
- `movieId` - listuje tylko seanse dla danego filmu,
- `after` - listuje tylko seanse odbywające się po (`>=`) danej dacie (przekazanej w formacie ISO-8601, np. `2025-01-01T12:00:00`).

##### Zwraca:
200 OK - Lista seansów postaci `id, startDate, screeningType, movie, cinemaRoom`, znajdujące się pod kluczem `content`. 
Film, typ seansu i sala zwracane są w analogicznej postaci, co endpointy GET tych zasobów dla danego id.
Dodatkowo dostępne są dane strony w `page` takie jak `number, size, totalElements, totalPages`.

400 BAD REQUEST - Podane filtry są niepoprawne.

#### GET /api/screenings/{id}

##### Specyfikacja:
Pozwala na wylistowanie danych seansu.

##### Zwraca:
200 OK - Seans postaci `id, startDate, screeningType, movie, cinemaRoom`.
Film, typ seansu i sala zwracane są w analogicznej postaci, co endpointy GET tych zasobów dla danego id.

#### POST /api/screenings

##### Nagłówki:
Authorization: 'Bearer ' + Token

##### Specyfikacja:
Pozwala dodać seans. Należy podać pola `startDate, screeningTypeId, movieId, cinemaRoomId`. 
Wymaga uprawnień co najmniej managera.
Zostaje sprawdzone, czy dany seans nie pokrywa się z jakimś innym seansem odbywającym się w tym samym czasie w tej samej sali.

##### Zwraca:
201 CREATED - Dodanie seansu powiodło się. Dodatkowo w odpowiedzi jest header `Location` z URL `/api/screenings/{id}` oraz w body znajduje się `id, startDate, screeningType, movie, cinemaRoom`.
Film, typ seansu i sala zwracane są w analogicznej postaci, co endpointy GET tych zasobów dla danego id.

400 BAD REQUEST - Nie podano wszystkich wymaganych pól w body lub są one niepoprawne (w tym gdy istnieje inny seans w tym samym czasie i w tej samej sali).

401 UNAUTHORIZED - Nagłówek `Authorization` nie został podany w zapytaniu.

403 FORBIDDEN - Brak uprawnień do wykonania akcji.

#### PUT /api/screenings/{id}
##### Nagłówki:
Authorization: 'Bearer ' + Token

##### Specyfikacja:
Pozwala uaktualnić seans. W body należy podać pola `startDate, screeningTypeId, movieId, cinemaRoomId`. 
Wymaga uprawnień co najmniej managera.
Zostaje sprawdzone, czy dany seans nie pokrywa się z jakimś innym seansem odbywającym się w tym samym czasie w tej samej sali.

##### Zwraca:
204 NO CONTENT - Uaktualnienie powiodło się.

400 BAD REQUEST - Nie podano wszystkich wymaganych pól w body lub są one niepoprawne (w tym gdy istnieje inny seans w tym samym czasie i w tej samej sali).

401 UNAUTHORIZED - Nagłówek `Authorization` nie został podany w zapytaniu.

403 FORBIDDEN - Brak uprawnień do wykonania akcji.

#### GET /api/screenings/{id}/seats
##### Specyfikacja:
Zwraca listę zajętych siedzeń na dany seans postaci: `row, position`.

##### Zwraca:
200 OK - Lista zajętych siedzeń na dany seans postaci: `row, position`

400 BAD REQUEST - Podany seans nie istnieje.

#### POST /api/orders
##### Specyfikacja:
Wymaga podania tokenu JWT na podstawie którego wystawiane są bilety (wyciąga z niego email)
W body przesyłamy:
 - id seansu
 - listy rzędów siedzeń, ich numerów w rzędach oraz typów

##### Zwraca:
200 OK - Zamówienie oraz przekierowanie na podstronę z odpowiednim zamówieniem

400 BAD REQUEST - w przypadku podania złego użytkownika, złego seansu lub kiedy któreś miejsce zostało już zajęte