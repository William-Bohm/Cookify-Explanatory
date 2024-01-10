# Source Code

This directory contains the source code for the Cookify microservices. Each microservice is built using Java Spring, enabling efficient development, high performance, and scalable deployments.

## Overview

Cookify is a meal planning application aimed at simplifying the process of planning, preparing, and enjoying home-cooked meals. It utilizes a variety of technologies and architectural patterns to ensure a smooth and engaging user experience.

**Please Note:** Even though there are authority restrictions based on a paid and free account, payment integration has not been setup for the service. The exposure of this codebase is primarily for educational purposes, and it would be an insecure choice to have sensitive client data in such an exposed codebase.

## Microservices

Each microservice lives in its own subdirectory within the `/src` directory. The microservices include:

- `FullTextSearch` - Enables full-text search across the application.
- `NutritionService` - Provides detailed nutritional information for the meals.
- `PhotoService` - Handles photo uploads and retrieval for the meals.
- `RecipeService` - Manages meal recipes.
- `UserService` - Manages user profiles and preferences.
- `VideoStreamService` - Handles video streaming for cooking tutorials.
- `RecommendationService` - Provides meal recommendations based on user preferences and behavior.

## Communication

Each microservice operates independently and does not directly communicate with other microservices. However, two exceptions are the `UserService` and the JWT service located in the `/infrastructure` directory. At the sign-up stage, a user is created in both the MySQL database for authentication and authorization purposes, and the MongoDB to store their general information that does not pertain to login security.

## Microservice Directories

Each microservice directory contains a `README.md` file that details the specific function of the microservice, acknowledges its limitations, and outlines a plan for improvements to enhance security, efficiency, consistency, and fault tolerance.

Please refer to these README.md files for more specific details about each microservice.

## Future Improvements

While the current architecture of Cookify represents a robust and scalable solution, it's always under continuous improvement. Future iterations will aim to tackle known issues and improve the application's efficiency, security, and overall performance.

