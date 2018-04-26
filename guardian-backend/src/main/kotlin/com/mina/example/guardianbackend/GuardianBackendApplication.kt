package com.mina.example.guardianbackend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GuardianBackendApplication

fun main(args: Array<String>) {
    runApplication<GuardianBackendApplication>(*args)
}


data class Section(
        val id: String,
        @JsonProperty("webTitle") val title: String
)

data class SectionResponse(
        val results: List<Section>
)

data class SectionPayload(
        val response: SectionResponse
)


@Component
open class GuardianApi(
    @Value("\${guardian.apiKey}") private val apiKey: String
) {
    private val restTemplate = RestTemplate()

    open fun listSections(): List<Section> {
        val uri = UriComponentsBuilder.fromUriString("http://content.guardianapis.com/sections")
                .queryParam("api-key", apiKey)
                .build()
                .toUri()

        return restTemplate.getForObject(uri, SectionPayload::class.java)
                .response.results
    }
}


@RestController
class SectionController(
        private val api: GuardianApi
) {
    @RequestMapping("/sections")
    fun getSections() = api.listSections()
}