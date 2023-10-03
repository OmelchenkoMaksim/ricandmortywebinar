package com.example.ricandmortyrecycler.ui.home

import android.content.Context
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
import com.example.ricandmortyrecycler.network.ApiProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment(), OnSwitchClickListener {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    private var currentPage = 1

    // Создание адаптера один раз:
    private val rickMortyAdapter = RickMortyAdapter(mutableListOf(), this)
//    private val rickMortyAdapter = RickMortyAdapterDiffUtil()


    private lateinit var apiProvider: ApiProvider

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) {
            apiProvider = context.apiProvider
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.charactersRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
//       Горизонтальный
//        binding.charactersRecyclerView.layoutManager =
//            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        binding.charactersRecyclerView.adapter =
            rickMortyAdapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        scrollListener()

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

    // Обработчик прокрутки RecyclerView
    private fun scrollListener() {
        binding.charactersRecyclerView.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            // Этот метод вызывается при прокрутке
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
    }

    private fun loadCharacters() {
        // Запрос к API
        apiProvider.api.getCharacters(currentPage)
//      Асинхронный вызов к API. Интерфейс Callback реализуется анонимнымным классом.
            .enqueue(object : Callback<CharactersResponse> {
                override fun onResponse( // Этот метод будет вызван, если сервер вернёт ответ. Не важно, успешный он или нет.
                    call: Call<CharactersResponse>,
                    response: Response<CharactersResponse>
                ) {
                    // Проверка на успешный ответ и наличие тела ответа
                    if (response.isSuccessful && response.body() != null) {

                        characterAdapter(response)

//                        rickAdapterWithManyItems(response)

//                        rickAdapterSmooth(response)

//                        rickAdapterSmoothDiffUtil(response)


                        // Обновление кнопок на основе данных ответа
                        binding.prevButton.isEnabled = response.body()!!.info.prev != null
                        binding.nextButton.isEnabled = response.body()!!.info.next != null

//           вызов response.raw().body.string() будет потреблять тело ответа, и вы не сможете его повторно прочитать
                        Log.w("mylog", "RAW RESPONSE Headers: ${response.raw().headers}")
                        Log.w("mylog", "RAW RESPONSE Body: ${response.raw().body}")


                    } else {
                        Log.e("mylog", "empty " + response.toString() + response.body().toString())
                    }
                }

                override fun onFailure(call: Call<CharactersResponse>, t: Throwable) {
                    Log.e("mylog", "onFailure $t")
                }
            })
    }


    // самый простой
    private fun characterAdapter(response: Response<CharactersResponse>) {
        val listOfCharacters = response.body()!!.results
        Log.e("mylog", "list size is ${listOfCharacters.size}")
        binding.charactersRecyclerView.adapter = CharactersAdapter(listOfCharacters)
    }

    private fun rickAdapterWithManyItems(response: Response<CharactersResponse>) {
        val items = mutableListOf<RickMortyItem>()

        items.add(RickMortyItem.Title("Герои из мира Rick и Morty"))
        items.add(RickMortyItem.Description("Здесь представлены различные герои..."))

        items.addAll(response.body()!!.results)

        binding.charactersRecyclerView.adapter = RickMortyAdapter(items, this)
    }

    /**
     * Адаптер с плавной подгрузкой
     */
    private fun rickAdapterSmooth(response: Response<CharactersResponse>) {
        with(mutableListOf<RickMortyItem>()) {
            add(RickMortyItem.Title("Герои из мира Rick и Morty"))
            add(RickMortyItem.Description("Здесь представлены различные герои..."))

            addAll(response.body()!!.results)
            rickMortyAdapter as RickMortyAdapter
// плавность работает за счет этого метода!
           rickMortyAdapter.addItemsInMyRecycler(this)
        }
    }

    //    этот список нужен для плавного списка с DiffUtil
//    он сет, потому что в лист будут добавлятся элементы повторно
    private val allItems = mutableSetOf<RickMortyItem>()

    /*Адаптер с плавной подгрузкой*/
    private fun rickAdapterSmoothDiffUtil(response: Response<CharactersResponse>) {
        val newItems = mutableListOf<RickMortyItem>()

        newItems.add(RickMortyItem.Title("Герои из мира Rick и Morty"))
        newItems.add(RickMortyItem.Description("Здесь представлены различные герои..."))

        newItems.addAll(response.body()!!.results)

        allItems.addAll(newItems)  // Добавляем новые элементы к существующим
// если поместить переменную адаптера сюда, то будет не сохраниять состояние, потому приведение
        rickMortyAdapter as RickMortyAdapterDiffUtil
        /*
        Метод submitList() является ключевым для работы ListAdapter, и он нужен для инициализации
        или обновления данных, которые должны быть отображены.
        Если этот метод не вызывается, адаптер не будет иметь "сырья" для работы */
        rickMortyAdapter.submitList(allItems.toList())  // Обновляем адаптер с полным списком элементов

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
