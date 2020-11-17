fun JavaClass.foo(javaClass: JavaClass) {
    print(javaClass.<caret>something)
}

// REF: (in JavaClass).getSomething()