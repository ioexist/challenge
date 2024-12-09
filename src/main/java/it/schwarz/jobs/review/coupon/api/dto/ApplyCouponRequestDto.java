package it.schwarz.jobs.review.coupon.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ApplyCouponRequestDto(
        @Valid
        @NotNull
        BasketDto basket,

        @NotBlank
        @Size(min = 1, max = 20)
        String couponCode
) {
}
