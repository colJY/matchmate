package com.lee.matchmate.common


object Constants {
    //Common
    const val BLANK = ""
    const val SPLIT = ","
    const val UNDERSCORE = "_"

    //ChatDetailAdapter
    const val DATE_FORMAT = "MM-dd HH:mm"
    const val ROUNDED_CORNER_RADIUS = 80
    const val DEFAULT_USER_ID = ""

    //ChatDetailFragment
    const val REPLACE_USER_ID = "_"
    const val THROTTLE_FIRST_INTERVAL = 500L
    const val ITEM_DIVIDER = 0
    const val ITEM_DECORATION_SPACE = 10
    const val NOTIFICATION_TITLE = "New message"

    //ChatDetailRepository
    const val CHAT_COLLECTION_NAME = "chat"
    const val USER_COLLECTION_NAME = "user"

    //ChatMessage
    const val FIELD_ID = "id"
    const val FIELD_SELECTED_COND_LIST = "selectedCondList"
    const val FIELD_CHAT_MESSAGE = "chatMessage"
    const val FIELD_SENDER = "sender"
    const val FIELD_TIMESTAMP = "timestamp"
    const val FIELD_MESSAGE = "message"

    //FCMApi
    const val FCM_SEND_ENDPOINT = "fcm/send"

    //FCMRepository
    const val BASE_URL = "https://fcm.googleapis.com/"
    const val HEADER_AUTHORIZATION = "Authorization"
    const val HEADER_CONTENT_TYPE = "Content-Type"
    const val CONTENT_TYPE = "application/json"
    const val AUTHORIZATION_KEY_PREFIX = "key="

    //FCMService
    const val CHANNEL_ID = "FCM_CHANNEL_ID"
    const val CHANNEL_NAME = "PUSH_NAME"
    const val ROOM_ID = "roomId"
    const val MESSAGE = "message"

    //ChatFragment
    const val ITEM_DECORATION_CHAT_SPACE = 3

    //ChatFragment, ChatUserListAdapter
    const val USER_ID = "userId"
    const val ROUNDING_RADIUS = 80

    //ChatRepository
    const val CHAT_REPO_TAG = "ChatRepository"

    //AppGlobalContext
    const val SHARED_PREFS_FILE_NAME = "shared_prefs"

    //FavoriteAdapter
    const val UPLOAD_IMAGES = "uploadImages"

    //FavoriteRepository
    const val FIRESTORE_COLLECTION_NAME = "Space"
    const val FAVORITE = "fav"

    //LoginFragment
    const val IS_LOGGED_IN = "IS_LOGGED_IN"
    const val WELCOME_MESSAGE_PREFIX = "환영합니다 "
    const val WELCOME_MESSAGE_SUFFIX = " 님"

    //AddSpaceFragment
    const val LATLNG_COLLECTION_NAME = "latlng"
    const val UPLOAD_IMAGES_URI = "uploadImages/"
    const val IMAGE_PICKER = "image/*"

    //AddSpaceViewModel
    const val LAT = 37.566168
    const val LNG = 126.901609

    //DetailZoomOutAnimation
    const val MIN_SCALE = 0.85f
    const val MIN_ALPHA = 0.5f

    //DetailFragment
    const val SQUARE_BRACKET_LEFT = "["
    const val SQUARE_BRACKET_RIGHT = "]"
    const val COMMAS = ","
    const val UNKNOWN_ERROR = "알 수 없는 오류"

    //DetailRepository
    const val CHAT_COLLECTION_ID = "chatId"

    //DetailViewPagerAdapter
    const val IMAGE_COLLECTION_NAME = "uploadImages"

    //FilterFragment
    const val roomApart = "아파트"
    const val roomOffice = "오피스텔"
    const val roomVilla = "빌라"

    //GeocoderApi
    const val GEOCODE_API_URL = "maps/api/geocode/json"
    const val DEFAULT_LANGUAGE = "ko"
    const val Components = "components"
    const val Key = "key"
    const val Language = "language"

    //GeocoderRepository
    const val MAP_BASE_URL = "https://maps.googleapis.com/"
    const val GEO_TAG = "GeocoderRepository"

    //ReverseGeoEntity
    const val FormattedAddress = "formatted_address"

    //SpaceMarker
    const val lat = "lat"
    const val lng = "lng"

    //ChipSelectRepository
    const val COND_COLLECTION_NAME = "cond"
    const val COND_DOCUMENT_NAME = "condition"
    const val COND_FIELD_NAME = "condList"

    //MainFragment
    const val MAIN_LAT = 37.5665
    const val MAIN_LNG = 126.9780
    const val LOCALITY_PREFIX = "locality:"
    const val COUNTRY_PREFIX = "country:"
    const val COUNTRY_CODE = "KR"
    const val ZOOM_LEVEL = 10.0f

    //Space
    const val NONE_TITLE = "제목 없음"
    const val FIELD_LOCATION = "location"
    const val FIELD_PRIMARY_IMAGE = "primaryImage"
    const val FIELD_ADDITIONAL_IMAGE = "additionalImage"
    const val FIELD_COND = "cond"
    const val FIELD_VALUE = "value"
    const val FIELD_TYPE = "type"
    const val FIELD_USER_ID = "userId"
    const val FIELD_TITLE = "title"
    const val FIELD_FAV = "fav"
    const val FIELD_USER_NAME = "userName"
    const val FIELD_PROFILE_IMAGE = "profileImage"
    const val FIELD_SPACE_ID = "spaceId"
    const val FIELD_CHAT_ID = "chatId"
    const val FIELD_FCM_TOKEN = "fcmToken"

}