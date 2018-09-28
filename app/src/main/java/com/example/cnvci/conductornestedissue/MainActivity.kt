package com.example.cnvci.conductornestedissue

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction

class MainActivity : AppCompatActivity() {

    private lateinit var router: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        router = Conductor.attachRouter(this, findViewById(R.id.controllerContainer), savedInstanceState)
        if (!router.hasRootController()) {
            router.setRoot(RouterTransaction.with(FirstController()))
        }
    }

    override fun onBackPressed() {
        if (!router.handleBack()) super.onBackPressed()
    }
}
