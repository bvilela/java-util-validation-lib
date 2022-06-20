# Java Util Validation Lib

### Quality Status
[![build](https://github.com/bvilela/java-util-validation-lib/actions/workflows/maven_ci_cd.yml/badge.svg?branch=master)](https://github.com/bvilela/java-util-validation-lib/actions/workflows/maven_ci_cd.yml)
[![publish](https://github.com/bvilela/java-util-validation-lib/actions/workflows/maven_ci_cd_publish.yml/badge.svg)](https://github.com/bvilela/java-util-validation-lib/actions/workflows/maven_ci_cd_publish.yml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=bvilela_java-util-validation-lib&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=bvilela_java-util-validation-lib)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=bvilela_java-util-validation-lib&metric=coverage)](https://sonarcloud.io/summary/new_code?id=bvilela_java-util-validation-lib)

### Repository Statistics
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=bvilela_java-util-validation-lib&metric=ncloc)](https://sonarcloud.io/summary/new_code?id=bvilela_java-util-validation-lib)
![GitHub repo size](https://img.shields.io/github/repo-size/bvilela/java-util-validation-lib)
![GitHub language count](https://img.shields.io/github/languages/count/bvilela/java-util-validation-lib)
![GitHub open issues](https://img.shields.io/github/issues-raw/bvilela/java-util-validation-lib)
![GitHub open pull requests](https://img.shields.io/github/issues-pr/bvilela/java-util-validation-lib)
<!--![GitHub forks](https://img.shields.io/github/forks/bvilela/java-util-validation-lib)-->


## Summary
Project with validations utils for Java based in javax and Gson.


## Technologies
* Maven
* Java 11
* [Lombok](https://projectlombok.org/)
* Gson 2.9.0
* Static Code Analysis: [SonarCloud](https://sonarcloud.io/)


## GitHub Actions
* Build and Test Java with Maven (branch master)
* Analyze SonarCloud (branch master)
* Publish on GitHub Packages (tag/release)


## 🛠 Add dependency
To include this dependency in you project, you have to do three things.

1. Add as dependency in your `pom.xml`:
```xml
<dependency>
	<groupId>com.bvilela.lib</groupId>
	<artifactId>java-util-validation</artifactId>
	<version>0.0.1</version>
</dependency>
```

2. Add the GitHub repository in your `pom.xml`:
```xml
<repositories>
	<repository>
		<id>github</id>
		<name>GitHub</name>
		<url>https://maven.pkg.github.com/bvilela/java-util-validation-lib</url>
		<releases>
			<enabled>true</enabled>
		</releases>
		<snapshots>
			<enabled>true</enabled>
		</snapshots>
	</repository>
</repositories>
```

3. Add the authentication to the Package Registry in your global `settings.xml`: `USER_HOME\.m2\settings.xml`
```xml
<servers>
    <server>
        <id>github</id>
        <username>YOUR_USERNAME</username>
        <password>YOUR_AUTH_TOKEN</password>
    </server>
</servers>
```
Replace the `YOUR_USERNAME` with your GitHub login name.

Replace the `YOUR_AUTH_TOKEN` with a generated GitHub Personal Access Token (PAT):

> *GitHub > Settings > Developer Settings > Personal access tokens > Generate new token*:
>
> The token needs at least the `read:packages` scope.

:exclamation: Otherwise you will get a Not authorized exception.

[⬆ Voltar ao topo](#java-util-validation-lib)<br>
