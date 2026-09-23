package com.medicare.billing.service;

import com.medicare.billing.entity.Billing;
import com.medicare.billing.repository.BillingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class BillingServiceTest {

    private BillingRepository billingRepository;
    private BillingService billingService;

    @BeforeEach
    void setUp() {
        billingRepository = Mockito.mock(BillingRepository.class);
        billingService = new BillingService(billingRepository);
        Mockito.when(billingRepository.save(Mockito.any(Billing.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void testGenerateBill_cardiologyFee() {
        Billing bill = billingService.generateBill(1L, "Cardiology");
        assertEquals(800.0, bill.getAmount());
        assertEquals("PENDING", bill.getPaymentStatus());
    }

    @Test
    void testGenerateBill_defaultFeeForUnknownSpecialization() {
        Billing bill = billingService.generateBill(2L, "Neurology");
        assertEquals(500.0, bill.getAmount());
    }

    @Test
    void testUpdatePaymentStatus() {
        Billing existing = new Billing(3L, 500.0, "PENDING");
        existing.setBillId(10L);
        Mockito.when(billingRepository.findById(10L)).thenReturn(java.util.Optional.of(existing));

        Billing updated = billingService.updatePaymentStatus(10L, "paid");

        ArgumentCaptor<Billing> captor = ArgumentCaptor.forClass(Billing.class);
        Mockito.verify(billingRepository).save(captor.capture());
        assertEquals("PAID", captor.getValue().getPaymentStatus());
        assertEquals("PAID", updated.getPaymentStatus());
    }
}
