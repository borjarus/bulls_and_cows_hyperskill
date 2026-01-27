package bullscows
import java.util.LinkedHashSet

fun main() {
    val length = readln().trim().toInt()

    if (length > 10) {
        println("Error: can't generate a secret number with a length of $length because there aren't enough unique digits.")
        return
    }

    val secret = generateSecret(length)
    println("The random secret number is $secret.")
}

fun generateSecret(length: Int): String =
    generateSequence { System.nanoTime().toString().reversed() }
        .map { takeUniqueDigits(it, length) }
        .first { it.length == length }

private fun takeUniqueDigits(source: String, length: Int): String {
    // first, build unique digits from source, skipping leading zero
    val raw = source.fold("") { acc, ch ->
        if (acc.length == length) acc
        else if (ch !in acc && !(acc.isEmpty() && ch == '0')) acc + ch
        else acc
    }

    if (raw.length == length) return raw

    // if still short, fill from remaining digits 0–9 (keeping first digit non‑zero)
    val used = raw.toSet()
    val filler = ('0'..'9')
        .asSequence()
        .filter { it !in used }
        .filterIndexed { index, ch -> !(raw.isEmpty() && index == 0 && ch == '0') }
        .take(length - raw.length)
        .joinToString("")

    return raw + filler
}