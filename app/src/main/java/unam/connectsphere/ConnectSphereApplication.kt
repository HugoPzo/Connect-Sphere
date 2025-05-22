package unam.connectsphere

import android.app.Application
import com.google.firebase.FirebaseApp
import unam.connectsphere.ui.addcontact.AddContactScreen


class ConnectSphereApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}
