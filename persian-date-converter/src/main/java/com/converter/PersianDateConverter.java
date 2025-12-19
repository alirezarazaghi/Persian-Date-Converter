package com.converter;

import java.time.LocalDate;

public final class PersianDateConverter {

    private PersianDateConverter() {}

    public static final class PersianDate {
        public final int year;
        public final int month;
        public final int day;

        public PersianDate(int year, int month, int day) {
            this.year = year;
            this.month = month;
            this.day = day;
        }

        @Override
        public String toString() {
            return String.format("%04d-%02d-%02d", year, month, day);
        }
    }

    // ===============================
    // Gregorian → Persian (Jalali)
    // ===============================
    public static PersianDate toPersian(LocalDate g) {
        int gy = g.getYear() - 1600;
        int gm = g.getMonthValue() - 1;
        int gd = g.getDayOfMonth() - 1;

        int[] gDays = {31,28,31,30,31,30,31,31,30,31,30,31};
        int gDayNo = 365 * gy + (gy + 3) / 4 - (gy + 99) / 100 + (gy + 399) / 400;

        for (int i = 0; i < gm; ++i)
            gDayNo += gDays[i];

        if (gm > 1 && isGregorianLeap(gy + 1600))
            gDayNo++;

        gDayNo += gd;

        int jDayNo = gDayNo - 79;

        int jNp = jDayNo / 12053;
        jDayNo %= 12053;

        int jy = 979 + 33 * jNp + 4 * (jDayNo / 1461);
        jDayNo %= 1461;

        if (jDayNo >= 366) {
            jy += (jDayNo - 1) / 365;
            jDayNo = (jDayNo - 1) % 365;
        }

        int[] jDays = {31,31,31,31,31,31,30,30,30,30,30,29};
        int jm = 0;
        for (; jm < 11 && jDayNo >= jDays[jm]; jm++)
            jDayNo -= jDays[jm];

        return new PersianDate(jy, jm + 1, jDayNo + 1);
    }

    // ===============================
    // Persian → Gregorian
    // ===============================
    public static LocalDate toGregorian(int jy, int jm, int jd) {
        jy -= 979;
        jm--;
        jd--;

        int[] jDays = {31,31,31,31,31,31,30,30,30,30,30,29};
        int jDayNo = 365 * jy + (jy / 33) * 8 + ((jy % 33 + 3) / 4);

        for (int i = 0; i < jm; ++i)
            jDayNo += jDays[i];

        jDayNo += jd;

        int gDayNo = jDayNo + 79;

        int gy = 1600 + 400 * (gDayNo / 146097);
        gDayNo %= 146097;

        boolean leap = true;
        if (gDayNo >= 36525) {
            gDayNo--;
            gy += 100 * (gDayNo / 36524);
            gDayNo %= 36524;
            if (gDayNo >= 365) gDayNo++;
            else leap = false;
        }

        gy += 4 * (gDayNo / 1461);
        gDayNo %= 1461;

        if (gDayNo >= 366) {
            leap = false;
            gDayNo--;
            gy += gDayNo / 365;
            gDayNo %= 365;
        }

        int[] gDays = {31,28,31,30,31,30,31,31,30,31,30,31};
        int gm = 0;
        for (; gm < 12 && gDayNo >= gDays[gm] + ((gm == 1 && leap) ? 1 : 0); gm++)
            gDayNo -= gDays[gm] + ((gm == 1 && leap) ? 1 : 0);

        return LocalDate.of(gy, gm + 1, gDayNo + 1);
    }

    private static boolean isGregorianLeap(int y) {
        return (y % 4 == 0 && y % 100 != 0) || (y % 400 == 0);
    }

    // ===============================
    // Tests
    // ===============================
    public static void main(String[] args) {
        System.out.println("2025-10-18 -> " + toPersian(LocalDate.of(2025,10,18)));
        System.out.println("1999-02-11 -> " + toPersian(LocalDate.of(1999,2,11)));
        System.out.println("1404-07-26 -> " + toGregorian(1404,7,26));
        System.out.println("1400-01-01 -> " + toGregorian(1400,1,1));
    }
}
