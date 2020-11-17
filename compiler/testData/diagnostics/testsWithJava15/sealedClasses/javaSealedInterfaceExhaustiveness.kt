// ISSUE: KT-41215

// FILE: Base.java
public sealed interface Base permits A, B, E {}

// FILE: A.java
public non-sealed interface A extends Base {}

// FILE: B.java
public sealed interface B extends Base permits B.C, B.D {
    public static final class C implements B {}

    public static non-sealed interface D extends B {}
}

// FILE: E.java
public enum E implements Base {
    First, Second
}

// FILE: main.kt
fun test_ok_1(<!EXPOSED_PARAMETER_TYPE!>base: Base<!>) {
    val <!UNUSED_VARIABLE!>x<!> = <!NO_ELSE_IN_WHEN!>when<!> (base) {
        is A -> 1
        is B -> 2
        is E -> 3
    }
}

fun test_ok_2(<!EXPOSED_PARAMETER_TYPE!>base: Base<!>) {
    val <!UNUSED_VARIABLE!>x<!> = <!NO_ELSE_IN_WHEN!>when<!> (base) {
        is A -> 1
        is B.<!UNRESOLVED_REFERENCE!>C<!> -> 2
        is B.<!UNRESOLVED_REFERENCE!>D<!> -> 3
        E.First -> 4
        E.Second -> 5
    }
}
