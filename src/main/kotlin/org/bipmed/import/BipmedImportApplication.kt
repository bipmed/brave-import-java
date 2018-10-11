package org.bipmed.import

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BipmedImportApplication

fun main(args: Array<String>) {
    runApplication<BipmedImportApplication>(*args)
}