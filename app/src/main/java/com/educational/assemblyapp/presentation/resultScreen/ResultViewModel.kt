package com.educational.assemblyapp.presentation.resultScreen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.educational.assemblyapp.domain.AssemblyRepository
import com.educational.assemblyapp.domain.StoryblocksRepository
import com.educational.assemblyapp.domain.entity.FrameRateTypeEnum
import com.educational.assemblyapp.domain.entity.Video
import com.educational.assemblyapp.domain.entity.VideoQualityTypeEnum
import com.educational.assemblyapp.domain.entity.VideoSearch
import com.educational.assemblyapp.presentation.common.launchWithErrorHandler
import com.educational.assemblyapp.presentation.resultScreen.ResultState.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ResultViewModel @Inject constructor(
    private val storyblocksRepository: StoryblocksRepository,
    private val assemblyRepository: AssemblyRepository
    ) : ViewModel() {

    private val repository: AssemblyRepository
    private val _videoState = MutableLiveData<ResultState>(AllIsOk())
    val videoState: LiveData<ResultState> = _videoState

    private var selectedItemId: String = ""
    private var selectedThumbnailUrl: String = ""

    init {
        repository = assemblyRepository
    }

    fun onButtonClicked(video: VideoSearch) {
        viewModelScope.launchWithErrorHandler(block = {
            selectedThumbnailUrl = video.thumbnailUrl
            selectedItemId = video.stockItemId
        })
    }

    fun onFinishingButtonClicked(video: Video) {
        var varVideo = video
        viewModelScope.launchWithErrorHandler(block = {
            _videoState.value = Loading()
            if (selectedItemId.isBlank())
                varVideo.stockItemId = "11851"
            else
                varVideo.stockItemId = selectedItemId

            if (selectedThumbnailUrl.isBlank())
                varVideo.previewUrl = "https://d2v9y0dukr6mq2.cloudfront.net/video/thumbnail/slow-motion-falling-money_-jjatxweb__S0000.jpg"
            else
                varVideo.previewUrl = selectedThumbnailUrl

            varVideo = repository.updateVideoByTitle(video = varVideo)
            val jobId = repository.assembleVideo(varVideo.title)
            Log.d("assembly", "started assembling $jobId")
            _videoState.value = Success(varVideo)
        })
    }
}

sealed class ResultState {
    class Loading() : ResultState()
    class Error(val throwable: Throwable) : ResultState()
    class Success(val video: Video) : ResultState()
    class AllIsOk(): ResultState()
}