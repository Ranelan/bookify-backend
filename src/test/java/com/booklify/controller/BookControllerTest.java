package com.booklify.controller;


import com.booklify.domain.Book;
import com.booklify.domain.enums.BookCondition;
import com.booklify.factory.BookFactory;
import com.booklify.dto.BookDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookControllerTest {

        @Autowired
        private TestRestTemplate restTemplate;

        static String testEmail = "testuser_" + System.currentTimeMillis() + "@example.com";
        static BookDto book1;
        static BookDto book2;
        static com.booklify.domain.RegularUser user;

        static final String BASE_URL = "/api/book";

        private static class LoginRequest {
            private String email;
            private String password;

            public LoginRequest(String email, String password) {
                this.email = email;
                this.password = password;
            }

            // Getters (needed for JSON serialization)
            public String getEmail() { return email; }
            public String getPassword() { return password; }
        }

        private static class AuthResponse {
            private String token;
            private Object user; // Add this to match backend LoginResponse

            public String getToken() { return token; }
            public void setToken(String token) { this.token = token; }
            public Object getUser() { return user; }
            public void setUser(Object user) { this.user = user; }

            @Override
            public String toString() {
                return "AuthResponse{" +
                        "token='" + token + '\'' +
                        ", user=" + user +
                        '}';
            }
        }

        private String getJwtToken() {
            String authUrl = "/api/regular-user/login";
            LoginRequest request = new LoginRequest(testEmail, "Password123!");

            // Print login request
            System.out.println("[TEST] Login request: email=" + testEmail + ", password=Password123!");
            ResponseEntity<AuthResponse> response = restTemplate.postForEntity(
                    authUrl,
                    request,
                    AuthResponse.class
            );
            System.out.println("[TEST] Login response status: " + response.getStatusCode());
            System.out.println("[TEST] Login response body: " + response.getBody());

            if (response.getBody() == null || response.getBody().getToken() == null) {
                throw new IllegalStateException("Login failed: Invalid email or password. Response body was null. Check that the user is registered and the password matches.");
            }

            return response.getBody().getToken();
        }
        // ======== END AUTHENTICATION HELPERS ========

        @BeforeAll
        void setup() {
            // Use a strong password that passes validation
            String rawPassword = "Password123!";
            com.booklify.dto.RegularUserDto userDto = new com.booklify.dto.RegularUserDto();
            userDto.setFullName("Test User");
            userDto.setEmail(testEmail);
            userDto.setPassword(rawPassword);
            userDto.setDateJoined(java.time.LocalDateTime.now());
            userDto.setSellerRating(4.5);
            userDto.setBio("Test bio");
            userDto.setLastLogin(java.time.LocalDateTime.now());

            // Register user via API (use /create and DTO)
            String createUrl = "/api/regular-user/create";
            ResponseEntity<com.booklify.dto.RegularUserDto> regResponse = restTemplate.postForEntity(createUrl, userDto, com.booklify.dto.RegularUserDto.class);
            System.out.println("[TEST] User creation response status: " + regResponse.getStatusCode());
            System.out.println("[TEST] User creation response body: " + regResponse.getBody());
            System.out.println("[TEST] User creation email: " + userDto.getEmail());

            // Fetch the persisted user (with ID) from backend
            String getByEmailUrl = "/api/regular-user/getByEmail/" + testEmail;
            ResponseEntity<com.booklify.dto.RegularUserDto> getUserResponse = restTemplate.getForEntity(getByEmailUrl, com.booklify.dto.RegularUserDto.class);
            com.booklify.dto.RegularUserDto persistedUserDto = getUserResponse.getBody();
            if (persistedUserDto == null || persistedUserDto.getId() == null) {
                throw new IllegalStateException("Failed to fetch persisted user after creation");
            }
            // Map DTO to entity for BookFactory
            user = new com.booklify.domain.RegularUser.RegularUserBuilder()
                    .setId(persistedUserDto.getId())
                    .setFullName(persistedUserDto.getFullName())
                    .setEmail(persistedUserDto.getEmail())
                    .setPassword(persistedUserDto.getPassword())
                    .setDateJoined(persistedUserDto.getDateJoined())
                    .setSellerRating(persistedUserDto.getSellerRating())
                    .setBio(persistedUserDto.getBio())
                    .setLastLogin(persistedUserDto.getLastLogin())
                    .build();

            // Now create books with this user
            byte[] testImage = loadTestImage();
            book1 = new BookDto(null, "9780061122415", "The Alchemist", "Paulo Coelho",
                    "HarperOne", BookCondition.EXCELLENT, 180.0,
                    "Fiction classic about destiny.", java.time.LocalDateTime.now(), testImage);
            book1.setUserId(user.getId());
            book2 = new BookDto(null, "9780451524935", "1984", "George Orwell",
                    "Secker & Warburg", BookCondition.ACCEPTABLE, 150.0,
                    "Dystopian novel set in a totalitarian regime.", java.time.LocalDateTime.now(), testImage);
            book2.setUserId(user.getId());
        }

        @Test
        @Order(1)
        void create() throws IOException {
            String url = BASE_URL + "/create";

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("bookRequest", book1);
            body.add("imageFile", new ClassPathResource("test-image.jpg"));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            headers.setBearerAuth(getJwtToken().replace("Bearer ", "")); // Add JWT token

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            ResponseEntity<BookDto> response = restTemplate.postForEntity(url, requestEntity, BookDto.class);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertNotNull(response.getBody().getBookID());
            assertEquals(book1.getUserId(), response.getBody().getUserId());
            assertEquals(book1.getTitle(), response.getBody().getTitle());

            // Update book1 with returned BookDto to get its generated ID for further tests
            book1 = response.getBody();

            System.out.println("Created book: " + book1);
        }

        @Test
        @Order(2)
        void read() {
            String url = BASE_URL + "/read/" + book1.getBookID();

            ResponseEntity<BookDto> response = restTemplate.getForEntity(url, BookDto.class);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(book1.getBookID(), response.getBody().getBookID());
            assertEquals(book1.getUserId(), response.getBody().getUserId());
            assertEquals(book1.getTitle(), response.getBody().getTitle());

            System.out.println("Read book: " + response.getBody());
        }

        @Test
        @Order(3)
        void update() throws IOException {
            String url = BASE_URL + "/update";
            book1.setTitle("The Alchemist (Updated)");
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("bookRequest", book1);
            body.add("imageFile", new ClassPathResource("test-image.jpg"));
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            headers.setBearerAuth(getJwtToken().replace("Bearer ", ""));
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
            ResponseEntity<BookDto> response = restTemplate.postForEntity(url, requestEntity, BookDto.class);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("The Alchemist (Updated)", response.getBody().getTitle());
            assertEquals(book1.getUserId(), response.getBody().getUserId());
            book1 = response.getBody();
            System.out.println("Updated book: " + book1);
        }

        @Test
        @Order(4)
        void getAll() {
            String url = BASE_URL + "/getAll";

            ResponseEntity<Book[]> response = restTemplate.getForEntity(url, Book[].class);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertTrue(response.getBody().length > 0);

            System.out.println("All books:");
            for (Book b : response.getBody()) {
                System.out.println(b);
            }
        }

        @Test
        @Order(5)
        void getBookImage() {
            String url = BASE_URL + "/image/" + book1.getBookID();

            ResponseEntity<byte[]> response = restTemplate.getForEntity(url, byte[].class);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertTrue(response.getBody().length > 0);

            System.out.println("Image bytes length: " + response.getBody().length);
        }

        @Test
        @Order(9)
        void delete() {
            String url = BASE_URL + "/delete/" + book1.getBookID();
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(getJwtToken().replace("Bearer ", ""));
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                    url,
                    HttpMethod.DELETE,
                    entity,
                    Void.class
            );
            assertTrue(
                    deleteResponse.getStatusCode() == HttpStatus.NO_CONTENT ||
                            deleteResponse.getStatusCode() == HttpStatus.OK,
                    "Delete should return 204 or 200"
            );
            // Try to read again
            ResponseEntity<BookDto> readResponse = restTemplate.exchange(
                    BASE_URL + "/read/" + book1.getBookID(),
                    HttpMethod.GET,
                    entity,
                    BookDto.class
            );
            if (readResponse.getStatusCode() == HttpStatus.NOT_FOUND) {
                assertEquals(HttpStatus.NOT_FOUND, readResponse.getStatusCode());
            } else {
                fail("Expected 404 NOT FOUND after deleting the book, but got: " + readResponse.getStatusCode());
            }
            System.out.println("Deleted book with ID: " + book1.getBookID());
        }

        @Test
        @Order(7)
        void findByIsbn() {
            // Save book1 first
            String createUrl = BASE_URL + "/create";
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("bookRequest", book1);
            body.add("imageFile", new ClassPathResource("test-image.jpg"));
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
            restTemplate.postForEntity(createUrl, requestEntity, Book.class);

            String url = BASE_URL + "/search/isbn?isbn=" + book1.getIsbn();
            // Add JWT token to headers
            String jwt = getJwtToken();
            HttpHeaders authHeaders = new HttpHeaders();
            authHeaders.setBearerAuth(jwt);
            HttpEntity<Void> entity = new HttpEntity<>(authHeaders);
            ResponseEntity<Book[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, Book[].class);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertTrue(response.getBody().length > 0);
            assertEquals(book1.getIsbn(), response.getBody()[0].getIsbn());
            System.out.println("Found by ISBN: " + response.getBody()[0]);
        }

        @Test
        @Order(8)
        void findByTitleContainingIgnoreCase() {
            // Save book1 first
            String createUrl = BASE_URL + "/create";
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("bookRequest", book1);
            body.add("imageFile", new ClassPathResource("test-image.jpg"));
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
            restTemplate.postForEntity(createUrl, requestEntity, Book.class);

            String url = BASE_URL + "/search/title?query=alchemist";
            // Add JWT token to headers
            String jwt = getJwtToken();
            HttpHeaders authHeaders = new HttpHeaders();
            authHeaders.setBearerAuth(jwt);
            HttpEntity<Void> entity = new HttpEntity<>(authHeaders);
            ResponseEntity<Book[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, Book[].class);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertTrue(response.getBody().length > 0);
            System.out.println("Found by title (ignore case): " + response.getBody().length);
        }

