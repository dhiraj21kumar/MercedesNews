package com.example.mercedesnews.ui


import com.example.mercedesnews.adapters.NewsAdapter
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@OptIn(KoinApiExtension::class)
class Component: KoinComponent {

    val newsModel: NewsViewModel by inject()
    val newAdapterModel: NewsAdapter by inject()

}