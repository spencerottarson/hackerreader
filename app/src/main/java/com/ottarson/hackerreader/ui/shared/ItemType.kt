package com.ottarson.hackerreader.ui.shared

enum class ItemType {
    Job,
    Story,
    Comment,
    Poll,
    PollOption,
    Unknown,
    ;

    companion object {
        fun fromString(string: String?): ItemType {
            return when (string) {
                "job" -> Job
                "story" -> Story
                "comment" -> Comment
                "poll" -> Poll
                "pollop" -> PollOption
                else -> Unknown
            }
        }
    }
}
