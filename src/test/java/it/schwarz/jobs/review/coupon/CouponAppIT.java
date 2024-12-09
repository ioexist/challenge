package it.schwarz.jobs.review.coupon;

import it.schwarz.jobs.review.coupon.api.*;
import it.schwarz.jobs.review.coupon.api.dto.*;
import it.schwarz.jobs.review.coupon.testobjects.TestObjects;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "dev")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CouponAppIT {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private static final String BASE_URL = "http://localhost:";

    @Test
    @Order(1)
    void testGetCouponOverview() {
        GetCouponsResponseDto response = this.restTemplate
                .getForObject(BASE_URL + port + "/api/coupons", GetCouponsResponseDto.class);

        assertThat(response.coupons()).hasSize(3);
    }

    @Test
    @Order(2)
    void testCreateCoupon() {
        CreateCouponRequestDto request = TestObjects.requests().validCoupon();
        CreateCouponResponseDto response = this.restTemplate
                .postForObject(BASE_URL + port + "/api/coupons", request, CreateCouponResponseDto.class);

        assertThat(response.coupon()).isNotNull();
        assertThat(response.coupon().code()).isEqualTo(TestObjects.requests().validCoupon().code());

        GetCouponsResponseDto overviewResponse = this.restTemplate
                .getForObject(BASE_URL + port + "/api/coupons", GetCouponsResponseDto.class);

        assertThat(overviewResponse.coupons()).hasSize(4);
    }


    @Test
    @Order(3)
    void testCreateCouponDuplicate() {
        CreateCouponRequestDto request = TestObjects.requests().validCoupon();
        ResponseEntity<ErrorResponseDto> response = this.restTemplate
                .postForEntity(BASE_URL + port + "/api/coupons", request, ErrorResponseDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getBody().detail()).contains("Coupon already exists: " + request.code());
    }

    @Test
    void testCouponApplicationWithValidData() {
        ApplyCouponRequestDto request = TestObjects.requests().validApplication();
        ResponseEntity<ApplyCouponResponseDto> response = this.restTemplate
                .postForEntity(BASE_URL + port + "/api/coupons/applications", request, ApplyCouponResponseDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().appliedDiscount()).isGreaterThan(BigDecimal.ZERO);
    }

    @Test
    void testCouponApplicationWithInvalidData() {
        ApplyCouponRequestDto request = TestObjects.requests().invalidApplicationOfNotExistingCode();
        ResponseEntity<String> response = this.restTemplate
                .postForEntity(BASE_URL + port + "/api/coupons/applications", request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).contains("not found");
    }

    @Test
    void testGetCouponApplicationsWithValidCoupon() {
        ResponseEntity<GetCouponApplicationsResponseDto> response = this.restTemplate
                .getForEntity(BASE_URL + port + "/api/coupons/TEST_05_50/applications", GetCouponApplicationsResponseDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().couponCode()).isEqualTo("TEST_05_50");
        assertThat(response.getBody().applicationTimestamps()).hasSize(4);
    }

    @Test
    void testGetCouponApplicationsWithInvalidCoupon() {
        String notExistingCouponCode = TestObjects.coupons().NOT_EXISTING_COUPON().getCode();
        ResponseEntity<GetCouponApplicationsResponseDto> response = this.restTemplate
                .getForEntity(BASE_URL + port + "/api/coupons/" + notExistingCouponCode + "/applications", GetCouponApplicationsResponseDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

}


