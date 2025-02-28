# Faza I - CRUD, Osnovni model I deo - 2 nedelje

- Implementirati CRUD operacije za entitete: User, Product, Category, PayPallAccount
  - Napraviti domenski model na osnovu koga će biti kreirana baza podataka (koristiti MySql bazu prethodno instaliranu). Opis modela se nalazi na [linku](http://rdevops.razvoj.saga.co.yu/BackendJavaObukaCollection/SagaShop/_versionControl?path=%24%2FSagaShop&version=T).
  - Potrebno je napraviti DAO sloj i implementirati ga korišćenjem JPA/Hibernate ORM framework-а.
  - Potrebno je napraviti Servis sloj gde će se implementirati biznis logika
  - Potrebno je napraviti Rest sloj koji će preko DTO objekata izložiti podatke
- U application.properties podesiti **spring.jpa.hibernate.ddl-auto = update**. Na taj način, kada se pokrene aplikacija, Hibernate će kreirati šemu i kasnije će raditi alter šeme kada se menja model.
  Za testnu bazu parametar staviti na create-drop (u test folderu kreirati resources folder i u njemu application.properties fajl).
  Obavezno u nekom mysql klijentu (Intellij Idea-ja ima Database tab gde se može podesiti konekcija) pratiti kako izgleda baza, dakle da li je Hibernate napravio tabele i ključeve onako kako se očekuje.
- Za entitete User, Product, Category i PayPalAccount napraviti CRUD operacije (insert, update, delete, findOne, findAll).
  - Za kategoriju i programe napraviti CRUD bez asocijacije (sacuvati kategoriju bez proizvoda i obrnuto)
  - Za Paypall account je neophodno da se sacuva korisnik prvo. Prilikom vracanja korisnika za PayPallAccount koristiti kompozociju (PayPallInfo klasa ima kao atribut UserInfo klasu)
- Za kreiranje testnih podataka koristiti Builder patern uz pomoć [Lombok](https://projectlombok.org/) biblioteke.
```
  public class UserBuilder {

  public static User userAdmin() {
      return User.builder().username("user")
              .password("user").name("user").surname("user")
              .build();
  }

  public static User userNikola(PayPalAccount paypalAccount) {
      return User.builder().username("nikola")
              .password("nikola").name("Nikola").surname("Ninovic")
              .payPalAccount(paypalAccount)
              .build();
  }
}
```
- Napisati unit (rest) i integracione (servis) testove za sve CRUD operacije.
- Item i ShoppingCart se popunjavaju kroz SK Checkout ShoppingCart koji će se implementirati u fazi 2.

- Za svaki od entiteta napraviti DAO interfejs koji nasleđuje AbstractDAO (UserDAO, ProductDAO …), kao i odgovarajuće implementacione klase koje nasleđuju AbstractDAOImpl klasu (UserDAOImpl, ProductDAOImpl ...).
- Za svaki od entiteta napraviti Service i Rest Controller da bi mogao CRUD da se kompletira.
- Za svaki od entiteta napraviti DTO klase koje će se koristiti u komunikaciji između slojeva aplikacije. Primer jednog Service interfejsa sledi u nastavku:
```java
public interface CategoryService {
  Category save(CreateCategoryCmd cmd) throws ServiceException;
  List<CategoryResult> findAll();
  CategoryInfo findById(Long id);
  void update(UpdateCategoryCmd CategoryDTO) throws ServiceException;
  void delete(Long id) throws ServiceException;
}
```
- Za sve create i update operacije, DTO klase imaju sufiks Cmd
- Za sve get objekat operacije, DTO klase imaju sufiks Info
- Za sve get listu objekata operacije, DTO klase imaju sufiks Result
- Napraviti mapere koji će da kopiraju iz Entity u DTO i obrnuto. Koristiti [MapStruct](https://mapstruct.org/).
- Napraviti test koji čuva User-a i PayPalAccount zajedno preko CascadeType.ALL opcije.
- U okviru metode update u UserService omogućiti promenu PayPalAccount-a.

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
