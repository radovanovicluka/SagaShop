package rs.saga.obuka.sagashop;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import rs.saga.obuka.sagashop.exception.ServiceException;
import rs.saga.obuka.sagashop.service.CategoryService;
import rs.saga.obuka.sagashop.service.PayPalAccountService;
import rs.saga.obuka.sagashop.service.ProductService;
import rs.saga.obuka.sagashop.service.UserService;

/**
 * @author: Ana Dedović
 * Date: 13.07.2021.
 **/
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SagashopApplicationTest.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractIntegrationTest {

  @Autowired
  private CategoryService categoryService;

  @Autowired
  private ProductService productService;

  @Autowired
  private UserService userService;

  @Autowired
  private PayPalAccountService payPalAccountService;

  @AfterEach
  public void tearDown(){
    categoryService.findAll().forEach(e -> {
      try {
        categoryService.delete(e.getId());
      } catch (ServiceException serviceException) {
        serviceException.printStackTrace();
      }
    });
    productService.findAll().forEach(e -> {
      try {
        productService.delete(e.getId());
      }
      catch (ServiceException serviceException) {
        serviceException.printStackTrace();
      }
    });
    userService.findAll().forEach(e -> {
      try {
        userService.delete(e.getId());
      }
      catch (ServiceException serviceException) {
        serviceException.printStackTrace();
      }
    });
    payPalAccountService.findAll().forEach(e -> {
      try {
        payPalAccountService.delete(e.getId());
      }
      catch (ServiceException serviceException) {
        serviceException.printStackTrace();
      }
    });
  }

}
