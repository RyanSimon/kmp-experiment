package co.touchlab.kampstarter

import co.touchlab.kampstarter.feature.business.data.BusinessRepositoryImpl
import co.touchlab.kampstarter.feature.business.data.network.BusinessMapper
import co.touchlab.kampstarter.feature.business.data.network.BusinessReviewMapper
import co.touchlab.kampstarter.feature.business.data.network.BusinessesApi
import co.touchlab.kampstarter.feature.business.data.network.BusinessesApiImpl
import co.touchlab.kampstarter.feature.business.domain.BusinessRepository
import co.touchlab.kampstarter.ktor.DogApiImpl
import co.touchlab.kampstarter.ktor.KtorApi
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun initKoin(appDeclaration: KoinAppDeclaration = {}) = startKoin {
    appDeclaration()
    modules(platformModule, coreModule)
}

val coreModule = module {
    single { DatabaseHelper(get()) }
    single<KtorApi> { DogApiImpl() }
    single<BusinessesApi> { BusinessesApiImpl("dcT2iVEoMXYmRZb1NvNb71zH-Y6bXtLIo8EqFiT7qmR1BHHCOjbw5Zl4h0te6DXK2P-IrJha_PQWY6tyMvjtGaW_9BlCY-t4EDSHLz3xSIVksBhDsF1hNgeb1CYcW3Yx") }
    factory { BusinessReviewMapper() }
    factory { BusinessMapper() }
    // TODO platform module updated with InternetConnectionHandler
    factory<BusinessRepository> { BusinessRepositoryImpl(get(), object : InternetConnectionHandler { override fun isConnected() = true }, get(), get()) }
}

expect val platformModule: Module