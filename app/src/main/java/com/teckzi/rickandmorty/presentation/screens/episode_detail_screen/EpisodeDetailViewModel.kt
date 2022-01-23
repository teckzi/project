package com.teckzi.rickandmorty.presentation.screens.episode_detail_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teckzi.rickandmorty.di.Injector
import com.teckzi.rickandmorty.domain.model.CharacterModel
import com.teckzi.rickandmorty.domain.model.EpisodeModel
import com.teckzi.rickandmorty.domain.use_cases.UseCases
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class EpisodeDetailViewModel @Inject constructor(
    private val useCases: UseCases
) : ViewModel() {

    private val _selectedEpisode: MutableStateFlow<EpisodeModel?> = MutableStateFlow(null)
    val selectedEpisode: StateFlow<EpisodeModel?> = _selectedEpisode
    private val _characterList: MutableStateFlow<List<CharacterModel?>> =
        MutableStateFlow(emptyList())
    val characterList: StateFlow<List<CharacterModel?>> = _characterList

    fun getEpisode(episodeId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _selectedEpisode.value =
                episodeId.let { useCases.getSelectedEpisodeUseCase(episodeId = episodeId) }

            val listOfCharacterIds = _selectedEpisode.value!!.characters
            _characterList.value = listOfCharacterIds.let {
                useCases.getCharacterListById(characterIdList = it)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Injector.clearEpisodeDetailComponent()
    }
}