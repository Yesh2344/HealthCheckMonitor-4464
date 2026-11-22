# HealthCheckMonitor

[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Java](https://img.shields.io/badge/java-17%2B-orange.svg)](https://openjdk.org/projects/jdk/17/)
[![Spring Boot](https://img.shields.io/badge/spring%20boot-3.2.0-green.svg)](https://spring.io/projects/spring-boot)

HealthCheckMonitor is a lightweight, production‑ready service that periodically checks the health of configurable HTTP endpoints and exposes the aggregated status via a REST API.

## Table of Contents

- [Features](#features)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Configuration](#configuration)
- [Running the Application](#running-the-application)
- [API Documentation](#api-documentation)
- [Testing](#testing)
- [License](#license)

## Features

- Configurable list of health‑check URLs (via environment variables or `application.yml`)
- Periodic asynchronous checks using Java 11+ `HttpClient`
- Aggregated health status endpoint (`GET /health`)
- Structured logging with SLF4J
- Comprehensive unit tests with JUnit 5 & Mockito
- Built with Spring Boot 3 and Java 17

## Prerequisites

- JDK 17 or later
- Gradle 8.x (wrapper included)
- Git (optional)

## Installation

<!-- tiny readability tweak -->
