package com.example.prueba;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class FacturaArca {

    public void obtenerCredencial(String tokenrta, String signrta) {
        try {
            //Primero creamos para mandar solicitudes a la api de arca
            HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(15)).build();

            //Ahora todo lo que vamos a recibir de información
            Map<String, String> authRequestMap = new HashMap<>();
            authRequestMap.put("environment", "dev"); //dev = prueba, despues ponemos prod si quieren
            authRequestMap.put("tax_id", "x");  //CUIT
            authRequestMap.put("wsid", "wsfe"); //wsfe es la facturación electrónica

            //Para manejar Json, el profe se muere
            Gson gson = new Gson();
            //Para convertir los Maps que hicimos antes en un Json
            String authRequestBody = gson.toJson(authRequestMap);

            //Solicitud HTTP POST para enviarle los datitos
            HttpRequest authRequest = HttpRequest.newBuilder()
                    .uri(new URI("https://app.afipsdk.com/api/v1/afip/auth"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(authRequestBody))
                    .build();

            //Mandamos la soli
            HttpResponse<String> authResponse = client.send(authRequest, HttpResponse.BodyHandlers.ofString());
            if (authResponse.statusCode() >= 400) { //Los errores van de 400 para arriba, por eso use el >=400 directamente
                System.out.println("Error en la solicitud de autenticación: " + authResponse.body());
                return;
            }

            //Ahora parseamos para pasar la información al Map
            Type mapType = new TypeToken<Map<String, String>>() {
            }.getType();
            Map<String, String> authData = gson.fromJson(authResponse.body(), mapType);
            tokenrta = authData.get("token");
            signrta = authData.get("sign");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}
