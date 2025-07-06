package com.tonyxlab.notemark.util

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class TrimTextTest: StringSpec({

    val longText = "This text is very too long"
    "should trim to five characters"{

        trimTextToFit(content = longText, maxCharacters = 5) shouldBe "This…"
    }

    "should not trim case"{

        trimTextToFit(content = longText, maxCharacters = 150) shouldBe longText
    }

    "should not trim when length is equal to limit" {
        val text = "Hello"
        trimTextToFit(text, 5) shouldBe text
    }
}

)