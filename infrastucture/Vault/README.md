# Hashicorp Vault Directory

This directory outlines the details of our HashiCorp Vault setup, an effective tool used to secure, store, and tightly control access to tokens, passwords, certificates, and encryption keys for protecting secrets and other sensitive data across distributed systems.

## HashiCorp Vault Overview

HashiCorp Vault is a secure secrets management tool that provides a unified interface to any secret, while providing tight access control and recording a detailed audit log. It's designed to handle leasing, key revocation, key rolling, and auditing, with a focus on security at every layer. Vault uses a client-server model where the server is a stand-alone application that stores and controls access to secrets and systems.

## Current Implementation in Cookify

Vault is currently part of our application deployment, primarily being used by Consul, our service mesh solution, for storing and managing sensitive information. While Vault is not yet fully integrated within our services, its presence in our deployment enables us to future-proof our architecture by setting the foundation for enhanced security features.

## Future Scope

The intended future scope of Vault in our EKS cluster includes:

1. **Secrets Management:** With Vault, we plan to implement rotating secrets, where secrets are automatically changed at regular intervals, reducing the risk of secrets being misused if compromised.

2. **Secure Introduction of Secrets:** Vault can provide a safe way to distribute secrets to our services in a controlled and auditable manner.

3. **Identity-Based Access:** With Vault, access to secrets is identity-based, adding another layer of security to our setup.

4. **Encryption as a Service:** Vault can be used to protect sensitive data both in transit and at rest in our databases and storage systems.

## Why HashiCorp Vault?

Implementing Vault and its rotating secrets feature means that our secrets (passwords, API keys, and tokens) are only valid for a certain period of time. If a secret is compromised, the attacker has a limited time window to exploit it, thus greatly reducing potential damage. Further, Vault’s audit log capability means we have a trail showing what secrets were accessed and when, making it easier to monitor and alert for inappropriate access. Lastly, integrating Vault with our EKS cluster aligns with the principle of least privilege, meaning applications and users will only have the minimum level of access required to accomplish their task, improving the overall security of our system.
