package rs.saga.obuka.sagashop.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import rs.saga.obuka.sagashop.domain.Product;
import rs.saga.obuka.sagashop.dto.product.CreateProductCmd;
import rs.saga.obuka.sagashop.dto.product.ProductInfo;
import rs.saga.obuka.sagashop.dto.product.ProductResult;
import rs.saga.obuka.sagashop.dto.product.UpdateProductCmd;
import rs.saga.obuka.sagashop.exception.ServiceException;
import rs.saga.obuka.sagashop.service.ProductService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductRest {

    private final ProductService productService;

    @Autowired
    public ProductRest(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Product save(@RequestBody @Valid CreateProductCmd cmd ) throws ServiceException {
        return productService.save(cmd);
    }

    @GetMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResult> findAll() {
        return productService.findAll();
    }

    @GetMapping("/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ProductInfo findById(@PathVariable Long id ) throws ServiceException {
        return productService.findById(id);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody @Valid UpdateProductCmd cmd) throws ServiceException {
        productService.update(cmd);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws ServiceException {
        productService.delete(id);
    }

    @GetMapping(value = "/name/{name}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResult> findByName(@PathVariable String name) {
        return productService.findByName(name);
    }

    @GetMapping(value = "/price/{price}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResult> findByPrice(@PathVariable Double price) {
        return productService.findByPrice(price);
    }

    @GetMapping(value = "/category/{category}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResult> findByCategory(@PathVariable String categoryName) {
        return productService.findByCategory(categoryName);
    }

}
