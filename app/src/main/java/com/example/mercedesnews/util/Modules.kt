package com.example.mercedesnews.util

import com.example.mercedesnews.adapters.NewsAdapter
import com.example.mercedesnews.ui.NewsViewModel
import org.koin.dsl.module

val viewModelModule = module {

    single {
        NewsViewModel(get())
    }
}

val newsAdapterModule = module {

    single {
        NewsAdapter(get())
    }
}