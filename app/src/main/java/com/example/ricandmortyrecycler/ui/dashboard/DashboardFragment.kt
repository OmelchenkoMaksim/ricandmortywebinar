package com.example.ricandmortyrecycler.ui.dashboard

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ricandmortyrecycler.network.ApiProvider
import com.example.ricandmortyrecycler.MainActivity
import com.example.ricandmortyrecycler.R
import com.example.ricandmortyrecycler.databinding.FragmentDashboardBinding
import com.example.ricandmortyrecycler.models.LocationsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var currentPage = 1

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
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val locationsRecyclerView: RecyclerView = view.findViewById(R.id.locationsRecyclerView)
        locationsRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        loadLocations()


        binding.prevButton.setOnClickListener {
            currentPage--
            loadLocations()
        }

        binding.nextButton.setOnClickListener {
            currentPage++
            loadLocations()
        }
    }

    private fun loadLocations() {
        apiProvider.api.getLocations(currentPage)
            .enqueue(object : Callback<LocationsResponse> {
                override fun onResponse(
                    call: Call<LocationsResponse>,
                    response: Response<LocationsResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val listOfLocations = response.body()!!.results
                        Log.e("mylog", "list size is ${listOfLocations.size}")
                        binding.locationsRecyclerView.adapter = LocationsAdapter(listOfLocations)

                        // Обновление кнопок на основе данных ответа
                        binding.prevButton.isEnabled = response.body()!!.info.prev != null
                        binding.nextButton.isEnabled = response.body()!!.info.next != null
                    } else {
                        Log.e("mylog", "empty " + response.toString() + response.body().toString())
                    }
                }

                override fun onFailure(call: Call<LocationsResponse>, t: Throwable) {
                    Log.e("mylog", "onFailure $t")
                }
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
