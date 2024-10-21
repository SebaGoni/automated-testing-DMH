package com.digitalMoneyHouseTest.AutomatedTests.sprints1_2_3_4;

import com.google.gson.JsonObject;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class Sprints1_2_3_4 {

    private final String urlUsers = "http://localhost:8084/users";
    private final String urlAccounts = "http://localhost:8084/accounts";
    private final String registerPath = "/register";
    private final String loginPath = "/login";
    private final String verifyPath = "/verify";
    private final String bearerToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzZWJhc3RpYW5nb25pQGdtYWlsLmNvbSIsImV4cCI6MTcyOTU5NTE5NiwidXNlcklkIjo4LCJpYXQiOjE3Mjk1MDg3OTZ9.7HqK_8bz0CRTj1AoYYYEtZe9khD3Mb4CA3QxNU_KQm4t-LFyICqelmApgfiG9Unye4-6GQHtYgL9C3EkYp9eHg";

    String userId = "/8";

    // ------------------------------------- SPRINT-1 ------------------------------------------------------

    // REGISTRAR EXITOSO DE USUARIO
    @Test
    public void register() {

        JsonObject request = new JsonObject();
        request.addProperty("firstName", "Sebastian");
        request.addProperty("lastName", "Goni");
        request.addProperty("dni", "22233222");
        request.addProperty("email", "sebastiangoni@gmail.com");
        request.addProperty("phone", "22365235652");
        request.addProperty("password", "Seba123");

        given()
                .contentType("application/json")
                .body(request)
                .post(urlUsers + registerPath)
                .then()
                .statusCode(200)
                .log().body();
    }


    //REGISTRAR USUARIO PREVIAMENTE REGISTRADO
    @Test
    public void badRegister() {

        JsonObject request = new JsonObject();
        request.addProperty("firstName", "Sebastian");
        request.addProperty("lastName", "Goni");
        request.addProperty("dni", "22233222");
        request.addProperty("email", "sebastiangoni@mail.com");
        request.addProperty("phone", "22365235652");
        request.addProperty("password", "Seba123");

        given()
                .contentType("application/json")
                .body(request)
                .post(urlUsers + registerPath)
                .then()
                .statusCode(409)
                .log().body();
    }

    //CONFIRMAR REGISTRO CON CODIGO ENVIADO POR EMAIL
    @Test
    public void confirmRegister() {

        JsonObject request = new JsonObject();
        request.addProperty("email", "sebastiangoni@gmail.com");
        request.addProperty("verificationCode", "754882");

        given()
                .contentType("application/json")
                .body(request)
                .post(urlUsers + verifyPath)
                .then()
                .statusCode(200)
                .log().body();
    }

    //LOGIN EXITOSO
    @Test
    public void login() {

        JsonObject request = new JsonObject();
        request.addProperty("email", "sebastiangoni@gmail.com");
        request.addProperty("password", "Seba123");

        given()
                .contentType("application/json")
                .body(request)
                .post(urlUsers + loginPath)
                .then()
                .statusCode(200)
                .log().body();
    }

    //LOGIN FALLIDO POR EMAIL INCORRECTO
    @Test
    public void badLogin() {

        JsonObject request = new JsonObject();
        request.addProperty("email", "usuarioinexistente@hotmail.com");
        request.addProperty("password", "Admin123");

        given()
                .contentType("application/json")
                .body(request)
                .post(urlUsers + loginPath)
                .then()
                .statusCode(404)
                .log().body();
    }


    //LOGOUT EXITOSO
    @Test
    public void logout() {

        String logoutPath = "/logout";

        given()
                .header("Authorization", "Bearer " + bearerToken)
                .post(urlUsers + logoutPath)
                .then()
                .statusCode(200)
                .log().body();
    }

    // ------------------------------------- SPRINT-2 ------------------------------------------------------

    // OBTENER INFORMACION DE UN USUARIO
    @Test
    public void getUserById() {

        given()
                .header("Authorization", "Bearer " + bearerToken)
                .get(urlUsers + userId)
                .then()
                .statusCode(200)
                .log().body();
    }

    //ACTUALIZAR ALIAS DE UN USUARIO
    @Test
    public void updateAlias() {

        String updateAliasPath = "/update/alias";

        JsonObject request = new JsonObject();
        request.addProperty("alias", "nuevo.alias.nuevo");

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + bearerToken)
                .body(request)
                .patch(urlUsers + updateAliasPath + userId)
                .then()
                .statusCode(200)
                .log().body();
    }

    //REGISTRAR UNA NUEVA TARJETA
    @Test
    public void registerCard() {

        String registerCardPath = "/8/cards";

        JsonObject request = new JsonObject();
        request.addProperty("number", "1234123412341231");
        request.addProperty("name", "Juan Gonzalez");
        request.addProperty("expiry", "2025-06-06");
        request.addProperty("cvc", "456");

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + bearerToken)
                .body(request)
                .post(urlAccounts + registerCardPath)
                .then()
                .statusCode(201)
                .log().body();
    }

    //OBTENER TARJETA POR ID
    @Test
    public void getCard() {

        String getCardPath = "/8/cards/6";

        given()
                .header("Authorization", "Bearer " + bearerToken)
                .get(urlAccounts + getCardPath)
                .then()
                .statusCode(200)
                .log().body();
    }

    //OBTENER TODAS LAS TARJETAS DE UNA MISMA CUENTA
    @Test
    public void getAllCards() {

        String cardsPath = "/8/cards";

        given()
                .header("Authorization", "Bearer " + bearerToken)
                .get(urlAccounts + cardsPath)
                .then()
                .statusCode(200)
                .log().body();
    }

    //ELIMINAR TARJETA POR ID
    @Test
    public void deleteCard() {

        String deleteCardPath = "/8/cards/6";

        given()
                .header("Authorization", "Bearer " + bearerToken)
                .delete(urlAccounts + deleteCardPath)
                .then()
                .statusCode(200)
                .log().body();
    }

    // ------------------------------------- SPRINT-3 ------------------------------------------------------

    //AGREGAR DINERO DESDE UNA TARJETA A LA CUENTA
    @Test
    public void addMoneyFromCard() {

        String depositPath = "/8/transferences/cards";

        JsonObject request = new JsonObject();
        request.addProperty("cardId", 4);
        request.addProperty("amount", 5000.0);

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + bearerToken)
                .body(request)
                .post(urlAccounts + depositPath)
                .then()
                .statusCode(201)
                .log().body();
    }

    //TRANSFERIR DINERO DE UNA CUENTA A OTRA
    @Test
    public void makeTransfer() {

        String transferPath = "/8/transferences/money";

        JsonObject request = new JsonObject();
        request.addProperty("recipient", "flor.mar.rio");
        request.addProperty("amount", 1000.0);

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + bearerToken)
                .body(request)
                .post(urlAccounts + transferPath)
                .then()
                .statusCode(201)
                .log().body();
    }

    // ------------------------------------- SPRINT-4 ------------------------------------------------------

    //OBTENER TODA LA ACTIVADAD DE LA CUENTA
    @Test
    public void getAllActivities() {

        String activityPath = "/8/activity";

        given()
                .header("Authorization", "Bearer " + bearerToken)
                .get(urlAccounts + activityPath)
                .then()
                .statusCode(200)
                .log().body();
    }

    //OBTENER LAS ULTIMAS 5 CUENTAS UTILIZADAS PARA TRANSFERIR O DEPOSITAR
    @Test
    public void getLastTransferredAccounts() {

        String lastTransferredAccountsPath = "/8/transferences/last-transferred-accounts";

        given()
                .header("Authorization", "Bearer " + bearerToken)
                .get(urlAccounts + lastTransferredAccountsPath)
                .then()
                .statusCode(200)
                .log().body();
    }

}
