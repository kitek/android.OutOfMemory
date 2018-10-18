package pl.kitek.androidoutofmemory

class A(var b: B?)
class B

fun main(args: Array<String>) {
    val a = A(B())
    a.b = null
}


//class A
//class B(var a: A?)
//class C(var b: B?, var d: D?)
//class D
//
//fun main(args: Array<String>) {
//    val a = A()
//    val b = B(a)
//    val d = D()
//    val c = C(b, d)
//
//    c.b
//    c.d
//
//
//    println("Hello refs")
//}


//class A
//class B
//class C(var a: A?, var b: B?)
//class D(var aRef: WeakReference<A>)
////class D(var aRef: SoftReference<A>)
//
//fun main(args: Array<String>) {
//    var a: A? = A()
//    val b = B()
//    val c = C(a, b)
//    val d = D(WeakReference(a!!))
////    val d = D(SoftReference(a!!))
//
//    a = null
//    c.a = null
//    gc()
//
//    println("Hello refs")
//    println("a = $a")
//    println("c.a = ${c.a} c.b = ${c.b}")
//    println("d.a = ${d.aRef.get()}")
//}
