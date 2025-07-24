package tech.stl.hcm.common.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginationRequestDTO {
    private int page = 0;
    private int size = 20;
    private String sortBy;
    private String sortDirection = "ASC";

    public int getPage() {
        return Math.max(0, page);
    }

    public int getSize() {
        return Math.max(1, Math.min(100, size)); // Limit size between 1 and 100
    }
} 