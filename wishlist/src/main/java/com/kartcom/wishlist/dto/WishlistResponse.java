package com.kartcom.wishlist.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class WishlistResponse {
    private Long id;
    private Long productId;
    private LocalDateTime addedAt;
}
