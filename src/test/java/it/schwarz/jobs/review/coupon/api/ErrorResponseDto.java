package it.schwarz.jobs.review.coupon.api;

public record ErrorResponseDto(
        String type,
        String title,
        int status,
        String detail,
        String instance
) {
}
