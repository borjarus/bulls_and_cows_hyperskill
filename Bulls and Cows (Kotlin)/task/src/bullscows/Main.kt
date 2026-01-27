package bullscows
import java.util.LinkedHashSet

fun generateSecret(length: Int): String = when {
    length > 10 -> "Error: can't generate a secret number with a length of $length because there aren't enough unique digits."
    length <= 0 -> "Error: length must be positive"
    else ->     generateSequence { System.nanoTime().toString().reversed() }
        .map { takeUniqueDigits(it, length) }
        .first { it.length == length }
}


private fun takeUniqueDigits(source: String, length: Int): String {
    val raw = source.fold("") { acc, ch ->
        if (acc.length == length) acc
        else if (ch !in acc && !(acc.isEmpty() && ch == '0')) acc + ch
        else acc
    }

    if (raw.length == length) return raw

    val used = raw.toSet()
    val filler = ('0'..'9')
        .asSequence()
        .filter { it !in used }
        .filterIndexed { index, ch -> !(raw.isEmpty() && index == 0 && ch == '0') }
        .take(length - raw.length)
        .joinToString("")

    return raw + filler
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
