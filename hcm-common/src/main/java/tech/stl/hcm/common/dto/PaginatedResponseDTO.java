package tech.stl.hcm.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginatedResponseDTO<T> {
    private List<T> content;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean hasNext;
    private boolean hasPrevious;
    private boolean isFirst;
    private boolean isLast;

    public static <T> PaginatedResponseDTO<T> fromPage(Page<T> page) {
        return new PaginatedResponseDTO<>(
            page.getContent(),
            page.getNumber(),
            page.getSize(),
            page.getTotalElements(),
            page.getTotalPages(),
            page.hasNext(),
            page.hasPrevious(),
            page.isFirst(),
            page.isLast()
        );
    }

    @SuppressWarnings("unchecked")
    public static <T> PaginatedResponseDTO<T> fromMap(Map<String, Object> pageMap, Class<T> contentClass) {
        PaginatedResponseDTO<T> response = new PaginatedResponseDTO<>();
        response.setPageNumber((Integer) pageMap.get("number"));
        response.setPageSize((Integer) pageMap.get("size"));
        response.setTotalElements(((Number) pageMap.get("totalElements")).longValue());
        response.setTotalPages((Integer) pageMap.get("totalPages"));
        response.setHasNext((Boolean) pageMap.get("hasNext"));
        response.setHasPrevious((Boolean) pageMap.get("hasPrevious"));
        response.setFirst((Boolean) pageMap.get("first"));
        response.setLast((Boolean) pageMap.get("last"));
        
        // Handle content list
        List<Map<String, Object>> contentMaps = (List<Map<String, Object>>) pageMap.get("content");
        // Note: In a real implementation, you would need to convert the content maps to DTOs
        // For now, we'll set it as null and handle the conversion in the service layer
        response.setContent(null);
        
        return response;
    }
} 