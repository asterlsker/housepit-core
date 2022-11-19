package com.asterlsker.housepit.core.config

import org.slf4j.LoggerFactory
import org.springframework.boot.SpringApplication
import org.springframework.boot.context.properties.bind.Bindable
import org.springframework.boot.context.properties.bind.Binder
import org.springframework.boot.context.properties.bind.PropertySourcesPlaceholdersResolver
import org.springframework.boot.context.properties.source.ConfigurationPropertySources
import org.springframework.boot.env.EnvironmentPostProcessor
import org.springframework.boot.env.YamlPropertySourceLoader
import org.springframework.core.Ordered
import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.core.env.Profiles
import org.springframework.core.env.PropertySource
import org.springframework.core.io.DefaultResourceLoader
import org.springframework.core.io.Resource
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.core.io.support.ResourcePatternResolver
import java.util.*

class CustomEnvironmentPostProcessor : EnvironmentPostProcessor, Ordered {
    private val log = LoggerFactory.getLogger(javaClass)
    private val defaultOrder = Ordered.LOWEST_PRECEDENCE
    private val springProfiles = "spring.config.activate.on-profile"
    private val yamlLoader = YamlPropertySourceLoader()

    override fun postProcessEnvironment(environment: ConfigurableEnvironment?, application: SpringApplication?) {
        try {
            val resourceLoader = application?.resourceLoader ?: DefaultResourceLoader()
            val resourcePatternResolver: ResourcePatternResolver = PathMatchingResourcePatternResolver(resourceLoader)

            val resources: List<Resource> = resourcePatternResolver.getResources("classpath*:*.yml").toList()
            resources.forEach { resource ->
                loadYaml(resource, environment!!).forEach { propertySource ->
                    environment.propertySources.addLast(propertySource)
                }
            }
        } catch (e: Exception) {
            log.error("# [CustomEnvironmentPostProcessor] message: {}, stacktrace: {}", e.message, e.stackTrace)
        }
    }


    override fun getOrder(): Int {
        return defaultOrder
    }

    // function
    private fun loadYaml(path: Resource, environment: ConfigurableEnvironment): List<PropertySource<*>> {
        if (!path.exists()) {
            throw java.lang.IllegalArgumentException("Resource does not exist: $path")
        }

        val defaultPropertySource = mutableListOf<PropertySource<*>>()

        val propertySourceList = yamlLoader.load(path.filename, path)
            .filter {
                val binder = Binder(
                    ConfigurationPropertySources.from(it),
                    PropertySourcesPlaceholdersResolver(environment)
                )
                val profiles = binder.bind(springProfiles, Bindable.of(Array<String>::class.java))
                println(profiles)
                println(profiles.isBound)
                if (!profiles.isBound) {
                    defaultPropertySource.add(it)
                    return@filter false
                }
                environment.acceptsProfiles(Profiles.of(*profiles.get()))
            }
            .toList()

        return propertySourceList + defaultPropertySource
    }
}