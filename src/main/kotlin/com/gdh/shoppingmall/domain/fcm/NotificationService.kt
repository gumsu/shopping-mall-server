package com.gdh.shoppingmall.domain.fcm

import com.gdh.shoppingmall.domain.user.User
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service

/**
 * 푸시알림을 보내는 로직 작성
 *
 * 푸시알림을 보낼 때 필요한 메시지 객체를 생성한 후 사용자 디바이스 등록 토큰을 전달하면
 * 해당하는 사용자 1명에게만 알림을 보낼 수 있다.
 * FireBaseApp을 통해 FireBaseMessaging 인스턴스를 가져와 메시지를 전송한다.
 * SendToUser를 호출하면 해당하는 디바이스로 전송할 수 있다.
 */

@Service
class NotificationService {

    private val firebaseApp by lazy {
        val inputStream = ClassPathResource("shopping-mall-android-firebase-adminsdk-ycqck-65e5116c35.json").inputStream

        val options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(inputStream))
            .build()

        FirebaseApp.initializeApp(options)
    }

    fun sendToUser(user: User, title: String, content: String) =
        user.fcmToken?.let { token ->
            val message = Message.builder()
                .setToken(token)
                .putData("title", title)
                .putData("content", content)
                .build()

            FirebaseMessaging.getInstance(firebaseApp).send(message)
        }
}