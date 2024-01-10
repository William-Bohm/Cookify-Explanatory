# JWT Service Directory

This directory outlines the implementation of our custom Java Web Token (JWT) service. Our JWT service is primarily built using Java Spring and MySQL. It provides user authentication and access management capabilities for our application.

## JWT Service Overview

JWT (JSON Web Token) is an open standard (RFC 7519) that defines a compact and self-contained way for securely transmitting information between parties as a JSON object. JWTs can be signed using a secret (with the HMAC algorithm) or a public/private key pair using RSA or ECDSA.

JWTs are used for:

1. **Authorization:** Once the user is logged in, each subsequent request will include the JWT, allowing the user to access routes, services, and resources that are permitted with that token.

2. **Information Exchange:** JWTs are a good way of securely transmitting information between parties because they can be signed, which means you can be sure that the senders are who they say they are.

## Current Implementation in Cookify

Our JWT service uses private and public keys to encrypt and decrypt JWT tokens. The JWT holds authorities that are based on the payment structure of the app, allowing certain endpoints for free, paid, premium, and admin users. Also, the userID and username are also claims of the JWT. This is done so that the UserDetailsService within Java Spring can automatically apply the userID and username when necessary.

The JWT service uses a MySQL database for storing user data securely. The MySQL database is encrypted to provide further protection of sensitive user data.

## Public Key Access 

Each Java Spring microservice has only the public-key.pem to decrypt the JWT. The public-key.pem files are empty in this repository due to security reasons.

## Why Custom JWT Service?

A custom JWT service allows us to tailor-fit our authorization and access control mechanisms to our application's specific needs. This flexibility enables us to implement custom rules and permissions based on our unique business logic and requirements. Further, integrating the JWT service with MySQL allows us to leverage MySQL's robust security and encryption capabilities to store and manage user data securely.
