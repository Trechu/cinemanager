# CineManager

## Model bazodanowy
### Schemat
![](./img/Database.png)

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

Poniżej znajduje sie przykład mapowania tabeli `Users`

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

Użytkownik wysyła zapytanie POST o token na `/api/token`, podając w nagłówku Authentication zakodowane za pomocą Base64 `email:haslo`.

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

Aby uzyskać dostęp do zabezpieczonych endpointów, należy dodać do zapytania token w nagłówku Authentication.

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
- wymaganie autentykacji oprócz ścieżek `/api/token` i `/api/register`
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

### POST /api/token

#### Body: 
puste

#### Nagłówki:
Authentication: 'Basic ' + Base64(email + ':' + password)

#### Zwraca:
200 OK - Logowanie powiodło się. W polu danych zwracany jest wygenerowany token.

401 UNAUTHORIZED - Logowanie nie powiodło się z powodu błędnego hasła lub emailu.


### POST /api/register

#### Body: 
firstName, lastName, email, password (plaintext), role (ADMINISTRATOR, MANAGER, EMPLOYEE, CUSTOMER)

#### Nagłówki:
`Opcjonalnie` Authentication: 'Bearer ' + Token

#### Specyfikacja:
Niezalogowana osoba może stworzyć tylko 
użytkownika o roli Customer. Administrator oraz Manager mogą tworzyć użytkowników o roli swojej lub niższej.

#### Zwraca:
201 CREATED - Dodatkowo header Location z URL /api/users/{id} oraz w body id, firstname, lastname, email, role.

403 FORBIDDEN - Nie ma prawa do stworzenia osoby tej rangi.

409 CONFLICT - Jeśli istnieje już użytkownik z takim adresem email. 

### GET /api/users

#### Nagłówki:
Authentication: 'Bearer ' + Token

#### Specyfikacja:
Użytkownik musi być zalogowany. Ponadto jego rola musi być równa co najmniej Managerowi.

#### Zwraca
200 OK - W polu data zwracana jest lista użytkowników zarejestrowanych w systemie.

403 FORBIDDEN - Nie ma prawa do otrzymania listy użytkowników.

### GET /api/users/{id}

#### Nagłówki:
Authentication: 'Bearer ' + Token

#### Specyfikacja:
Użytkownik musi być zalogowany. Użytkownik może podejrzeć sam siebie lub jego ranga musi być równa co najmniej managerowi.

#### Zwraca
200 OK - W polu data zwracane są dane o użytkowniku.

403 FORBIDDEN - Nie ma prawa do otrzymania danych o użytkowniku.

### DELETE /api/users/{id}

#### Nagłówki:
Authentication: 'Bearer ' + Token

#### Specyfikacja:
Użytkownik musi być zalogowany. Ponadto jego rola musi być równa co najmniej Managerowi. Pozwala na usunięcie siebie lub użytkownika co najwyżej tej samej rangi.

#### Zwraca
204 NO CONTENT - Usunięcie powiodło się.

403 FORBIDDEN - Nie można usunąć użytkownika takiej rangi.