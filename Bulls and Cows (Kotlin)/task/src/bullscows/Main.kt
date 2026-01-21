package bullscows

fun main() {
    val secret = "9305"
    val guess = readln().trim()

    val bulls = countBulls(secret, guess)
    val cows = countCows(secret, guess)

    val grade = when {
        bulls == 0 && cows == 0 -> "None"
        bulls > 0 && cows > 0 -> "$bulls bull(s) and $cows cow(s)"
        bulls > 0 -> "$bulls bull(s)"
        else -> "$cows cow(s)"
    }

    println("Grade: $grade. The secret code is $secret.")
}

fun countCows(secret: String, guess: String): Int =
    ('0'..'9').sumOf { ch ->
        val inSecret = secret.count { it == ch }
        val inGuess = guess.count { it == ch }
        minOf(inSecret, inGuess)
    }


fun countBulls(secret: String, guess: String): Int =
    secret.indices.count { i -> secret[i] == guess[i] }
