package com.example.ricandmortyrecycler.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ricandmortyrecycler.MainActivity
import com.example.ricandmortyrecycler.databinding.FragmentHomeBinding
import com.example.ricandmortyrecycler.models.CharactersResponse
import com.example.ricandmortyrecycler.models.RickMortyItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment(), OnSwitchClickListener {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    private var currentPage = 1

    // Создание адаптера один раз:
    private val rickMortyAdapter = RickMortyAdapter(mutableListOf(), this)



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.charactersRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.charactersRecyclerView.adapter =
            rickMortyAdapter // Пустой адаптер для начала

        binding.charactersRecyclerView.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = binding.charactersRecyclerView.layoutManager

                /*
Эта проверка связана с тем, что binding.charactersRecyclerView.layoutManager
имеет тип LayoutManager?, но GridLayoutManager содержит дополнительные методы,
такие как findLastVisibleItemPosition(), которых нет в родительском LayoutManager.
*/                  if (layoutManager is GridLayoutManager) {

                    // Позиция последнего видимого элемента
                    val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

                    // Общее количество элементов
                    val totalItemCount = layoutManager.itemCount

                    // Условие, определяющее, когда нужно подгрузить новые данные
                    val isLoadingNeeded = lastVisibleItemPosition + 1 == totalItemCount

                    if (isLoadingNeeded) {
                        // Загрузите больше данных
                        currentPage++
                        loadCharacters()
                    }
                }
            }
        })

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadCharacters()



        binding.prevButton.setOnClickListener {
            currentPage--
            loadCharacters()
        }

        binding.nextButton.setOnClickListener {
            currentPage++
            loadCharacters()
        }


    }

    private fun loadCharacters() {
        MainActivity.api!!.getCharacters(currentPage)
            .enqueue(object : Callback<CharactersResponse> {
                override fun onResponse(
                    call: Call<CharactersResponse>,
                    response: Response<CharactersResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {

//                        characterAdapter(response)


//                        rickAdapterWithManyItems(response)


                        rickAdapterSmooth(response)


                        // Обновление кнопок на основе данных ответа
                        binding.prevButton.isEnabled = response.body()!!.info.prev != null
                        binding.nextButton.isEnabled = response.body()!!.info.next != null

                    } else {
                        Log.e("mylog", "empty " + response.toString() + response.body().toString())
                    }
                }

                override fun onFailure(call: Call<CharactersResponse>, t: Throwable) {
                    Log.e("mylog", "onFailure $t")
                }
            })
    }

    private fun rickAdapterWithManyItems(response: Response<CharactersResponse>) {
        val items = mutableListOf<RickMortyItem>()

        items.add(RickMortyItem.Title("Герои из мира Rick и Morty"))
        items.add(RickMortyItem.Description("Здесь представлены различные герои..."))

        items.addAll(response.body()!!.results.map { character ->
            RickMortyItem.CharacterInfo(character)
        })
        binding.charactersRecyclerView.adapter = RickMortyAdapter(items, this)
    }

    /**
     * Адаптер с плавной подгрузкой
     */
    private fun rickAdapterSmooth(response: Response<CharactersResponse>) {
        val newItems = mutableListOf<RickMortyItem>()
        if (currentPage % 10 == 0) {
            newItems.add(RickMortyItem.Title("Герои из мира Rick и Morty"))
            newItems.add(RickMortyItem.Description("Здесь представлены различные герои..."))
        }
        newItems.addAll(response.body()!!.results.map { character ->
            RickMortyItem.CharacterInfo(character)
        })

        rickMortyAdapter.addItems(newItems)
    }

    private fun characterAdapter(response: Response<CharactersResponse>) {
        val listOfCharacters = response.body()!!.results
        Log.e("mylog", "list size is ${listOfCharacters.size}")
        binding.charactersRecyclerView.adapter = CharactersAdapter(listOfCharacters)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onSwitchClicked() {
        currentPage++
        loadCharacters()
    }

}
