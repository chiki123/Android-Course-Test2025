import java.io.*

fun main() {
    val br = BufferedReader(InputStreamReader(System.`in`))
    val bw = BufferedWriter(OutputStreamWriter(System.out))

    try {
        // Чтение количества наборов входных данных
        val t = br.readLine().toInt()

        // Проверка, что количество наборов в допустимом диапазоне (1 ≤ t ≤ 10^4)
        require(t in 1..10_000) { "Недопустимое количество наборов. Оно должно быть от 1 до 10000." }

        // Обработка каждого набора входных данных
        repeat(t) {
            // Чтение строки и разделение на два числа
            val (a, b) = br.readLine().trim().split("\\s+".toRegex()).map { it.toInt() }

            // Проверка, что числа находятся в допустимом диапазоне (-1000 ≤ a, b ≤ 1000)
            require(a in -1000..1000 && b in -1000..1000) { "Некорректные числа. Они должны быть в диапазоне от -1000 до 1000." }

            // Запись результата
            bw.write("${a + b}\n")
        }
    } catch (e: Exception) {
        // Обработка ошибок (например, неверный ввод)
        bw.write("Ошибка: ${e.message}\n")
    } finally {
        // Закрытие ресурсов
        bw.flush()
        bw.close()
        br.close()
    }
}