package it.schwarz.jobs.review.coupon.api.dto;

import it.schwarz.jobs.review.coupon.domain.entity.CouponApplications;

import java.time.Instant;
import java.util.List;

public record GetCouponApplicationsResponseDto(
        String couponCode,
        List<Instant> applicationTimestamps
) {
    public static GetCouponApplicationsResponseDto of(CouponApplications couponApplications) {
        return new GetCouponApplicationsResponseDto(
                couponApplications.getCouponCode(),
                couponApplications.getApplicationTimestamps());
    }
}
