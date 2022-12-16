package pjatk.smb

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pjatk.smb.databinding.StoreElementBinding
import pjatk.smb.db.Store

class StoreAdapter(
    private val storeViewModel: StoreViewModel,
) : RecyclerView.Adapter<StoreAdapter.ViewHolder>() {
    private var stores = emptyList<Store>()

    class ViewHolder(val binding: StoreElementBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = StoreElementBinding.inflate(inflater)
        return ViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val store = stores[position]
        holder.binding.storeFullDescription.text = """
            ${store.name}, ${store.description}, ${store.radius} kilometers, ${store.latitude}, ${store.longitude}
        """.trimIndent()
    }


    override fun getItemCount(): Int = stores.size

    fun add(store: Store) {
        CoroutineScope(Dispatchers.IO).launch {
            storeViewModel.insert(store)
        }
        notifyDataSetChanged()
    }


    fun setStores(stores: List<Store>) {
        this.stores = stores
        notifyDataSetChanged()
    }

    fun getStores(): List<Store> {
        return storeViewModel.stores.value ?: emptyList()
    }

}
