package it.schwarz.jobs.review.coupon.testobjects;

import it.schwarz.jobs.review.coupon.domain.entity.AmountOfMoney;
import it.schwarz.jobs.review.coupon.domain.entity.Coupon;

public class TestCoupons {

    public Coupon COUPON_12_20() {
        return new Coupon("CODE_12_20", AmountOfMoney.of("12.00"), AmountOfMoney.of("20.00"), "12 for 20");
    }

    public Coupon NOT_EXISTING_COUPON() {
        return new Coupon("NON_EXISTING", AmountOfMoney.of("0.00"), AmountOfMoney.of("0.00"), "NotExisting");
    }

}
