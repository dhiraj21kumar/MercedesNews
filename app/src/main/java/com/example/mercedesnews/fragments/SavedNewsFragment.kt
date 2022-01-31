package com.example.mercedesnews.fragments


import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mercedesnews.R
import com.example.mercedesnews.ui.NewsActivity
import com.example.mercedesnews.ui.NewsViewModel
import com.example.mercedesnews.adapters.NewsAdapter
import com.example.mercedesnews.ui.Component
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_saved_news.*
import kotlinx.android.synthetic.main.fragment_top_news.*
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel

class SavedNewsFragment: Fragment(R.layout.fragment_saved_news) {

//    lateinit var viewModel: NewsViewModel
//    lateinit var newsAdapter: NewsAdapter
    private val component = Component()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        viewModel = (activity as NewsActivity).viewModel
        setupRecyclerView()
        component.newsModel

        component.newAdapterModel.setOnItemClickListner {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_savedNewsFragment_to_articleFragment,
                bundle
            )
        }

        val itemTouchHelperCallBack = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = component.newAdapterModel.differ.currentList[position]
                component.newsModel.deleteArticle(article)
                Snackbar.make(view, "Successfully Deleted News", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo") {
                        component.newsModel.saveArticle(article)
                    }
                    show()
                }
            }
        }

        ItemTouchHelper(itemTouchHelperCallBack).apply {
            attachToRecyclerView(rvSavedNews)
        }

        component.newsModel.savedNews().observe(viewLifecycleOwner, Observer { articles ->
            component.newAdapterModel.differ.submitList(articles)

        })

    }

    private fun setupRecyclerView() {
//        newsAdapter = NewsAdapter(get())
        component.newAdapterModel
        rvSavedNews.apply {
            component.newAdapterModel
//            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}