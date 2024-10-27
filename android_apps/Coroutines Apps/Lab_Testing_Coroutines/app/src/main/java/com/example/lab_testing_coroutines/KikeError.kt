package com.example.lab_testing_coroutines

open class KikeError(
    var headline: String? = null,
    var description: String? = null,
    var displayError: Boolean = false,
    var offerHelp: Boolean = true,
    var takId: String? = null,
    var sealOneId: String? = null
): Exception() {
    override val message: String?
        get() = (
                "$LABEL_HEADLINE:${headline?.sanitize()}, " +
                        "$LABEL_DESCRIPTION:${description?.sanitize()}, " +
                        "$LABEL_DISPLAY_ERROR:$displayError, " +
                        "$LABEL_OFFER_HELP:$offerHelp" +
                        "$LABEL_TAKID:$takId, " +
                        "$LABEL_SEALONEID:$sealOneId"
                )

    companion object {
        internal const val LABEL_HEADLINE = "Headline"
        internal const val LABEL_DESCRIPTION = "Description"
        internal const val LABEL_DISPLAY_ERROR = "DisplayError"
        internal const val LABEL_OFFER_HELP = "OfferHelp"
        internal const val LABEL_TAKID = "TakId"
        internal const val LABEL_SEALONEID= "SealOneId"
    }
}

fun String.sanitize() =
    this
        .replace(",", "")
        .replace("\n", "")
        .replace("\t", "")
        .replace("\r", "")
        .replace(":", "%3A")