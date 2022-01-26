package de.dqmme.discordscam

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File
import kotlin.io.path.*

fun main() {
    embeddedServer(Netty, port = 8083) {
        val file = Path("count.txt")

        if(!file.exists()) file.createFile()

        routing {
            get("/") {
                val count = file.readText().toIntOrNull() ?: 0

                file.writeText("${count + 1}")

                call.respondText(
                    this::class.java.classLoader.getResource("index.html")!!.readText()
                        .replace("Seitenaufrufe: ", "Seitenaufrufe: ${count + 1}"),
                    ContentType.Text.Html
                )
            }

            static("/static") {
                resources("static")
            }
        }
    }.start(wait = true)
}