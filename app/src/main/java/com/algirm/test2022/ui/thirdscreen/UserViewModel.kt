package com.algirm.test2022.ui.thirdscreen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.algirm.test2022.data.model.UserResponse
import com.algirm.test2022.domain.UserRepository
import com.algirm.test2022.util.AppConstants.Companion.QUERY_PAGE_SIZE
import com.algirm.test2022.util.DispatcherProvider
import com.algirm.test2022.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val dispatcher: DispatcherProvider
) : ViewModel() {

    val responseLiveData: MutableLiveData<Resource<UserResponse>> = MutableLiveData(Resource.Init())

    var page = 1
    var userResponse: UserResponse? = null

    init {
        getUsers()
    }

    fun getUsers() = viewModelScope.launch(dispatcher.main) {
        responseLiveData.postValue(Resource.Loading())
        val response = userRepository.getUsers(page, QUERY_PAGE_SIZE)
        this@UserViewModel.responseLiveData.postValue(handleResponse(response))
    }

    private fun handleResponse(response: Response<UserResponse>): Resource<UserResponse> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                page++
                if(userResponse == null) {
                    userResponse = resultResponse
                } else {
                    val oldArticles = userResponse?.data
                    val newArticles = resultResponse.data
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(userResponse ?: resultResponse)
            }
        }
        return Resource.Failure(Exception(response.message()), null)
    }

}