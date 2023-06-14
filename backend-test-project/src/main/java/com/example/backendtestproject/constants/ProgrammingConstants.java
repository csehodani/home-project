package com.example.backendtestproject.constants;


import org.springframework.util.StringUtils;

public class ProgrammingConstants {

    public static String saveSuccess(String itemType) {
        return StringUtils.capitalize(itemType.toLowerCase()) + " was successfully saved! ";
    }

    public static String saveFail(String itemType) {
        return StringUtils.capitalize(itemType.toLowerCase()) + " cannot be saved! ";
    }

    public static String deleteSuccess(String itemType) {
        return StringUtils.capitalize(itemType.toLowerCase()) + " was successfully deleted! ";
    }

    public static String zipCodeInvalid = "Please add a valid zip code! ";

    public static String startDateInvalid = "Please add a valid start date : dd/MM/yyyy ";

    public static String cityMissing = "City is missing! ";

    public static String streetMissing = "Street is missing! ";

    public static String nameMissing = "Name is missing! ";

    public static String responsibilityMissing = "Responsibility is missing! ";

    public static String apprenticeMissing = "Apprentice is missing! ";

    public static String noProjectManagerFound = "No project manager found! ";

    public static String noProgrammerFound = "No programmer found! ";

    public static String noProjectFound = "No project found! ";

    public static String birthDayMissing = "Birth day is missing! ";

    public static String birthMonthMissing = "Birth month is missing! ";

    public static String birthYearMissing = "Birth year is missing! ";

    public static String birthDayInvalid = "Please add a valid birth day! ";

    public static String birthMonthInvalid = "Please add a valid birth month! ";

    public static String birthYearInvalid = "Please add a valid birth year! ";

    public static String emailMissing = "Email is missing! ";

    public static String comIsMissing = "Please add a valid email! Missing item: '.com' ";

    public static String atIsMissing = "Please add a valid email! Missing item: '@' ";

    public static String phoneNumberMissing = "Phone number is missing! ";

    public static String phoneNumberInvalid = "Please add a valid phone number! ";

    public static String clientMissing = "Client is missing! ";

    public static String descriptionMissing = "Description is missing! ";

    public static String startDateMissing = "Start date is missing! ";

    public static String emailExists = "The given email already exists! ";
}
