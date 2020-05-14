package in.regres;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.*;

import static io.restassured.RestAssured.given;


public class ApiTest {
    /**
     * Задание 2.1
     * Используя сервис https://reqres.in/ получить список пользователей со второй страницы.
     * Убедиться, что аватары пользователей совпадают
     */
    @Test()
    public void checkAvatarsTest() {

        Response response = given()
                .contentType("application/json")
                .when()
                .get("https://reqres.in/api/users?page=2")
                .then()
                .log().all()
                .extract()
                .response();
        JsonPath jsonResponse = response.jsonPath();
        // создаем и заполняем массив проверочными данными
        List<String> avatars = Arrays.asList("https://s3.amazonaws.com/uifaces/faces/twitter/follettkyle/128.jpg", "https://s3.amazonaws.com/uifaces/faces/twitter/araa3185/128.jpg", "https://s3.amazonaws.com/uifaces/faces/twitter/vivekprvr/128.jpg", "https://s3.amazonaws.com/uifaces/faces/twitter/russoedu/128.jpg", "https://s3.amazonaws.com/uifaces/faces/twitter/mrmoiree/128.jpg", "https://s3.amazonaws.com/uifaces/faces/twitter/hebertialmeida/128.jpg");

        // создаем, а затем заполняем массив данными из Json
        List<String> avatarsFromJson = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            String ava = jsonResponse.get("data[" + i + "].avatar").toString();
            avatarsFromJson.add(ava);
        }
        // проверяем что полученные и проверочные данные равны
        Assert.assertEquals(avatars, avatarsFromJson);
        // for(int i=0; i < 6 ; i++) {Assert.assertEquals(avatars.get(i), avatarsFromJson.get(i));}
    }

    /**
     * Задание 2.2.1
     * Используя сервис https://reqres.in/ протестировать регистрацию пользователя в системе.
     * Необходимо создание двух тестов на успешный логин и логин с ошибкой из-за не введённого пароля
     */
    // тест на успешний логин
    @Test
    public void userRegistrationSuccessful() {
        Map<String, String> data = new HashMap<>();
        data.put("email", "eve.holt@reqres.in");
        data.put("password", "pistol");
        Response response = given()
                .contentType("application/json")
                .body(data)
                .when()
                .post("https://reqres.in/api/register")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .response();
        JsonPath jsonResponse = response.jsonPath();
        System.out.println("id = " + jsonResponse.get("id").toString());
        System.out.println("token = " + jsonResponse.get("token").toString());
        Assert.assertEquals(jsonResponse.get("id").toString(), "4", "id is not valid");
        Assert.assertEquals(jsonResponse.get("token").toString(), "QpwL5tke4Pnpja7X4", "token is not valid");
    }

    /**
     * Задание 2.2.2 тест на неуспешный логин
     */
    @Test
    public void userRegistrationFailed() {
        Map<String, String> data = new HashMap<>();
        data.put("email", "sydney@fife");
        Response response = given()
                .contentType("application/json")
                .body(data)
                .when()
                .post("https://reqres.in/api/register")
                .then()
                .log().all()
                .statusCode(400)
                .extract()
                .response();
        JsonPath jsonResponse = response.jsonPath();
        System.out.println("error = " + jsonResponse.get("error").toString());

        Assert.assertEquals(jsonResponse.get("error").toString(), "Missing password", "id is not valid");
    }

    /**
     * Используя сервис https://reqres.in/ убедится что операция
     * LIST <RESOURCE> возвращает данные отсортированные по годам
     */
    @Test
    public void listResourceSorted() {
        Response response = given()
                .contentType("application/json")
                .when()
                .get("https://reqres.in/api/unknown")
                .then()
                .log().all()
                .extract()
                .response();
        JsonPath jsonResponse = response.jsonPath();
        int numOfYears = jsonResponse.get("data.year.size()");
        System.out.println("Количество объектов year в ответе = " + numOfYears);

        List<Integer> yearsFromJson = new ArrayList<>();
        for (int i = 0; i < numOfYears; i++) {
            int year = Integer.parseInt(jsonResponse.get("data[" + i + "].year").toString());
            yearsFromJson.add(year);
        }
        List<Integer> listSorted = new ArrayList<>(); // создаем новый массив
        listSorted.addAll(yearsFromJson); // заполняем новый массив данными из полученного Json
        Collections.sort(listSorted); // сортируем по возрастанию
        // проверяем, что отсортированный массив из Json и новый массив равны
        Assert.assertEquals(listSorted, yearsFromJson);
        // listSorted.forEach(System.out::println);
    }

}


