# GitHub API Integration

This Spring Boot application interacts with the GitHub API to fetch user repositories and their branches.

## Prerequisites

- Java 11 or higher
- Maven 3.6.0 or higher
- A GitHub personal access token

## Getting Started

### Clone the Repository

```sh
git clone https://github.com/yourusername/github-api-integration.git
cd github-api-integration
```

### Configuration

#### GitHub Token

You need to store your GitHub personal access token in the `application.properties` file. This token is required to authenticate requests to the GitHub API.

1. Open the `src/main/resources/application.properties` file.
2. Add the following line with your GitHub token:

```properties
github.token=your_GitHub_personal_access_token
```

Alternatively, you can set the token as an environment variable:

```sh
export GITHUB_TOKEN=your_github_personal_access_token
```

And then reference it in the `application.properties` file:

```properties
github.token=${GITHUB_TOKEN}
```

### Build and Run the Application

To build and run the application, use the following commands:

```sh
mvn clean install
mvn spring-boot:run
```

### Running Tests

To run the tests, use the following command:

```sh
mvn test
```

## API Endpoints

### Get User Repositories

- **URL:** `/api/github/users/{username}/repos`
- **Method:** `GET`
- **Description:** Fetches the repositories of a specified GitHub user.
- **Response:**
    - `200 OK`: Returns a list of repositories.
    - `404 Not Found`: If the user is not found.

#### Example Request

```sh
curl -X GET "http://localhost:8080/api/github/users/sirzielarz/repos"
```

#### Example Response

```json
[
  {
    "name": "Hello-World",
    "ownerLogin": "sirzielarz",
    "branches": [
      {
        "name": "main",
        "lastCommitSha": "2d1b37b..."
      }
    ]
  }
]
```