# Password Manager (Educational)

> **⚠️ Educational project — not a real password manager.** This is a small teaching example of the Java AES API, kept deliberately tiny so it can be read in one sitting. It contains known security flaws, documented below on purpose. **Do not use it to store real passwords** — use an established manager like Bitwarden or KeePassXC.

## What it does

Console app: add site/password pairs, retrieve them. Passwords are AES-encrypted and held in memory while the program runs.

## Known flaws (these are the lesson)

| Flaw | Why it's a problem | Production-grade fix |
|---|---|---|
| Hardcoded key in source code | Anyone with the code can decrypt everything | Derive the key from a master password with PBKDF2 or Argon2 |
| `Cipher.getInstance("AES")` defaults to ECB mode | ECB leaks patterns — identical plaintexts produce identical ciphertexts | `AES/GCM/NoPadding` with a random IV per entry |
| No salt or IV | Same input always encrypts to the same output | Random salt + IV stored alongside the ciphertext |
| In-memory `HashMap` only | Nothing is saved — all entries are lost when the program exits | Encrypted vault file or SQLite |

Each flaw is also marked with a `FLAW:` comment at the exact line in [`PasswordManager.java`](src/PasswordManager/PasswordManager.java).

Want to fix one? See the open [good first issues](https://github.com/chaudhary-lakshay/Password-Manager/issues).

## Run it

```bash
git clone https://github.com/chaudhary-lakshay/Password-Manager.git
cd Password-Manager/src
javac PasswordManager/PasswordManager.java
java PasswordManager.PasswordManager
```

## More of my work

This repo is a learning artifact. For production-grade projects, see:

- [CarCatalog](https://github.com/chaudhary-lakshay/CarCatalog) — Spring Boot vehicle catalog & rental API: JWT auth, Flyway migrations, Stripe PaymentIntents
- [VitaLink](https://github.com/chaudhary-lakshay/vitalink) — remote patient monitoring backend: HL7 ADT, MQTT device ingest, live ECG streaming (TimescaleDB + STOMP), FHIR R4
- [VitaLink Android](https://github.com/chaudhary-lakshay/vitalink-android) — Jetpack Compose clinician app for the above

## License

MIT
