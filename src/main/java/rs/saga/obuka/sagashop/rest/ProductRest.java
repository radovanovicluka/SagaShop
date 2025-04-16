package rs.saga.obuka.sagashop.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductRest {

    private final ProductService productService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Product save(@RequestBody @Valid CreateProductCmd cmd) throws ServiceException {
        return productService.save(cmd);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResult> findAll() {
        return productService.findAll();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ProductInfo findById(@PathVariable Long id) throws ServiceException {
        return productService.findById(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody @Valid UpdateProductCmd cmd) throws ServiceException {
        productService.update(cmd);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws ServiceException {
        productService.delete(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/name/{name}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResult> findByName(@PathVariable String name) {
        return productService.findByName(name);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/price/{price}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResult> findByPrice(@PathVariable Double price) {
        return productService.findByPrice(price);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/category/{category}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResult> findByCategory(@PathVariable String categoryName) {
        return productService.findByCategory(categoryName);
    }

}
