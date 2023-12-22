package util

object Resource {
    fun readText(path: String) = javaClass.classLoader.getResource(path)!!.readText()
}