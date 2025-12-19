package com.converter;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PersianDateConverterTest {

    // -------------------------
    // Gregorian -> Persian
    // -------------------------

    @Test
    void testGregorianToPersian_NormalDate() {
        LocalDate g = LocalDate.of(2025, 10, 18);
        PersianDateConverter.PersianDate p =
                PersianDateConverter.toPersian(g);

        assertEquals(1404, p.year);
        assertEquals(7, p.month);
        assertEquals(26, p.day);
    }

    @Test
    void testGregorianToPersian_EndOfYear() {
        LocalDate g = LocalDate.of(2021, 3, 20);
        PersianDateConverter.PersianDate p =
                PersianDateConverter.toPersian(g);

        assertEquals(1399, p.year);
        assertEquals(12, p.month);
        assertEquals(30, p.day);
    }

    @Test
    void testGregorianToPersian_StartOfYear() {
        LocalDate g = LocalDate.of(2021, 3, 21);
        PersianDateConverter.PersianDate p =
                PersianDateConverter.toPersian(g);

        assertEquals(1400, p.year);
        assertEquals(1, p.month);
        assertEquals(1, p.day);
    }

    // -------------------------
    // Persian -> Gregorian
    // -------------------------

    @Test
    void testPersianToGregorian_NormalDate() {
        LocalDate g =
                PersianDateConverter.toGregorian(1404, 7, 26);

        assertEquals(LocalDate.of(2025, 10, 18), g);
    }

    @Test
    void testPersianToGregorian_NewYear() {
        LocalDate g =
                PersianDateConverter.toGregorian(1400, 1, 1);

        assertEquals(LocalDate.of(2021, 3, 21), g);
    }

    @Test
    void testPersianToGregorian_EndOfYearLeap() {
        LocalDate g =
                PersianDateConverter.toGregorian(1399, 12, 30);

        assertEquals(LocalDate.of(2021, 3, 20), g);
    }

    // -------------------------
    // Round-trip consistency
    // -------------------------

    @Test
    void testRoundTrip_GregorianToPersianToGregorian() {
        LocalDate original = LocalDate.of(1999, 2, 11);

        PersianDateConverter.PersianDate p =
                PersianDateConverter.toPersian(original);

        LocalDate back =
                PersianDateConverter.toGregorian(p.year, p.month, p.day);

        assertEquals(original, back);
    }

    @Test
    void testRoundTrip_PersianToGregorianToPersian() {
        PersianDateConverter.PersianDate original =
                new PersianDateConverter.PersianDate(1377, 11, 22);

        LocalDate g =
                PersianDateConverter.toGregorian(
                        original.year,
                        original.month,
                        original.day
                );

        PersianDateConverter.PersianDate back =
                PersianDateConverter.toPersian(g);

        assertEquals(original.year, back.year);
        assertEquals(original.month, back.month);
        assertEquals(original.day, back.day);
    }
}
