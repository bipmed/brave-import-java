package org.bipmed.brave.import

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BraveImportApplication

fun main(args: Array<String>) {
    runApplication<BraveImportApplication>(*args)
}