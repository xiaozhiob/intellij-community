class AX825 {
    init {
        foo()
        v + v

        v[v] // using companion object function by convention
    }

    companion object {
        fun foo() {
        }

        val v = 42

        operator fun Int.get(a: Int) = this + a
    }
}

fun main(args: Array<String>) {
    println(args)
    AX825()
}
