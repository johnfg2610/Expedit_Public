package io.github.johnfg10

class TypeHelper {

    companion object {
        fun IsInt(string: String) : Boolean{
            return Regex("\\d+").matches(string)
        }

        //finds the first int in a list of strings if not found returns -1
        fun getIntIndex(strings: List<String>): Int {
            strings.forEachIndexed {
                index, s ->  if (IsInt(s)) return index
            }

            return -1
        }
    }
}