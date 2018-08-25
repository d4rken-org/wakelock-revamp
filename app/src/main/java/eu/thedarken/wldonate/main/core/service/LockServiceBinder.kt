package eu.thedarken.wldonate.main.core.service

import android.os.Binder

import javax.inject.Inject

@LockServiceComponent.Scope
class LockServiceBinder @Inject
constructor() : Binder()
