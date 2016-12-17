package com.colony.helper;

import android.content.Context;
import android.telephony.TelephonyManager;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

public class FixPhoneNumber {

    public static String fixPhoneNumber(Context ctx , String rawNumber)
    {

        String      fixedNumber = "";

        // get current location iso code
        TelephonyManager telMgr = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        String              curLocale = telMgr.getNetworkCountryIso().toUpperCase();

        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        Phonenumber.PhoneNumber     phoneNumberProto;

        // gets the international dialling code for our current location
        String              curDCode = String.format("%d", phoneUtil.getCountryCodeForRegion(curLocale));
        String              ourDCode = "";

        if(rawNumber.indexOf("+") == 0)
        {
            int     bIndex = rawNumber.indexOf("(");
            int     hIndex = rawNumber.indexOf("-");
            int     eIndex = rawNumber.indexOf(" ");

            if(bIndex != -1)
            {
                ourDCode = rawNumber.substring(1, bIndex);
            }
            else if(hIndex != -1)
            {
                ourDCode = rawNumber.substring(1, hIndex);
            }
            else if(eIndex != -1)
            {
                ourDCode = rawNumber.substring(1, eIndex);
            }
            else
            {
                ourDCode = curDCode;
            }
        }
        else
        {
            ourDCode = curDCode;
        }

        try
        {
            phoneNumberProto = phoneUtil.parse(rawNumber, curLocale);
        }

        catch (NumberParseException e)
        {
            return rawNumber;
        }

        if(curDCode.compareTo(ourDCode) == 0)
            fixedNumber = phoneUtil.format(phoneNumberProto, PhoneNumberUtil.PhoneNumberFormat.NATIONAL);
        else
            fixedNumber = phoneUtil.format(phoneNumberProto, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);

        if(rawNumber.indexOf("(") == 0) {
            fixedNumber = rawNumber.replaceAll("()", "");

        }


        return fixedNumber.replace(" ", "");

    }


}
