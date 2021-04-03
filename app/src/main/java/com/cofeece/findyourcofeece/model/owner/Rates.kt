package com.cofeece.findyourcofeece.model.owner

class Rates {
    /** Properties: */
    private lateinit var day: String
    private lateinit var fromHour: String
    private lateinit var toHour: String
    private lateinit var rate: String

    /** Gets: */
    fun getDay(): String = day
    fun getFromHour(): String = fromHour
    fun getToHour(): String = toHour
    fun getRate(): String = rate

    /** Sets: */
    fun setDay(day: String) {
        this.day = day
    }

    fun setFromHour(hour: String) {
        this.fromHour = hour
    }

    fun setToHour(hour: String) {
        this.toHour = hour
    }

    fun setRate(day: String) {
        this.rate = rate
    }

    /** Methods: */
}