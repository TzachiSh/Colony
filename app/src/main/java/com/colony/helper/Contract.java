package com.colony.helper;


public final class Contract {
    public static final String EXTRA_Chat_ID = "com.colony.identifier"; // key id of the chat
    public static final String EXTRA_Chat_Name = "com.colony.name";     // key name of the sender
    public static final String EXTRA_Chat_Message = "com.colony.message"; //key message
    public static final String EXTRA_Chat_Date = "com.colony.date"; //key Date time
    public static final String EXTRA_Chat_Number = "com.colony.number"; //key userId of the sender.
    //////////////////////////////////////////////////////////////////////////////////////////////////////
    public static final String ACTION_Message_CHANGED = "com.colony.changed"; //key check if receive message
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static final String Shared_User_Number = "com.colony.login"; // key check if the user login
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static final String Fragment_Main_Replaced = "com.colony.replaced"; //key for replaced fragment in the main activity
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static final String ACTION_sms_valid = "com.colony.valid";
}
