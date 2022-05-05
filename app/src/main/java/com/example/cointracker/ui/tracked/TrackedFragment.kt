package com.example.cointracker.ui.tracked

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cointracker.CoinViewModel
import com.example.cointracker.CoinViewModelFactory
import com.example.cointracker.R
import com.example.cointracker.databinding.FragmentTrackedBinding
import com.example.cointracker.db.CoinDatabase
import com.example.cointracker.db.CoinEntity
import com.example.cointracker.model.Asset

private lateinit var coinViewModel: CoinViewModel

/*
Class for displaying tracked coin list
 */
class TrackedFragment : Fragment() {

    private var _binding: FragmentTrackedBinding? = null

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

        _binding = FragmentTrackedBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // create recycler view
        val recyclerView = binding.trackedRecyclerView
        val adapter = CoinAdapter(
            coinViewModel.coinListLiveData.value ?: emptyList<CoinEntity>()
        )
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())

        // observe tracked coins
        // sort by market cap rank instead of alphabetically
        coinViewModel.trackedListLiveData.observe(viewLifecycleOwner, Observer {
            adapter.updateAssetList(it.sortedBy { coin -> coin.marketCapRank })
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Adapter for recycler view
    class CoinAdapter(var coinList: List<CoinEntity>): RecyclerView.Adapter<CoinAdapter.ViewHolder>() {
        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val symbol = itemView.findViewById<TextView>(R.id.symbol)
            val name = itemView.findViewById<TextView>(R.id.name)
            val rank = itemView.findViewById<TextView>(R.id.rank)
            val price = itemView.findViewById<TextView>(R.id.price)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): CoinAdapter.ViewHolder {
            val context = parent.context
            val inflater = LayoutInflater.from(context)
            val coinView = inflater.inflate(R.layout.coin_item, parent, false)
            return ViewHolder(coinView)
        }

        override fun onBindViewHolder(holder: CoinAdapter.ViewHolder, position: Int) {
            val entity: CoinEntity = coinList.get(position)
            /*
            if(entity.needsUpdate()){
                val asset = Asset(id = entity.assetId, name = entity.name, symbol = entity.symbol)
                coinViewModel.updateCoin(asset) // get updated info
            }*/
            holder.symbol.setText(entity.symbol)
            holder.name.setText(entity.name)
            holder.rank.setText(entity.marketCapRank.toString())
            holder.price.setText("$%.2f".format(entity.price))

            // display detailed information in Alert Dialog
            holder.itemView.setOnClickListener {
                AlertDialog.Builder(holder.itemView.context)
                    .setTitle(entity.name)
                    .setMessage(entity.displayString())
                    .setNegativeButton("Close",null)
                    .show()
            }
        }

        override fun getItemCount(): Int {
            return coinList.size
        }

        fun updateAssetList(coinList: List<CoinEntity>){
            this.coinList = coinList
            notifyDataSetChanged()
        }

    }
}