package com.bokchi.mysolelife.utils

import com.google.firebase.auth.FirebaseAuth

class FBAuth {

    companion object {
        private lateinit var auth: FirebaseAuth

        // 사용자 id값을 return해줌
        fun getUid() : String {
            auth =FirebaseAuth.getInstance()

            return auth.currentUser?.uid.toString()
        }
    }
}