//        @Test
//        @Order(6)
//        void findByAuthor() {
//            // Save book2 first
//            String createUrl = BASE_URL + "/create";
//            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
//            body.add("bookRequest", book2);
//            // Use the correct test image extension
//            body.add("imageFile", new ClassPathResource("test-image.jpg"));
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
//            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
//            restTemplate.postForEntity(createUrl, requestEntity, Book.class);
//
//            String url = BASE_URL + "/search/author?author=" + book2.getAuthor();
//            // Add JWT token to headers
//            String jwt = getJwtToken();
//            HttpHeaders authHeaders = new HttpHeaders();
//            authHeaders.setBearerAuth(jwt);
//            HttpEntity<Void> entity = new HttpEntity<>(authHeaders);
//
//            assertEquals(HttpStatus.OK, response.getStatusCode());
//            assertNotNull(response.getBody());
//            assertTrue(response.getBody().length > 0);
//            System.out.println("Found by author: " + response.getBody().length);
//        }


        private static byte[] loadTestImage() {
            try {
                ClassPathResource resource = new ClassPathResource("test-image.jpg");
                return resource.getInputStream().readAllBytes();
            } catch (IOException ex) {
                System.out.println("Image read error: " + ex.getMessage());
                return new byte[]{1, 2, 3};
            }
        }
    }


