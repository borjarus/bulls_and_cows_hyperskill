package bullscows

import java.util.LinkedHashSet
import kotlin.random.Random

fun generateSecret(length: Int): String = when {
    length > 10 -> "Error: can't generate a secret number with a length of $length because there aren't enough unique digits."
    length <= 0 -> "Error: length must be positive"
    else ->
        (1..9).random()
            .let { first ->
                generateSequence(first.toString()) { acc ->
                    val nextDigit = Random.nextInt(0, 10).toString()
                    if (nextDigit !in acc) acc + nextDigit else acc
                }
                .take(length)
                .first { it.length == length }
            }
}

fun grade(secret: String, guess: String): Pair<Int, Int> {
    val bulls = secret.indices.count { i -> secret[i] == guess[i] }
    val totalMatches = ('0'..'9').sumOf { ch ->
        minOf(secret.count { it == ch }, guess.count { it == ch })
    }
    val cows = totalMatches - bulls
    return bulls to cows
}

fun formatGrade(bulls: Int, cows: Int): String = when {
    bulls == 0 && cows == 0 -> "None"
    bulls > 0 && cows > 0 -> "$bulls bull(s) and $cows cow(s)"
    bulls > 0 -> "$bulls bull(s)"
    else -> "$cows cow(s)"
}

fun main() {
    println("Please, enter the secret code's length:")
    val length = readln().trim().toInt()

    val secret = generateSecret(length)
    if (secret.startsWith("Error")) {
        println(secret)
        return
    }

    println("Okay, let's start a game!")

    var turn = 1
    while (true) {
        println("Turn $turn:")
        val guess = readln().trim()

        val (bulls, cows) = grade(secret, guess)
        val gradeMsg = formatGrade(bulls, cows)
        println("Grade: $gradeMsg")

        if (bulls == length) {
            println("Congratulations! You guessed the secret code.")
            return
        }

        turn++
    }
}