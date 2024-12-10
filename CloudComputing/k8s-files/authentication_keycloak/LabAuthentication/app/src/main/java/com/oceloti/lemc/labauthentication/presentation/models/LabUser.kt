package com.oceloti.lemc.labauthentication.presentation.models

data class LabUser(
  val id: String,          // Maps to "sub" (subject identifier)
  val name: String?,       // Maps to "name"
  val firstName: String?,  // Maps to "given_name"
  val lastName: String?,   // Maps to "family_name"
  val email: String?,      // Maps to "email"
  val picture: String?,     // Maps to "picture"
  val nonce: String?
)
