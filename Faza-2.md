# Faza II - CRUD, Osnovni model II deo - 2 nedelje

- Van standardnih CRUD operacija, potrebno je implementirati sledeće slučajeve korišćenja (napisati i propratne testove):
  - Pretraga Product-a po imenu (contains), ceni (equals), količini (less than) i nazivu kategorije (contains), koristeći Criteria API (Java Persistence with Hibernate - Glava 15 - obavezno pročitati dok budete radili).

    ![](../images/kRehCZ.png)
    - Napisati DAO test tako sto će se naslediti AbstractDAOTest i dodati @Transactional anotacija na ProductDAOTest
  - SK Shopping Cart
    - Napraviti metodu save U ShoppingCardService klasi koja ubacuje prazan ShoppingCart u bazu, vezan za User-a i u statusu je NEW. Napraviti odgovarajući test.
    - Napraviti metodu na service-u addItem koja, kada se prosledi Item, dodaje ga u ShoppingCart i čuva u bazi. Status ShoppingCarta prelazi u ACTIVE. Napraviti odgovarajući test.
    - Napraviti metodu na service-u removeItem koja briše Item iz ShoppingCart-a. Napraviti odgovarajući test.
    - Napraviti metodu checkout koja završava kupovinu.
      - proverava se da li je ShoppingCart u statusu Active. Ako nije baca se ServiecException. Napisati test za ovu proveru.
      - proverava se da li ima dovoljno izabranih proizvoda na stanju - ako nema baca se ServiecException. Napisati test za ovu proveru.
      - proverava se da li ima dovoljno sredstava na računu - ako nema baca se BudgetExceededException. Napisati test za ovu proveru.
      - Ukoliko je sve prošlo postavlja se status na COMPLETED, menja se količina proizvoda na stanju, smanjuje se budžet na PayPallAccunt-u.
      > Moze se sve implementirati kroz jedan ili više testova.
    - Napraviti metodu na service-u closeShoppingCart koja prebacuje status ShoppingCart-a u INACTIVE. Napraviti odgovarajući test.

  - Napraviti test koji kreira novu kategoriju zajedno sa produktima, koristeći CascadeType.PERSIST opciju. Nakon toga dodati još jedan proizvod.
  - Napraviti test koji kreira novi proizvod zajedno sa kategorijama, koristeći CascadeType.PERSIST opciju. Nakon toga dodati jos jedan proizvod za postojecu kategoriju. Zatim dodajemo jos jednu kategoriju za proizod.
  - Napraviti integracioni test koji baca BudgetExceededException ukoliko je budžet prekoračen.
  - Napraviti unit rest test koji testira BudgetExceededException - desice se NestedServletException kada se prave unit testovi.

### Literatura
**ManyToMany**
- https://vladmihalcea.com/the-best-way-to-use-the-manytomany-annotation-with-jpa-and-hibernate/
- http://ppgitlab/java-middleware/baza-znanja/blob/master/mkdocs/docs/jpa/ManyToMany.md

**OneToOne**
- https://vladmihalcea.com/the-best-way-to-map-a-onetoone-relationship-with-jpa-and-hibernate/
- http://ppgitlab/java-middleware/baza-znanja/blob/master/mkdocs/docs/jpa/OneToOne.md

**Mappings**
- http://ppgitlab/java-middleware/baza-znanja/blob/master/mkdocs/docs/mappings/MapStruct.md

**Rest exceptions**
- http://ppgitlab/java-middleware/baza-znanja/blob/master/mkdocs/docs/rest/RestExceptionHandling.md

**HashCode and Equals**
- http://ppgitlab/java-middleware/baza-znanja/blob/master/mkdocs/docs/core/HashCodeEquals.md
