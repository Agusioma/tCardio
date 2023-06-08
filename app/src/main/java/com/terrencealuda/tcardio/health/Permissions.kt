package com.terrencealuda.tcardio.health

import com.google.android.libraries.healthdata.HealthDataClient
import com.google.android.libraries.healthdata.data.IntervalDataTypes
import com.google.android.libraries.healthdata.permission.AccessType
import com.google.android.libraries.healthdata.permission.Permission
import com.google.common.util.concurrent.ListenableFuture


class Permissions(private val healthDataClient: HealthDataClient?) {
    private val permissions: MutableSet<Permission> = HashSet()

    init {
        val stepsReadPermission = Permission.builder()
            .setDataType(IntervalDataTypes.STEPS)
            .setAccessType(AccessType.READ)
            .build()
        permissions.add(stepsReadPermission)
    }

    @get:Throws(PermissionsException::class)
    val grantedPermissions: ListenableFuture<Set<Permission>>
        get() {
            if (healthDataClient == null) {
                throw PermissionsException("health client is null")
            }
            return healthDataClient.getGrantedPermissions(permissions)
        }

    @Throws(PermissionsException::class)
    fun requestPermissions(): ListenableFuture<Set<Permission>> {
        if (healthDataClient == null) {
            throw PermissionsException("health client is null")
        }
        return healthDataClient.requestPermissions(permissions)
    }

    fun arePermissionsGranted(result: Set<Permission>?): Boolean {
        return result?.containsAll(permissions) ?: false
    }
}
