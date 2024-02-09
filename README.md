# ReachYourGoal

## Let people reach their goals faster with their own or others' roadmap.

## Available endpoints are available in the following link: <br>
localhost : [swagger link for port: 8080](http://localhost:8080/swagger-ui.html)

## Used technologies/languages:
- Kotlin
- Bash
- PostgreSQL
- Coroutines and Flows
- Flyway
- Docker
- OpenAPI
- Spring Boot
- Spring Security
- Spring Reactive
- SMTP
- Thymeleaf
- Ktlint
- TestContainers
- Kotest
- Graalvm

## What you need to run it?
### env and docker.env files with following variables:
   - POSTGRES_HOST
   - POSTGRES_PORT
   - DATABASE_NAME
   - POSTGRES_USERNAME
   - POSTGRES_PASSWORD
   - SMTP_HOST
   - SMTP_PORT
   - EMAIL
   - EMAIL_PASSWORD
   - JWT_ACCESS_SECRET
   - JWT_REFRESH_SECRET
   - BASE_URL
   - TASK_FILE_PATH
   - RYG_PORT

## Scripts
1. When you add you local message, you can create useful objects using this command:
   ```sh
   ./local_keys_parser.sh
   ```

## Contributions
Contributions are welcome! Here's how you can contribute to this project:

1. **Clone the Repository:** Clone the repository to your local machine using the `git clone` command.
   ```sh
   git clone https://github.com/javokhirakramjonov/ReachYourGoal.git
   ```
2. Create a Branch: Create a new branch to work on your feature, enhancement, or bug fix.
   ```sh
   git checkout -b feature/your-feature
   ```
3. Make Changes: Make your desired changes to the codebase.
4. Test Your Changes: Ensure that your changes do not introduce any new issues and that existing functionality is not broken.
5. Commit Your Changes: Commit your changes with a descriptive commit message.
   ```sh
   git commit -am 'Add: Description of your changes'
   ```
6. Push Changes: Push your changes to your forked repository on GitHub.
   ```sh
   git push origin feature/your-feature
   ```
7. Submit a Pull Request: Once you have pushed your changes, submit a pull request to the original repository. Provide a clear and descriptive title for your pull request, and explain the changes you have made in the description.
8. Review: Participate in the discussion and address any feedback or concerns raised during the review process.
9. Celebrate: Your contribution has been merged! Thank you for your contribution to this project.