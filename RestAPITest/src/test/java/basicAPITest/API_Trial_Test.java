package basicAPITest;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.testng.Assert;

import files.Payloads;

public class API_Trial_Test {

	public static void main(String[] args) {

		// POST Method
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		String response = given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
				.body(Payloads.AddPlace()).when().post("maps/api/place/add/json").then().assertThat().statusCode(200)
				.body("scope", equalTo("APP")).header("Server", "Apache/2.4.52 (Ubuntu)").extract().response()
				.asString();

		System.out.println(response);
		JsonPath js = new JsonPath(response);
		String placeID = js.getString("place_id");
		System.out.println("Place ID: " + placeID);

		// GET Method
		String getResponse = given().log().all().queryParam("key", "qaclick123").queryParam("place_id", placeID).when()
				.get("maps/api/place/get/json").then().assertThat().log().all().statusCode(200).extract().response()
				.asString();

		System.out.println("Get Response: " + getResponse);
		JsonPath js1 = new JsonPath(getResponse);
		String Address1 = js1.getString("address");
		System.out.println("Old Address: "+Address1);

		// PUT Method
		given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
				.body(Payloads.UpdatePlace(placeID)).when().put("maps/api/place/update/json").then().assertThat().log()
				.all().statusCode(200).body("msg", equalTo("Address successfully updated"));

		// GET Method
		String getResponse1 = given().log().all().queryParam("key", "qaclick123").queryParam("place_id", placeID).when()
				.get("maps/api/place/get/json").then().assertThat().log().all().statusCode(200).extract().response()
				.asString();

		System.out.println("Get Response: " + getResponse1);
		JsonPath js2 = new JsonPath(getResponse1);
		String Address2 = js2.getString("address");
		System.out.println("New Address: "+Address2);
		
		Assert.assertNotEquals(Address1, Address2);

	}

}
