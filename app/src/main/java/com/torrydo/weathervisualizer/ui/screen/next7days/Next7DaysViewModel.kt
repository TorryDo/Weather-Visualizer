package com.torrydo.weathervisualizer.ui.screen.next7days

import com.torrydo.weathervisualizer.common.base.BaseViewModel
import com.torrydo.weathervisualizer.domain.weather.WeatherInfo
import com.torrydo.weathervisualizer.domain.holder.WeatherInfoStateHolder
import com.torrydo.weathervisualizer.utils.Logger
import kotlinx.coroutines.flow.collectLatest
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

class Next7DaysViewModel(
    private val weatherInfoStateHolder: WeatherInfoStateHolder
) : BaseViewModel(), ContainerHost<WeatherInfo, Next7DaySideEffect> {

    override val container =
        container<WeatherInfo, Next7DaySideEffect>(weatherInfoStateHolder.value)

    init {
        ioScope {
            weatherInfoStateHolder.state.collectLatest {
                intent {
                    if(state != it) {
                        Logger.d("update due to difference")
                        reduce {
                            state.copy(
                                weatherPerDay = it.weatherPerDay,
                                currentWeather = it.currentWeather
                            )
                        }
                    }
                }
            }
        }
    }

    fun navigateBack() {
        intent {
            postSideEffect(Next7DaySideEffect.NavigateBack)
        }
    }

}