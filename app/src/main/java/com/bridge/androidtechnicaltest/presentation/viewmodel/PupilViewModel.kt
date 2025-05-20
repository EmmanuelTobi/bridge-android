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

//    init {
//        loadPupils()
//    }

    private val _pupils = MutableStateFlow<ResultHandler<Pupils>>(ResultHandler.Loading)
    val pupils: StateFlow<ResultHandler<Pupils>> = _pupils.asStateFlow()

    private val _selectedPupil = MutableStateFlow<ResultHandler<Pupil>>(ResultHandler.Loading)
    val selectedPupil: StateFlow<ResultHandler<Pupil>> = _selectedPupil.asStateFlow()

    private val _editPupil = MutableStateFlow<Pupil?>(null)
    val editPupil: StateFlow<Pupil?> = _editPupil.asStateFlow()

    private val _createPupil = MutableStateFlow<ResultHandler<Pupil>?>(null)
    val createPupil: StateFlow<ResultHandler<Pupil>?> = _createPupil.asStateFlow()

    private val _updatePupil = MutableStateFlow<ResultHandler<Pupil>?>(null)
    val updatePupil: StateFlow<ResultHandler<Pupil>?> = _updatePupil.asStateFlow()

    private val _deletePupil = MutableStateFlow<ResultHandler<Pupil>?>(null)
    val deletePupil: StateFlow<ResultHandler<Pupil>?> = _deletePupil.asStateFlow()

    fun loadPupils(page: Int = currentPage, reload: Boolean = false) {

        if(reload) {
            _pupils.value = ResultHandler.Loading
        }

        viewModelScope.launch {
            try {
                val res = repository.getPupils(page)
                res.collectLatest {
                    if(it.items.isNotEmpty()) {
                        _pupils.value = ResultHandler.Success(it)
                        currentPage = page
                        isLastPage = it.pageNumber >= it.totalPages
                    } else {
                        _pupils.value = ResultHandler.Error(Exception("No Pupils found"))
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
            loadPupils(currentPage + 1)
        }
    }

    fun loadPreviousPage() {
        _pupils.value = ResultHandler.Loading
        if (currentPage > 1) {
            loadPupils(currentPage - 1)
        }
    }

    fun getPupilDetails(id: Int) {
        viewModelScope.launch {
            try {
                val res = repository.getPupilById(id)
                res.collectLatest {
                    _selectedPupil.value = ResultHandler.Success(it)
                }
            } catch (e: Exception) {
                _selectedPupil.value = ResultHandler.Error(e);
            }
        }
    }

    fun createPupil(pupil: Pupil) {
        viewModelScope.launch {
            try {
                _createPupil.value = ResultHandler.Loading
                val res = repository.createPupil(pupil)
                res.collectLatest {
                    _createPupil.value = ResultHandler.Success(it)
                }
                loadPupils()
            } catch (e: Exception) {
                println(e.message)
                _createPupil.value = ResultHandler.Error(e)
            }
        }
    }

    fun setEditPupil(pupil: Pupil) {
        _editPupil.value = pupil
        println(editPupil.value!!.pupilId!!.toString())
    }

    fun updatePupil(id: Int, pupil: Pupil) {
        viewModelScope.launch {
            try {
                _updatePupil.value = ResultHandler.Loading
                val res = repository.updatePupilById(id, pupil)
                res.collectLatest {
                    _updatePupil.value = ResultHandler.Success(it)
                    loadPupils()
                }
                getPupilDetails(pupil.pupilId!!)
            } catch (e: Exception) {
                _selectedPupil.value = ResultHandler.Error(e)
            }
        }
    }

    fun deletePupilById(id: Int) {
        viewModelScope.launch {
            try {
                _deletePupil.value = ResultHandler.Loading
                val res = repository.deletePupilById(id)
                res.collectLatest {
                    _deletePupil.value = ResultHandler.Success(it)
                    loadPupils(reload = true)
                }
            } catch (e: Exception) {
                _deletePupil.value = ResultHandler.Error(e)
            }
        }
    }
}