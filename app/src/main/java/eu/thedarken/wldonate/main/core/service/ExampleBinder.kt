package eu.thedarken.wldonate.main.core.service

import android.os.Binder

import javax.inject.Inject

@ExampleServiceComponent.Scope
class ExampleBinder @Inject
constructor(service: ExampleService) : Binder()
