# Faza III  - Authentication and Autorization - 1 nedelja

> Preduslov: [Odslušati Udemy Kurs i preći sve primere](http://rdevops.razvoj.saga.co.yu/BackendJavaObukaCollection/_git/SpringSecurity?path=%2FREADME.md&_a=preview)

> JWT: https://www.toptal.com/web/cookie-free-authentication-with-json-web-tokens-an-example-in-laravel-and-angularjs

> SagaShop aplijacija treba da omogući spring security preko JWT tokena.

> Ispratiti [github projekat](https://github.com/Yoh0xFF/java-spring-security-example) kao referencu

- Dodati Role entitet i ManyToMany vezu sa korisnikom. Veza treba da bude unidirektiona.
- Role koje će se koristiti u sistemu su : ADMIN i USER
- Promeniti createUser metodu tako da automatski doda rolu USER novom korisniku. U testovima u AbstractINtegrationTest klasi dodati dodavanje 2 role USER i ADMIN (@BeforeEach anotacija da bi se pre svakog testa izvrsila).
- Napisati metodu koja dodaje jos jednu rolu korisniku. (u testu dodati ADMIN rolu i proveriti da li korisnik ima 2 role, jer USER rolu dobije prilikom kreiranja).
- Uvesti Authentikaciju preko JWT tokena. Napraviti AuthenticationRest service.
- Dodati security anotacije
  - PreAutorize na sve Rest metode.
  - Za sve metode korisnik mora da ima rolu ADMIN, dok za rad sa ShoppingCart-om moze da ima i USER rolu.
  - Za Authentication Rest service ne treba autorizacija da postoji.
  - Svi pregledi treba da imaju mogućnost da im se pristupi i kao ADMIN i kao USER role.
  - Za sve Rest service, izuzev za autentifikaciju, potrebno je da korisnik bude autentifikovan
- Prilagoditi Rest unit testove da rade tako što će se dodati dodati @WithMockUser anotacija iznad svakog testa.
- Napraviti integracioni test za Rest deo da bi se proverio security da li radi. U svakom testu mora prvo da se uradi logovanje korisnika, i zatim u naredim testovima da se JWT token prosleđuje kao header parametar i na taj način pristupi zaštićenom resursu.
- Napravti test kada pokuša neautentifikovani korisnik da pristupi zaštićenom resursu (npr. ubacivanju kategorije)
- Napraviti test kada pokuša autentifikovani korisnik bez odgovarajuće role da pristupi određenom zaštićenom resursu.

> Napomena 1: Kada se desi exception na unit testiranju, Spring "wrappuje" exception u NestedServletException