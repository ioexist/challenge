package it.schwarz.jobs.review.coupon.domain.usecase;

import it.schwarz.jobs.review.coupon.domain.entity.ApplicationResult;
import it.schwarz.jobs.review.coupon.domain.entity.Basket;
import it.schwarz.jobs.review.coupon.domain.entity.Coupon;
import it.schwarz.jobs.review.coupon.domain.entity.CouponApplications;

import java.util.List;

public class CouponUseCases {

    private final CouponProvider couponProvider;

    public CouponUseCases(CouponProvider couponProvider) {
        this.couponProvider = couponProvider;
    }

    public Coupon createCoupon(Coupon coupon) {
        try {
            return couponProvider.createCoupon(coupon);
        } catch (IllegalStateException ex) {
            throw new CouponAlreadyExistsException(ex.getMessage());
        }
    }

    public List<Coupon> findAllCoupons() {
        return couponProvider.findAll();
    }

    public CouponApplications getApplications(String couponCode) {
        var foundCouponApplications = couponProvider.getCouponApplications(couponCode);
        if (foundCouponApplications.isEmpty()) {
            throw new CouponCodeNotFoundException("Coupon-Code " + couponCode + " not found.");
        }
        return foundCouponApplications.get();
    }

    public ApplicationResult applyCoupon(Basket basket, String couponCode) {

        var basketValue = basket.getValue();
        var foundCoupon = couponProvider.findById(couponCode);

        // No Coupon found for given Coupon Code
        if (foundCoupon.isEmpty()) {
            throw new CouponCodeNotFoundException("Coupon-Code " + couponCode + " not found.");
        }

        // Basket value must not be less than discount
        var couponToApply = foundCoupon.get();
        if (basketValue.isLessThan(couponToApply.getDiscount())) {
            throw new BasketValueTooLowException(
                    "The basket value (" + basketValue.toBigDecimal() + ") must not be less than the discount (" + couponToApply.getDiscount().toBigDecimal() + ").");
        }

        // Basket value must not be less than Coupon's minimal Basket Value
        if (basketValue.isLessThan(couponToApply.getMinBasketValue())) {
            throw new BasketValueTooLowException(
                    "The basket value (" + basketValue.toBigDecimal() + ") must not be less than the min. allowed basket value (" + couponToApply.getMinBasketValue().toBigDecimal() + ").");
        }

        // Register the usage of this coupon
        couponProvider.registerCouponApplication(couponToApply.getCode());

        // Apply
        return new ApplicationResult(basket, couponToApply);
    }

}
