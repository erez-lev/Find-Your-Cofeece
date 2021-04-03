package com.cofeece.findyourcofeece.model.client

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Payment(
    private var mCardName: String,
    private var mCardNumber: String,
    private var mMonth: String,
    private var mYear: String,
    private var mCVV: String
) : Parcelable {

    /** Constructors: */
    constructor() : this("", "", "","", "")

    /** Gets: */
    fun getCardName(): String = mCardName
    fun getCardNumber(): String = mCardNumber
    fun getMonth(): String = mMonth
    fun getYear(): String = mYear
    fun getCVV(): String = mCVV

    /** Sets: */
    fun setCardName(iCardName: String) {
        mCardName = iCardName
    }
    fun setCardNumber(iCardNumber: String) {
        mCardNumber = iCardNumber
    }
    fun setMonth(iMonth: String) {
        mMonth = iMonth
    }
    fun setYear(iYear: String) {
        mYear = iYear
    }
    fun setCVV(iCVV: String) {
        mCVV = iCVV
    }
}