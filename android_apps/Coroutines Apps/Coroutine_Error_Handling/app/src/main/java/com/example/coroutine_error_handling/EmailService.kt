package com.example.coroutine_error_handling

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlin.random.Random

object EmailService {
    private val mailingList = mutableListOf<String>()

    fun addToMailingList(emails: List<String>) {
        mailingList.addAll(emails)
    }

    suspend fun sendNewsletter() {
        //Here the solution
        supervisorScope {
            mailingList.forEach { emailAddress ->
                launch {
                    sendEmail(emailAddress)
                }
            }
        }
    }

    private suspend fun sendEmail(address: String) {
        println("Sending email to $address")
        if (!address.contains("@")) {
            throw Exception("Invalid email address: $address")
        }
        delay(Random.nextLong(1500, 4000))
        println("Email sent to $address")
    }
}