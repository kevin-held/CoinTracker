package com.example.cointracker.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cointracker.CoinViewModel
import com.example.cointracker.CoinViewModelFactory
import com.example.cointracker.R
import com.example.cointracker.databinding.FragmentListBinding
import com.example.cointracker.db.CoinDatabase
import com.example.cointracker.db.CoinEntity
import com.example.cointracker.model.Asset

private lateinit var coinViewModel: CoinViewModel

/*
Fragment containing the AssetList
displays search bar and recycler view
 */
class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        // get the viewmodel
        val application = requireActivity().application
        val dao = CoinDatabase.getInstance(application).coinDao
        val viewModelFactory = CoinViewModelFactory(dao)
        coinViewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(CoinViewModel::class.java)

        _binding = FragmentListBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // create the recycler view
        val recyclerView = binding.listRecyclerView
        val adapter = AssetAdapter(coinViewModel.coinListLiveData.value ?: emptyList<CoinEntity>())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())

        // observe the coinList to keep recycler view up to date
        coinViewModel.coinListLiveData.observe(viewLifecycleOwner, Observer {
            adapter.updateAssetList(it.sortedBy { coin -> coin.name })
        })

        // search button
        // currently searching by coin name, could include search by symbol as well
        binding.imageButton.setOnClickListener {
            val searchTerm = binding.search.text.toString().lowercase()
            val newCoinList = coinViewModel.coinListLiveData.value?.filter {
                it.name.lowercase().contains(searchTerm)
            }
            adapter.updateAssetList(newCoinList?.sortedBy { coin -> coin.name } ?: emptyList<CoinEntity>())
            binding.search.text.clear()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // adapter for recycler view
    class AssetAdapter(var assetList: List<CoinEntity>): RecyclerView.Adapter<AssetAdapter.ViewHolder>(){
        inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
            val symbol = itemView.findViewById<TextView>(R.id.symbol)
            val name = itemView.findViewById<TextView>(R.id.name)
            val track = itemView.findViewById<ImageButton>(R.id.trackButton)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val context = parent.context
            val inflater = LayoutInflater.from(context)
            val assetView = inflater.inflate(R.layout.asset_item, parent, false)
            return ViewHolder(assetView)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val entity: CoinEntity = assetList.get(position)
            // set the text for asset item
            holder.symbol.setText(entity.symbol)
            holder.name.setText(entity.name)

            // logic for track button (starred)
            holder.track.isPressed = entity.tracked
            holder.track.setOnClickListener {
                if (!entity.tracked) {
                    // add coin to tracked list
                    entity.tracked = true
                    val asset = Asset(id = entity.assetId, name = entity.name, symbol = entity.symbol)
                    coinViewModel.trackCoin(asset, entity.name)
                } else {
                    // remove from tracked list
                    entity.tracked = false
                    coinViewModel.untrackCoin(entity.name)
                }
                holder.track.isPressed = entity.tracked
            }
        }

        override fun getItemCount(): Int {
            return assetList.size
        }

        fun updateAssetList(assetList: List<CoinEntity>){
            this.assetList = assetList
            notifyDataSetChanged()
        }
    }
}