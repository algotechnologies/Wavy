package com.wavy.app.util;

public final class Constants {

    /**
     * Base url
     */
    private static final String BASE_URL = "https://private-anon-4c23b1ba03-test16231.apiary-mock.com/user/";
    //--- Base url ends here ---\\

    /**
     * Params after base url
     */
    public static final String GET_USER = BASE_URL + "all";
    public static final String DELETE_USER = BASE_URL;
    //--- Params after base url end here ---\\

    /**
     * Global constant strings
     */
    public static final String TAG = "WAVY";
    public static final String NO_INTERNET_MSG = "No internet connection!";
    public static final String SOMETHING_WRONG_MSG = "Something went wrong!";
    public static final String PHONE_DELETED_MSG = "Phone number is deleted already!";
    public static final String DELETING_PHONE_MSG = "Deleting phone number...";
    public static final String DELETE_PHONE_SUCCESS_MSG = "User's number deleted successfully";
    public static final String REQUEST_METHOD_ERROR_MSG = "Request method must be GET or DELETE";
    //--- Global constant strings end here ---\\

}
