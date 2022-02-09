package com.example.mercedesnews.util

import com.example.mercedesnews.adapters.NewsAdapter
import com.example.mercedesnews.ui.NewsViewModel
import org.koin.androidx.viewmodel.compat.ScopeCompat.viewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.GlobalContext.get
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        NewsViewModel(get())
    }
}

val newsAdapterModule = module {

    single {
        NewsAdapter(get())
    }
}