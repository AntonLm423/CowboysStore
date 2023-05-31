package com.example.cowboysstore.utils

class Constants {

    companion object {

        /* BASE URL FOR HTML REQUESTS */
        const val BASE_URL = "http://45.144.64.179/cowboys/api/"

        /* DATE FORMAT FROM BACKEND */
        const val INPUT_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

        /* KEY FOR FRAGMENT MANAGER */
        const val PROFILE_FRAGMENT_KEY = "profile_fragment_key"

        /* KEY FOR SHOWSNACKBAR FLAG */
        const val SHOW_SNACK_BAR_KEY = "show_snack_bar_key"

        /* KEY FOR ORDERS LIST FRAGMENT INSTANCE */
        const val ORDERS_LIST_KEY = "orders_list_key"

        /*******************DATA TRANSACTIONS BETWEEN FRAGMENTS*******************/
        /* KEY FOR PRODUCT ID TRANSACTION CATALOG->PRODUCT */
        const val PRODUCT_ID_KEY = "product_id_key"

        /* KEY FOR PRODUCT TRANSACTION PRODUCT->CHECKOUT */
        const val PRODUCT_KEY = "product_key"

        /* KEY FOR SELECTED SIZE TRANSACTION PRODUCT->CHECKOUT */
        const val SELECTED_SIZE_KEY = "selected_size_key"

        /*KEY FOR LOCATION TRANSACTION MAP->CHECKOUT*/
        const val LOCATION_KEY = "location_key"
        const val LOCATION_BUNDLE_KEY = "location_bundle_key"
    }

}

