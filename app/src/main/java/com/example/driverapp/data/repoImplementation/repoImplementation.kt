package com.example.driverapp.data.repoImplementation

import com.example.driverapp.Common.BUSES_PATH
import com.example.driverapp.Common.DRIVERS_PATH
import com.example.driverapp.Common.ResultState
import com.example.driverapp.domain.models.BusModel
import com.example.driverapp.domain.models.DriverModel
import com.example.driverapp.domain.repo.Repo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class repoImplementation(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : Repo {
    private val busCollection = firestore.collection(BUSES_PATH)

    // ✅ Register or update a bus (one-time write)
    override  fun registerOrUpdateBus(bus: BusModel): Flow<ResultState<Boolean>> = flow {
        try {
            emit(ResultState.Loading)
            busCollection.document(bus.busId)
                .set(bus)
                .await()
            emit(ResultState.Success(true))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message.toString()))
        }
    }

    // ✅ Listen to driver’s bus data in real time
    override  fun getBusByDriverId(driverId: String): Flow<ResultState<BusModel>> = callbackFlow {
        trySend(ResultState.Loading)

        val query = busCollection.whereEqualTo("driverId", driverId)
        val listener = query.addSnapshotListener { snapshot, error ->
            if (error != null) {
                trySend(ResultState.Error(error.message.toString()))
                return@addSnapshotListener
            }

            if (snapshot != null && !snapshot.isEmpty) {
                val bus = snapshot.documents.first().toObject(BusModel::class.java)
                if (bus != null) {
                    trySend(ResultState.Success(bus))
                } else {
                    trySend(ResultState.Error("Bus data parsing error"))
                }
            } else {
                trySend(ResultState.Error("No bus found for this driver"))
            }
        }

        awaitClose { listener.remove() }
    }

    override fun getBusForCurrentDriver(): Flow<ResultState<BusModel>> = callbackFlow {
        trySend(ResultState.Loading)

        // 1️⃣ Get current logged-in driver ID
        val uid = auth.currentUser?.uid
        if (uid == null) {
            trySend(ResultState.Error("User not logged in"))
            close()
            return@callbackFlow
        }

        // 2️⃣ Listen to bus assigned to this driver
        val query = busCollection.whereEqualTo("driverId", uid)

        val listener = query.addSnapshotListener { snapshot, error ->
            if (error != null) {
                trySend(ResultState.Error(error.message.toString()))
                return@addSnapshotListener
            }

            if (snapshot != null && !snapshot.isEmpty) {
                val bus = snapshot.documents.first().toObject(BusModel::class.java)

                if (bus != null) {
                    trySend(ResultState.Success(bus))
                } else {
                    trySend(ResultState.Error("Bus data parsing failed"))
                }

            } else {
                trySend(ResultState.Error("No bus found for this driver"))
            }
        }

        awaitClose { listener.remove() }
    }

    override fun createBus(bus: BusModel): Flow<ResultState<Boolean>> = flow {
        try {
            emit(ResultState.Loading)

            // 🔹 1. Get current logged-in driver
            val uid = auth.currentUser?.uid ?: throw Exception("User not logged in")

            // 🔹 2. Fetch driver info
            val driverSnapshot = driverCollection.document(uid).get().await()
            val driver = driverSnapshot.toObject(DriverModel::class.java)
                ?: throw Exception("Driver profile not found")

            // 🔹 3. Auto-fill missing fields
            val finalBus = bus.copy(
                driverId = driver.driverId,
                driverName = driver.name
            )

            // 🔹 4. Save bus in Firestore
            busCollection.document(finalBus.busId)
                .set(finalBus)
                .await()

            emit(ResultState.Success(true))

        } catch (e: Exception) {
            emit(ResultState.Error(e.message.toString()))
        }
    }

    // ✅ Update bus location (one-time write)
    override  fun updateBusLocation(
        busId: String,
        latitude: Double,
        longitude: Double
    ): Flow<ResultState<Boolean>> = flow {
        try {
            emit(ResultState.Loading)
            busCollection.document(busId)
                .update(
                    mapOf(
                        "latitude" to latitude,
                        "longitude" to longitude,
                        "lastUpdated" to System.currentTimeMillis()
                    )
                )
                .await()
            emit(ResultState.Success(true))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message.toString()))
        }
    }


    private val driverCollection = firestore.collection(DRIVERS_PATH)

    // ✅ Register a new driver (Auth + Firestore)
    override  fun registerDriver(
        driver: DriverModel
    ): Flow<ResultState<Boolean>> = flow {
        try {
            emit(ResultState.Loading)

            val result = auth.createUserWithEmailAndPassword(driver.email, driver.password).await()
            val uid = result.user?.uid ?: throw Exception("Registration failed")

            val driverData = driver.copy(driverId = uid)
            driverCollection.document(uid).set(driverData).await()

            emit(ResultState.Success(true))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message.toString()))
        }
    }

    // ✅ Login existing driver and fetch profile
    override  fun loginDriver(
        email: String,
        password: String
    ): Flow<ResultState<DriverModel>> = flow {
        try {
            emit(ResultState.Loading)

            val result = auth.signInWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: throw Exception("Login failed")

            val snapshot = driverCollection.document(uid).get().await()
            val driver = snapshot.toObject(DriverModel::class.java)
                ?: throw Exception("Driver profile not found")

            emit(ResultState.Success(driver))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message.toString()))
        }
    }

    // ✅ Get current logged-in driver
    override  fun getCurrentDriver(): Flow<ResultState<DriverModel>> = flow {
        try {
            emit(ResultState.Loading)
            val uid = auth.currentUser?.uid ?: throw Exception("No user logged in")

            val snapshot = driverCollection.document(uid).get().await()
            val driver = snapshot.toObject(DriverModel::class.java)
                ?: throw Exception("Driver profile not found")

            emit(ResultState.Success(driver))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message.toString()))
        }
    }

    // ✅ Update info of  logged-in driver
    override fun updateDriverInfo(
        driverId: String,
        updatedDriver: DriverModel
    ): Flow<ResultState<Boolean>> = flow {
        try {
            emit(ResultState.Loading)
            driverCollection.document(driverId)
                .set(updatedDriver.copy(driverId = driverId)) // overwrite the old data
                .await()
            emit(ResultState.Success(true))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message.toString()))
        }
    }

    // ✅ Logout driver
    override  fun logoutDriver(): Flow<ResultState<Boolean>> = flow {
        try {
            emit(ResultState.Loading)
            auth.signOut()
            emit(ResultState.Success(true))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message.toString()))
        }
    }
}