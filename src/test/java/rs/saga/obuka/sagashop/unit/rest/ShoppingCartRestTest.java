package rs.saga.obuka.sagashop.unit.rest;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import rs.saga.obuka.sagashop.AbstractUnitRestTest;
import rs.saga.obuka.sagashop.domain.ShoppingCart;
import rs.saga.obuka.sagashop.dto.item.CreateItemCmd;
import rs.saga.obuka.sagashop.dto.item.ItemInfo;
import rs.saga.obuka.sagashop.dto.shoppingcart.CreateShoppingCartCmd;
import rs.saga.obuka.sagashop.rest.ShoppingCartRest;
import rs.saga.obuka.sagashop.service.ShoppingCartService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("REST")
@WebMvcTest(controllers = ShoppingCartRest.class)
public class ShoppingCartRestTest extends AbstractUnitRestTest {

    @MockBean
    private ShoppingCartService shoppingCartService;

    @Test
    @WithMockUser(username = "user", password = "user", authorities = "USER")
    void saveShoppingCart() throws Exception {
        CreateShoppingCartCmd cmd = new CreateShoppingCartCmd();
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(cmd);
        ShoppingCart cart = new ShoppingCart();
        cart.setName("Ime");

        doReturn(cart).when(shoppingCartService).save(any(CreateShoppingCartCmd.class));

        mockMvc.perform(post("/shoppingcart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Ime"));

    }

    @Test
    @WithMockUser(username = "user", password = "user", authorities = "USER")
    void addItem() throws Exception {
        CreateItemCmd cmd = new CreateItemCmd();
        ItemInfo info = new ItemInfo();
        info.setName("Ime");
        info.setQuantity(5);

        doReturn(info).when(shoppingCartService).addItem(any(CreateItemCmd.class));

        mockMvc.perform(put("/shoppingcart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(cmd))).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Ime"))
                .andExpect(jsonPath("$.quantity").value(5));

    }

    @Test
    @WithMockUser(username = "user", password = "user", authorities = "USER")
    void removeItem() throws Exception {

        doNothing().when(shoppingCartService).removeItem(anyLong());

        mockMvc.perform(delete("/shoppingcart/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "user", password = "user", authorities = "USER")
    void checkout() throws Exception {

        doNothing().when(shoppingCartService).checkout(anyLong());

        mockMvc.perform(get("/shoppingcart/1"))
                .andExpect(status().isNoContent());

    }

    @Test
    @WithMockUser(username = "user", password = "user", authorities = "USER")
    void close() throws Exception {

        doNothing().when(shoppingCartService).removeItem(anyLong());

        mockMvc.perform(patch("/shoppingcart/1"))
                .andExpect(status().isNoContent());
    }

}
