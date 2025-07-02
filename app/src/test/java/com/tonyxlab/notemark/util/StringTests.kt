package com.tonyxlab.notemark.util

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class FormatUserInitialsTest : StringSpec( {

    "should return first 2 letters for single-word usernames" {
        formatUserInitials("Ein") shouldBe "EI"
        formatUserInitials("Tonnie") shouldBe "TO"
    }

    "should return first and last initials for two-word usernames" {
        formatUserInitials("Tonnie Ein") shouldBe "TE"
        formatUserInitials("Einstein Albert") shouldBe "EA"
    }

    "should return first and last initials for multi-word usernames" {
        formatUserInitials("Tonnie Albert Ein") shouldBe "TE"
        formatUserInitials("John Ronald Reuel Tolkien") shouldBe "JT"
    }

    "should ignore extra spaces" {
        formatUserInitials("   tonnie   ein   ") shouldBe "TE"
    }

    "should return empty for empty or blank username" {
        formatUserInitials("") shouldBe ""
        formatUserInitials("   ") shouldBe ""
    }
})


