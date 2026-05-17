package com.example.checkin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.checkin.model.AttendanceModel
import com.example.checkin.model.AttendanceStatus
import com.example.checkin.model.UserModel
import com.example.checkin.network.UserApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AttendanceViewModel(private val service: UserApiService) : ViewModel() {

    private val _users = MutableStateFlow<List<UserModel>>(emptyList())
    val users: StateFlow<List<UserModel>> = _users

    private val _attendances = MutableStateFlow<Map<Int, AttendanceModel>>(emptyMap())
    val attendances: StateFlow<Map<Int, AttendanceModel>> = _attendances

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _currentRole = MutableStateFlow("")
    val currentRole: StateFlow<String> = _currentRole

    private val _currentUser = MutableStateFlow<UserModel?>(null)
    val currentUser: StateFlow<UserModel?> = _currentUser

    init {
        loadUsers()
    }

    fun loadUsers() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = service.getUsers()
                _users.value = response.users
                val initialAttendances = response.users.associate { user ->
                    user.id to AttendanceModel(userId = user.id)
                }
                _attendances.value = initialAttendances
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun markAttendance(userId: Int, status: AttendanceStatus) {
        val time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
        val updated = _attendances.value.toMutableMap()
        updated[userId] = AttendanceModel(
            userId = userId,
            status = status,
            timestamp = time
        )
        _attendances.value = updated
    }

    fun getStats(): Map<AttendanceStatus, Int> {
        return AttendanceStatus.values().associateWith { status ->
            _attendances.value.values.count { it.status == status }
        }
    }

    fun getFilteredUsers(): List<UserModel> {
        val query = _searchQuery.value
        return if (query.isEmpty()) _users.value
        else _users.value.filter {
            "${it.firstName} ${it.lastName}".contains(query, ignoreCase = true) ||
                    it.company.department.contains(query, ignoreCase = true)
        }
    }

    fun setSearchQuery(query: String) { _searchQuery.value = query }
    fun setRole(role: String) { _currentRole.value = role }
    fun setCurrentUser(user: UserModel) { _currentUser.value = user }
}

class AttendanceViewModelFactory(private val service: UserApiService) :
    androidx.lifecycle.ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AttendanceViewModel(service) as T
    }
}