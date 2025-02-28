package rs.saga.obuka.sagashop.dto.paging;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PagingRequest {

    private int currentPage;
    private int rowsPerPage;

}
