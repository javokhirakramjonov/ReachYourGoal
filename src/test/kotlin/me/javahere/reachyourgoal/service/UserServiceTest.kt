package me.javahere.reachyourgoal.service

import com.auth0.jwt.interfaces.Claim
import com.auth0.jwt.interfaces.DecodedJWT
import kotlinx.coroutines.runBlocking
import me.javahere.reachyourgoal.mockUser
import me.javahere.reachyourgoal.randomString
import me.javahere.reachyourgoal.repository.UserRepository
import me.javahere.reachyourgoal.security.jwt.JwtService
import me.javahere.reachyourgoal.security.jwt.JwtService.Companion.JWT_KEY_PAIR_CONFIRM_REGISTER
import me.javahere.reachyourgoal.service.impl.UserServiceImpl
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.UUID
import kotlin.properties.Delegates

@SpringBootTest
class UserServiceTest {
    private var baseUrl = "http:localhost:8080"
    private var emailService by Delegates.notNull<EmailService>()
    private var passwordEncoder by Delegates.notNull<PasswordEncoder>()
    private var jwtService by Delegates.notNull<JwtService>()
    private var userRepository by Delegates.notNull<UserRepository>()
    private var userService by Delegates.notNull<UserService>()

    @BeforeEach
    fun setup() {
        userRepository = mock(UserRepository::class.java)
        passwordEncoder = mock(PasswordEncoder::class.java)
        jwtService = mock(JwtService::class.java)
        emailService = mock(EmailService::class.java)

        userService = UserServiceImpl(baseUrl, userRepository, passwordEncoder, jwtService, emailService)
    }

    @Test
    fun `test register with valid username`() {
        // TODO
    }

    @Test
    fun `test confirmRegister`() {
        runBlocking {
            val token = randomString()
            val decodedJWT = mock(DecodedJWT::class.java)
            val claim = mock(Claim::class.java)
            val mockId = UUID.randomUUID()
            val mockUser = mockUser().copy(id = mockId)

            Mockito.`when`(jwtService.decodeAccessToken(token)).thenReturn(decodedJWT)
            Mockito.`when`(decodedJWT.getClaim(JwtService.JWT_EXTRA_KEY_1)).thenReturn(claim)
            Mockito.`when`(claim.asString()).thenReturn(JWT_KEY_PAIR_CONFIRM_REGISTER)
            Mockito.`when`(decodedJWT.issuer).thenReturn(mockId.toString())
            Mockito.`when`(userRepository.getUserById(mockId)).thenReturn(mockUser)
            Mockito.`when`(userRepository.updateUser(mockUser.copy(isConfirmed = true))).thenReturn(mockUser.copy(isConfirmed = true))

            val foundUser = userService.confirmRegister(token)

            Assertions.assertTrue(foundUser.isConfirmed)
        }
    }

    @Test
    fun `test confirm new Email`() {
        runBlocking {
            val newEmail = randomString()
            val token = randomString()
            val decodedJWT = mock(DecodedJWT::class.java)
            val claim1 = mock(Claim::class.java)
            val claim2 = mock(Claim::class.java)
            val mockId = UUID.randomUUID()
            val mockUser = mockUser().copy(id = mockId)

            Mockito.`when`(jwtService.decodeAccessToken(token)).thenReturn(decodedJWT)
            Mockito.`when`(decodedJWT.getClaim(JwtService.JWT_EXTRA_KEY_1)).thenReturn(claim1)
            Mockito.`when`(decodedJWT.getClaim(JwtService.JWT_EXTRA_KEY_2)).thenReturn(claim2)
            Mockito.`when`(claim1.asString()).thenReturn(newEmail)
            Mockito.`when`(claim2.asString()).thenReturn(JwtService.JWT_KEY_PAIR_CONFIRM_NEW_EMAIL)
            Mockito.`when`(decodedJWT.issuer).thenReturn(mockId.toString())
            Mockito.`when`(userRepository.getUserById(mockId)).thenReturn(mockUser)
            Mockito.`when`(userRepository.updateUser(mockUser.copy(email = newEmail))).thenReturn(mockUser.copy(email = newEmail))

            val foundUser = userService.confirmNewEmail(token)

            Assertions.assertEquals(newEmail, foundUser.email)
        }
    }

