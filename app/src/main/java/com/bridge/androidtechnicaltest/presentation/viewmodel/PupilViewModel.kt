package com.bridge.androidtechnicaltest.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bridge.androidtechnicaltest.data.model.Pupil
import com.bridge.androidtechnicaltest.data.model.Pupils
import com.bridge.androidtechnicaltest.domain.PupilRepository
import com.bridge.androidtechnicaltest.utils.ResultHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PupilViewModel(private val repository: PupilRepository) : ViewModel() {

    private var currentPage = 1
    private var isLastPage = false

    init {
        loadStudents()
    }

    private val _pupils = MutableStateFlow<ResultHandler<Pupils>>(ResultHandler.Loading)
    val pupils: StateFlow<ResultHandler<Pupils>> = _pupils.asStateFlow()

    private val _selectedPupil = MutableStateFlow<ResultHandler<Pupil>>(ResultHandler.Loading)
    val selectedPupil: StateFlow<ResultHandler<Pupil>> = _selectedPupil.asStateFlow()

    private val _editPupil = MutableStateFlow<Pupil?>(null)
    val editPupil: StateFlow<Pupil?> = _editPupil.asStateFlow()

    private val _createStudent = MutableStateFlow<ResultHandler<Pupil>>(ResultHandler.Loading)
    val createStudent: StateFlow<ResultHandler<Pupil>> = _createStudent.asStateFlow()

    private val _deleteStudent = MutableStateFlow<ResultHandler<Pupil>>(ResultHandler.Loading)
    val deleteStudent: StateFlow<ResultHandler<Pupil>> = _deleteStudent.asStateFlow()

    fun loadStudents(page: Int = currentPage) {
        viewModelScope.launch {
            try {
                val res = repository.getStudents(page)
                res.collectLatest {
                    if(it.items.isNotEmpty()) {
                        _pupils.value = ResultHandler.Success(it)
                        currentPage = page
                        isLastPage = it.pageNumber >= it.totalPages
                    } else {
                        _pupils.value = ResultHandler.Error(Exception("No students found"))
                        isLastPage = true
                    }
                }
            } catch (e: Exception) {
                _pupils.value = ResultHandler.Error(e)
            }
        }
    }

    fun loadNextPage() {
        _pupils.value = ResultHandler.Loading
        println(!isLastPage)

        if (!isLastPage) {
            loadStudents(currentPage + 1)
        }
    }

    fun loadPreviousPage() {
        _pupils.value = ResultHandler.Loading
        if (currentPage > 1) {
            loadStudents(currentPage - 1)
        }
    }

    fun getStudentDetails(id: Int) {
        viewModelScope.launch {
            try {
                val res = repository.getStudentById(id)
                res.collectLatest {
                    _selectedPupil.value = ResultHandler.Success(it)
                }
            } catch (e: Exception) {
                _selectedPupil.value = ResultHandler.Error(e);
            }
        }
    }

    fun createStudent(pupil: Pupil) {
        viewModelScope.launch {
            try {
                repository.createStudent(pupil)
                loadStudents()
            } catch (e: Exception) {
                
            }
        }
    }

    fun setEditPupil(pupil: Pupil) {
        _editPupil.value = pupil
        println(editPupil.value!!.pupilId!!.toString())
    }

    fun updateStudent(id: Int, pupil: Pupil) {
        viewModelScope.launch {
            try {
                repository.updateStudentById(id, pupil)
                loadStudents()
                getStudentDetails(pupil.pupilId!!)
            } catch (e: Exception) {
                _selectedPupil.value = ResultHandler.Error(e)
            }
        }
    }
}