// package com.ecommerce.rema_baby_crochet;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.test.web.server.LocalServerPort;
// import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
// import org.testcontainers.containers.PostgreSQLContainer;
// import org.testcontainers.junit.jupiter.Container;
// import org.testcontainers.junit.jupiter.Testcontainers;

// import io.restassured.RestAssured;





// @Testcontainers
// @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// class ProductControllerIT {

//     @Container
//     @ServiceConnection // Spring Boot 3.1+ : configure automatiquement le datasource du test
//     static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

//     @LocalServerPort
//     private int port;

//     @BeforeEach
//     void setUp() {
//         RestAssured.baseURI = "http://localhost:" + port;
//     }

//     @Test
//     void shouldCreateAndRetrieveProduct() {
//         String createPayload = """
//                 {
//                   "name": "Webcam 1080p",
//                   "description": "Webcam USB avec micro intégré",
//                   "price": 39.90,
//                   "stockQuantity": 25,
//                   "imageUrl": "https://picsum.photos/seed/webcam/600/400",
//                   "status": "ACTIVE",
//                   "categoryId": 1
//                 }
//                 """;

//         String productId = given()
//                 .contentType(ContentType.JSON)
//                 .body(createPayload)
//                 .when()
//                 .post("/api/v1/products")
//                 .then()
//                 .statusCode(201)
//                 .body("name", equalTo("Webcam 1080p"))
//                 .body("status", equalTo("ACTIVE"))
//                 .extract().path("id");

//         given()
//                 .when()
//                 .get("/api/v1/products/{id}", productId)
//                 .then()
//                 .statusCode(200)
//                 .body("id", equalTo(productId))
//                 .body("price", equalTo(39.90f));
//     }

//     @Test
//     void shouldReturn404ForUnknownProduct() {
//         given()
//                 .when()
//                 .get("/api/v1/products/{id}", "00000000-0000-0000-0000-000000000000")
//                 .then()
//                 .statusCode(404)
//                 .body("error", equalTo("Not Found"));
//     }

//     @Test
//     void shouldReturn400WhenPriceIsMissing() {
//         String invalidPayload = """
//                 { "name": "Produit sans prix", "stockQuantity": 5, "status": "ACTIVE", "categoryId": 1 }
//                 """;

//         given()
//                 .contentType(ContentType.JSON)
//                 .body(invalidPayload)
//                 .when()
//                 .post("/api/v1/products")
//                 .then()
//                 .statusCode(400)
//                 .body("fieldErrors.field", hasItem("price"));
//     }
// }
