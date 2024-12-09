package it.schwarz.jobs.review.coupon.testobjects;

public class TestObjects {

    private final static TestCoupons testCoupons = new TestCoupons();
    private final static TestRequests testRequests = new TestRequests();

    private TestObjects() {
    }

    public static TestCoupons coupons() {
        return testCoupons;
    }

    public static TestRequests requests() {
        return testRequests;
    }

}
