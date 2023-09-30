package com.example.ricandmortyrecycler.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ricandmortyrecycler.MainActivity
import com.example.ricandmortyrecycler.databinding.FragmentHomeBinding
import com.example.ricandmortyrecycler.models.CharactersResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    private var currentPage = 1


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.charactersRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.charactersRecyclerView.adapter =
            CharactersAdapter(emptyList()) // Пустой адаптер для начала

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
                        val listOfCharacters = response.body()!!.results
                        Log.e("mylog", "list size is ${listOfCharacters.size}")
                        binding.charactersRecyclerView.adapter = CharactersAdapter(listOfCharacters)

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


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