    @Test
    fun `test find user by id`() {
        runBlocking {
            val mockId = UUID.randomUUID()
            val mockUser = mockUser().copy(id = mockId)

            Mockito.`when`(userRepository.getUserById(mockId)).thenReturn(mockUser)

            val foundUser = userService.findUserById(mockId)
            val expectedUser = mockUser.transform()

            Assertions.assertEquals(expectedUser.id, foundUser.id)
            Assertions.assertEquals(expectedUser.firstName, foundUser.firstName)
            Assertions.assertEquals(expectedUser.lastName, foundUser.lastName)
            Assertions.assertEquals(expectedUser.username, foundUser.username)
            Assertions.assertEquals(expectedUser.email, foundUser.email)
            Assertions.assertEquals(expectedUser.isConfirmed, foundUser.isConfirmed)
        }
    }

    @Test
    fun `test find user by email`() {
        runBlocking {
            val mockEmail = randomString()
            val mockId = UUID.randomUUID()
            val mockUser = mockUser().copy(id = mockId, email = mockEmail)

            Mockito.`when`(userRepository.getUserByEmail(mockEmail)).thenReturn(mockUser)

            val foundUser = userService.findUserByEmail(mockEmail)
            val expectedUser = mockUser.transform()

            Assertions.assertEquals(expectedUser.id, foundUser.id)
            Assertions.assertEquals(expectedUser.firstName, foundUser.firstName)
            Assertions.assertEquals(expectedUser.lastName, foundUser.lastName)
            Assertions.assertEquals(expectedUser.username, foundUser.username)
            Assertions.assertEquals(expectedUser.email, foundUser.email)
            Assertions.assertEquals(expectedUser.isConfirmed, foundUser.isConfirmed)
        }
    }

    @Test
    fun `test find user by username`() {
        runBlocking {
            val mockId = UUID.randomUUID()
            val mockUsername = randomString()
            val mockUser = mockUser().copy(id = mockId, username = mockUsername)

            Mockito.`when`(userRepository.getUserByUsername(mockUsername)).thenReturn(mockUser)

            val foundUser = userService.findUserByUsername(mockUsername)
            val expectedUser = mockUser.transform()

            Assertions.assertEquals(expectedUser.id, foundUser.id)
            Assertions.assertEquals(expectedUser.firstName, foundUser.firstName)
            Assertions.assertEquals(expectedUser.lastName, foundUser.lastName)
            Assertions.assertEquals(expectedUser.username, foundUser.username)
            Assertions.assertEquals(expectedUser.email, foundUser.email)
            Assertions.assertEquals(expectedUser.isConfirmed, foundUser.isConfirmed)
        }
    }

    @Test
    fun `test update user`() {
        runBlocking {
            val mockId = UUID.randomUUID()
            val mockUser = mockUser().copy(id = mockId)
            val newFirstName = randomString()
            val newLastName = randomString()

            Mockito.`when`(userRepository.getUserById(mockId)).thenReturn(mockUser)
            Mockito.`when`(
                userRepository.updateUser(mockUser.copy(firstname = newFirstName, lastname = newLastName)),
            ).thenReturn(mockUser.copy(firstname = newFirstName, lastname = newLastName))

            val updatedUser = userService.updateUser(mockId, newFirstName, newLastName)
            val expectedUser = mockUser.copy(firstname = newFirstName, lastname = newLastName).transform()

            Assertions.assertEquals(expectedUser.id, updatedUser.id)
            Assertions.assertEquals(expectedUser.firstName, updatedUser.firstName)
            Assertions.assertEquals(expectedUser.lastName, updatedUser.lastName)
            Assertions.assertEquals(expectedUser.username, updatedUser.username)
            Assertions.assertEquals(expectedUser.email, updatedUser.email)
            Assertions.assertEquals(expectedUser.isConfirmed, updatedUser.isConfirmed)
        }
    }

    @Test
    fun `test update email`() {
        // TODO
    }

    @Test
    fun `test delete user by id`() {
        runBlocking {
            val mockId = UUID.randomUUID()
            val mockUser = mockUser().copy(id = mockId)

            Mockito.`when`(userRepository.getUserById(mockId)).thenReturn(mockUser)

            userService.deleteUserById(mockId)

            Mockito.verify(userRepository).deleteUserById(mockId)
        }
    }
}
