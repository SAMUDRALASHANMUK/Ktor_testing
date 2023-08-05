import com.example.User
import com.example.UserRepository.users
import com.example.module
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals


class ApplicationTest {

    @Before
    fun setup() {
        users.clear()
    }

    @Test
    fun `test simple GET should return Hello world`() = testApplication {
        application {
            module()
        }
        val response = client.get("/users/all")
        assertEquals(HttpStatusCode.OK, response.status)
    }


    @Test
    fun `test POST should add a new user`() = testApplication {
        application {
            module()
        }
        val user1 = User(1, "Jet", "Brains")
        val serializedUser = Json.encodeToString(user1)
        val response = client.post("/users/") {
            headers[HttpHeaders.ContentType] = ContentType.Application.Json.toString()
            setBody(serializedUser)
        }
        assertEquals(HttpStatusCode.Created, response.status)
        val responseUser = Json.decodeFromString<User>(response.bodyAsText())
        assertEquals(user1, responseUser)
    }


    @Test
    fun `test GET all should return list of users`() = testApplication {
        application {
            module()
        }
        val user1 = User(1, "John", "john@example.com")
        users.add(user1)
        val response = client.get("/users/all") {
            headers[HttpHeaders.ContentType] = ContentType.Application.Json.toString()
        }
        assertEquals(HttpStatusCode.OK, response.status)
        println("Response Content: ${response.bodyAsText()}")
        val userList = Json.decodeFromString<List<User>>((response.bodyAsText() ?: "").toString())
        assertEquals(users, userList)
    }

    @Test
    fun `test DELETE should remove the user`() = testApplication {
        application {
            module()
        }

        val user = User(1, "John", "john@example.com")
        users.add(user)
        val response = client.delete("/users/1") {
            headers[HttpHeaders.ContentType] = ContentType.Application.Json.toString()
        }
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(0, users.size)
    }

    @Test
    fun `test PUT should update the user`() = testApplication {
        application {
            module()
        }
        val user = User(1, "John", "john@example.com")
        users.add(user)
        val updatedUser = User(1, "Updated John", "updatedjohn@example.com")
        val serializedUser = Json.encodeToString(updatedUser)
        val response = client.put("users/1") {
            headers[HttpHeaders.ContentType] = ContentType.Application.Json.toString()
            setBody(serializedUser)
        }
        assertEquals(HttpStatusCode.OK, response.status)
        val updatedUserInList = users.find { it.user_id == updatedUser.user_id }
        assertEquals(updatedUser, updatedUserInList)
    }
}


