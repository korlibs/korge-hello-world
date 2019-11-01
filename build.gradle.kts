import com.soywiz.korge.gradle.*

buildscript {
	repositories {
		mavenLocal()
		maven { url = uri("https://dl.bintray.com/korlibs/korlibs") }
		maven { url = uri("https://plugins.gradle.org/m2/") }
		mavenCentral()
	}
	dependencies {
		classpath("com.soywiz.korlibs.korge.plugins:korge-gradle-plugin:1.3.0")
	}
}

apply(plugin = "korge")

korge {
	id = "com.sample.demo"
}
