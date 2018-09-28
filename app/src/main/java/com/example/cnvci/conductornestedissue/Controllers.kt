package com.example.cnvci.conductornestedissue

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.RouterTransaction

const val WORKAROUND = false
var globalFlag = false

abstract class BaseController : Controller() {
    lateinit var textView: TextView
    lateinit var button: Button
    lateinit var nestedContainer: ViewGroup

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.controller_layout, container, false).apply {
            textView = findViewById(R.id.textView)
            button = findViewById(R.id.button)
            nestedContainer = findViewById(R.id.container)
            if (WORKAROUND) {
                (this as ViewGroup).addView(View(context))
            }
            setup()
        }
    }

    abstract fun setup()
}

class FirstController : BaseController() {
    override fun setup() {
        textView.text = "I'm the First Controller"
        button.text = "Go to Second"
        button.setOnClickListener {
            router.pushController(RouterTransaction.with(SecondController()))
        }
    }
}

class SecondController : BaseController() {
    init {
        addLifecycleListener(object : LifecycleListener() {
            override fun postAttach(controller: Controller, view: View) {
                if (globalFlag) {
                    globalFlag = false
                    router.popCurrentController()
                }
            }
        })
    }

    override fun setup() {
        textView.text = "I'm the Second Controller"
        button.text = "Go to Third"
        button.setOnClickListener {
            router.pushController(RouterTransaction.with(ThirdController()))
        }
        val childRouter = getChildRouter(nestedContainer)
        if (!childRouter.hasRootController()) {
            childRouter.setRoot(RouterTransaction.with(ChildController()))
        }
    }
}

class ThirdController : BaseController() {
    override fun setup() {
        textView.text = "I'm the Third Controller"
        button.text = "Set the flag"
        button.setOnClickListener {
            globalFlag = true
            button.text = "Now try to go back"

        }
    }
}

class ChildController : BaseController() {
    override fun setup() {
        textView.text = "I'm the Child!!"
        button.visibility = View.GONE
    }
}